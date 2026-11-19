package com.example.data

import android.content.Context
import com.example.data.local.ChallengeProfileEntity
import com.example.data.local.EpisodeReflectionEntity
import com.example.data.local.RecoveryDao
import com.example.data.local.RecoveryDatabase
import com.example.data.local.UrgeLogEntity
import com.example.model.EpisodeReflection
import com.example.model.UrgeLogEntry
import com.example.model.UrgeOutcome
import com.example.model.UserChallengeProfile
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RecoveryRepository(
    private val dao: RecoveryDao
) {
    constructor(context: Context) : this(
        RecoveryDatabase.getInstance(context.applicationContext).recoveryDao()
    )

    fun observeProfile(): Flow<UserChallengeProfile?> {
        return combine(
            dao.observeProfile(),
            dao.observeEpisodeReflections()
        ) { entity, reflections ->
            entity?.let { profile ->
                val completedList = reflections
                    .filter { it.completed }
                    .map { it.episodeNumber.toLong() }
                    .sorted()
                UserChallengeProfile(
                    id = profile.id.toString(),
                    userId = "local_user",
                    displayName = profile.displayName,
                    niyyahTitle = profile.niyyahTitle,
                    challengeStartMillis = profile.challengeStartMillis,
                    targetDays = profile.targetDays,
                    longestStreakDays = profile.longestStreakDays,
                    urgesOvercomeCount = profile.urgesOvercomeCount,
                    totalResetsCount = profile.totalResetsCount,
                    completedEpisodes = completedList,
                    createdAtMillis = profile.createdAtMillis,
                    updatedAtMillis = profile.updatedAtMillis
                )
            }
        }
    }

    fun observeUrgeLogs(): Flow<List<UrgeLogEntry>> {
        return dao.observeUrgeLogs().map { entities ->
            entities.map { entity ->
                UrgeLogEntry(
                    id = entity.id.toString(),
                    outcome = entity.outcome,
                    triggerNote = entity.triggerNote,
                    intensity = entity.intensity,
                    dhikrCount = entity.dhikrCount,
                    createdAtMillis = entity.createdAtMillis
                )
            }
        }
    }

    fun observeEpisodeReflections(): Flow<Map<Int, EpisodeReflection>> {
        return dao.observeEpisodeReflections().map { entities ->
            entities.associate { entity ->
                entity.episodeNumber to EpisodeReflection(
                    id = "ep_${entity.episodeNumber}",
                    episodeNumber = entity.episodeNumber.toLong(),
                    reflection = entity.reflection,
                    completed = entity.completed,
                    createdAtMillis = entity.createdAtMillis,
                    updatedAtMillis = entity.updatedAtMillis
                )
            }
        }
    }

    suspend fun ensureInitialProfile(displayName: String = "Believer"): Result<Unit> {
        return try {
            val existing = dao.getProfileOnce()
            if (existing == null) {
                val safeName = displayName.trim().ifBlank { "Believer" }.take(60)
                val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
                dao.upsertProfile(
                    ChallengeProfileEntity(
                        id = 1,
                        displayName = safeName,
                        niyyahTitle = "Clean Heart & Peace with Allah",
                        challengeStartMillis = nowMillis,
                        targetDays = 40L,
                        longestStreakDays = 0L,
                        urgesOvercomeCount = 0L,
                        totalResetsCount = 0L,
                        createdAtMillis = nowMillis,
                        updatedAtMillis = nowMillis
                    )
                )
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateChallengeConfig(
        displayName: String,
        niyyahTitle: String,
        challengeStartMillis: Long,
        targetDays: Long
    ): Result<Unit> {
        return try {
            val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
            val existing = dao.getProfileOnce() ?: ChallengeProfileEntity(
                id = 1,
                createdAtMillis = nowMillis,
                updatedAtMillis = nowMillis
            )
            val cleanName = displayName.trim().ifBlank { "Believer" }.take(60)
            val cleanNiyyah = niyyahTitle.trim().ifBlank { "Clean Heart & Peace with Allah" }.take(100)
            val safeStart = challengeStartMillis.coerceIn(1L, nowMillis + 60_000L)
            val safeTarget = targetDays.coerceIn(1L, 3650L)

            dao.upsertProfile(
                existing.copy(
                    displayName = cleanName,
                    niyyahTitle = cleanNiyyah,
                    challengeStartMillis = safeStart,
                    targetDays = safeTarget,
                    updatedAtMillis = nowMillis
                )
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordUrgeVictory(
        newUrgesOvercomeCount: Long,
        longestStreakDays: Long,
        triggerNote: String,
        intensity: Long,
        dhikrCount: Long
    ): Result<Unit> {
        return try {
            val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
            val cleanNote = triggerNote.trim().take(300)
            val safeIntensity = intensity.coerceIn(1L, 5L)
            val safeDhikr = dhikrCount.coerceIn(0L, 10000L)
            val safeCount = newUrgesOvercomeCount.coerceIn(0L, 1_000_000L)
            val safeLongest = longestStreakDays.coerceIn(0L, 36500L)

            dao.insertUrgeLog(
                UrgeLogEntity(
                    outcome = UrgeOutcome.OVERCOME.name,
                    triggerNote = cleanNote,
                    intensity = safeIntensity,
                    dhikrCount = safeDhikr,
                    createdAtMillis = nowMillis
                )
            )

            val existing = dao.getProfileOnce() ?: ChallengeProfileEntity(
                id = 1,
                challengeStartMillis = nowMillis,
                createdAtMillis = nowMillis,
                updatedAtMillis = nowMillis
            )
            dao.upsertProfile(
                existing.copy(
                    urgesOvercomeCount = safeCount,
                    longestStreakDays = safeLongest,
                    updatedAtMillis = nowMillis
                )
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordSlipAndReset(
        newTotalResetsCount: Long,
        newStartMillis: Long,
        longestStreakDays: Long,
        triggerNote: String
    ): Result<Unit> {
        return try {
            val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
            val cleanNote = triggerNote.trim().take(300)
            val safeResets = newTotalResetsCount.coerceIn(0L, 1_000_000L)
            val safeLongest = longestStreakDays.coerceIn(0L, 36500L)
            val safeStart = newStartMillis.coerceAtLeast(1L)

            dao.insertUrgeLog(
                UrgeLogEntity(
                    outcome = UrgeOutcome.SLIP.name,
                    triggerNote = cleanNote,
                    intensity = 5L,
                    dhikrCount = 0L,
                    createdAtMillis = nowMillis
                )
            )

            val existing = dao.getProfileOnce() ?: ChallengeProfileEntity(
                id = 1,
                createdAtMillis = nowMillis,
                updatedAtMillis = nowMillis
            )
            dao.upsertProfile(
                existing.copy(
                    challengeStartMillis = safeStart,
                    totalResetsCount = safeResets,
                    longestStreakDays = safeLongest,
                    updatedAtMillis = nowMillis
                )
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveEpisodeReflection(
        episodeNumber: Int,
        reflection: String,
        completed: Boolean
    ): Result<Unit> {
        return try {
            require(episodeNumber in 1..11) { "Episode number must be between 1 and 11." }
            val nowMillis = System.currentTimeMillis().coerceAtLeast(1L)
            val cleanReflection = reflection.trim().take(500)
            val existing = dao.getEpisodeReflectionOnce(episodeNumber)

            dao.upsertEpisodeReflection(
                EpisodeReflectionEntity(
                    episodeNumber = episodeNumber,
                    reflection = cleanReflection,
                    completed = completed,
                    createdAtMillis = existing?.createdAtMillis ?: nowMillis,
                    updatedAtMillis = nowMillis
                )
            )
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
