package com.example.data

import android.content.Context
import com.example.R
import com.example.model.EpisodeReflection
import com.example.model.UrgeLogEntry
import com.example.model.UrgeOutcome
import com.example.model.UserChallengeProfile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class RecoveryRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireMatchingUserId(expectedUserId: String): String {
        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("Please sign in with Google to save your progress.")
        check(uid == expectedUserId) { "Signed-in account changed; refusing cross-user write." }
        return uid
    }

    fun observeProfile(userId: String): Flow<UserChallengeProfile?> {
        return db.collection("users").document(userId)
            .snapshots()
            .map { snapshot ->
                if (!snapshot.exists()) null
                else snapshot.toObject(
                    UserChallengeProfile::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
    }

    fun observeUrgeLogs(userId: String): Flow<List<UrgeLogEntry>> {
        return db.collection("users").document(userId)
            .collection("urgeLogs")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    UrgeLogEntry::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                )
            }
    }

    fun observeEpisodeReflections(userId: String): Flow<Map<Int, EpisodeReflection>> {
        return db.collection("users").document(userId)
            .collection("episodeNotes")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(
                    EpisodeReflection::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ).associateBy { it.episodeNumber.toInt() }
            }
    }

    suspend fun ensureInitialProfile(
        userId: String,
        displayName: String
    ): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireMatchingUserId(userId)
            val docRef = db.collection("users").document(uid)
            val existing = docRef.get().await()
            if (!existing.exists()) {
                val safeName = displayName.trim().ifBlank { "Believer" }.take(60)
                val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
                val payload = mapOf(
                    "userId" to uid,
                    "displayName" to safeName,
                    "niyyahTitle" to "Clean Heart & Peace with Allah",
                    "challengeStartMillis" to nowMillis,
                    "targetDays" to 40L,
                    "longestStreakDays" to 0L,
                    "urgesOvercomeCount" to 0L,
                    "totalResetsCount" to 0L,
                    "completedEpisodes" to emptyList<Long>(),
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun updateChallengeConfig(
        userId: String,
        displayName: String,
        niyyahTitle: String,
        challengeStartMillis: Long,
        targetDays: Long
    ): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireMatchingUserId(userId)
            val cleanName = displayName.trim().ifBlank { "Believer" }.take(60)
            val cleanNiyyah = niyyahTitle.trim().ifBlank { "Clean Heart & Peace with Allah" }.take(100)
            val safeStart = challengeStartMillis.coerceIn(1L, System.currentTimeMillis() + 60_000L)
            val safeTarget = targetDays.coerceIn(1L, 3650L)

            db.collection("users").document(uid)
                .update(
                    mapOf(
                        "displayName" to cleanName,
                        "niyyahTitle" to cleanNiyyah,
                        "challengeStartMillis" to safeStart,
                        "targetDays" to safeTarget,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun recordUrgeVictory(
        userId: String,
        newUrgesOvercomeCount: Long,
        longestStreakDays: Long,
        triggerNote: String,
        intensity: Long,
        dhikrCount: Long
    ): Result<Unit> {
        val logPath = "users/$userId/urgeLogs"
        return try {
            val uid = requireMatchingUserId(userId)
            val cleanNote = triggerNote.trim().take(300)
            val safeIntensity = intensity.coerceIn(1L, 5L)
            val safeDhikr = dhikrCount.coerceIn(0L, 10000L)
            val safeCount = newUrgesOvercomeCount.coerceIn(0L, 1_000_000L)
            val safeLongest = longestStreakDays.coerceIn(0L, 36500L)

            val userRef = db.collection("users").document(uid)
            val logRef = userRef.collection("urgeLogs").document()

            val logPayload = mapOf(
                "userId" to uid,
                "outcome" to UrgeOutcome.OVERCOME.name,
                "triggerNote" to cleanNote,
                "intensity" to safeIntensity,
                "dhikrCount" to safeDhikr,
                "createdAt" to FieldValue.serverTimestamp()
            )
            logRef.set(logPayload).await()

            userRef.update(
                mapOf(
                    "urgesOvercomeCount" to safeCount,
                    "longestStreakDays" to safeLongest,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, logPath)
            Result.failure(e)
        }
    }

    suspend fun recordSlipAndReset(
        userId: String,
        newTotalResetsCount: Long,
        newStartMillis: Long,
        longestStreakDays: Long,
        triggerNote: String
    ): Result<Unit> {
        val logPath = "users/$userId/urgeLogs"
        return try {
            val uid = requireMatchingUserId(userId)
            val cleanNote = triggerNote.trim().take(300)
            val safeResets = newTotalResetsCount.coerceIn(0L, 1_000_000L)
            val safeLongest = longestStreakDays.coerceIn(0L, 36500L)
            val safeStart = newStartMillis.coerceAtLeast(1L)

            val userRef = db.collection("users").document(uid)
            val logRef = userRef.collection("urgeLogs").document()

            val logPayload = mapOf(
                "userId" to uid,
                "outcome" to UrgeOutcome.SLIP.name,
                "triggerNote" to cleanNote,
                "intensity" to 5L,
                "dhikrCount" to 0L,
                "createdAt" to FieldValue.serverTimestamp()
            )
            logRef.set(logPayload).await()

            userRef.update(
                mapOf(
                    "challengeStartMillis" to safeStart,
                    "totalResetsCount" to safeResets,
                    "longestStreakDays" to safeLongest,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, logPath)
            Result.failure(e)
        }
    }

    suspend fun saveEpisodeReflection(
        userId: String,
        episodeNumber: Int,
        reflection: String,
        completed: Boolean,
        updatedCompletedList: List<Long>
    ): Result<Unit> {
        val epId = "ep_${episodeNumber.coerceIn(1, 11)}"
        val path = "users/$userId/episodeNotes/$epId"
        return try {
            val uid = requireMatchingUserId(userId)
            require(episodeNumber in 1..11) { "Episode number must be between 1 and 11." }
            val cleanReflection = reflection.trim().take(500)
            val safeList = updatedCompletedList
                .map { it.coerceIn(1L, 11L) }
                .distinct()
                .sorted()
                .take(11)

            val userRef = db.collection("users").document(uid)
            val epRef = userRef.collection("episodeNotes").document(epId)
            val existing = epRef.get().await()

            if (!existing.exists()) {
                epRef.set(
                    mapOf(
                        "userId" to uid,
                        "episodeNumber" to episodeNumber.toLong(),
                        "reflection" to cleanReflection,
                        "completed" to completed,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                epRef.update(
                    mapOf(
                        "reflection" to cleanReflection,
                        "completed" to completed,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }

            userRef.update(
                mapOf(
                    "completedEpisodes" to safeList,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }
}
