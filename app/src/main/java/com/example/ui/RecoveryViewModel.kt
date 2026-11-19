package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RecoveryRepository
import com.example.model.EpisodeReflection
import com.example.model.UiState
import com.example.model.UrgeLogEntry
import com.example.model.UserChallengeProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecoveryViewModel(
    private val repository: RecoveryRepository,
    private val currentUserId: String,
    private val initialDisplayName: String
) : ViewModel() {

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    // Live 1-second ticker for the Sober Step elapsed counter
    val currentTimeMillis: StateFlow<Long> = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000L)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = System.currentTimeMillis()
    )

    val profileState: StateFlow<UiState<UserChallengeProfile>> =
        repository.observeProfile(currentUserId)
            .map<UserChallengeProfile?, UiState<UserChallengeProfile>> { profile ->
                if (profile != null) {
                    UiState.Success(profile)
                } else {
                    UiState.Loading
                }
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                Log.w(TAG, "Error observing user profile", root)
                emit(UiState.Error(root.message ?: "Unable to load your challenge profile."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    val urgeLogsState: StateFlow<UiState<List<UrgeLogEntry>>> =
        repository.observeUrgeLogs(currentUserId)
            .map<List<UrgeLogEntry>, UiState<List<UrgeLogEntry>>> { UiState.Success(it) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                Log.w(TAG, "Error observing urge logs", root)
                emit(UiState.Error(root.message ?: "Unable to load urge history."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    val reflectionsState: StateFlow<UiState<Map<Int, EpisodeReflection>>> =
        repository.observeEpisodeReflections(currentUserId)
            .map<Map<Int, EpisodeReflection>, UiState<Map<Int, EpisodeReflection>>> {
                UiState.Success(it)
            }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val root = firestoreError ?: error
                Log.w(TAG, "Error observing episode notes", root)
                emit(UiState.Error(root.message ?: "Unable to load episode progress."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    init {
        viewModelScope.launch {
            val result = repository.ensureInitialProfile(currentUserId, initialDisplayName)
            result.onFailure { err ->
                _actionMessage.value = err.localizedMessage ?: "Failed to initialize profile."
            }
        }
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }

    fun updateChallengeConfig(
        displayName: String,
        niyyahTitle: String,
        challengeStartMillis: Long,
        targetDays: Long,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val result = repository.updateChallengeConfig(
                userId = currentUserId,
                displayName = displayName,
                niyyahTitle = niyyahTitle,
                challengeStartMillis = challengeStartMillis,
                targetDays = targetDays
            )
            _isSaving.value = false
            result.onSuccess {
                _actionMessage.value = "Challenge start time & Niyyah saved. Allah is with you."
                onComplete()
            }.onFailure { err ->
                _actionMessage.value = err.localizedMessage ?: "Could not update challenge settings."
            }
        }
    }

    fun recordUrgeVictory(
        profile: UserChallengeProfile,
        triggerNote: String,
        intensity: Int,
        dhikrCount: Int,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val elapsedDays = computeElapsedDays(profile.challengeStartMillis)
            val bestStreak = maxOf(profile.longestStreakDays, elapsedDays)
            val result = repository.recordUrgeVictory(
                userId = currentUserId,
                newUrgesOvercomeCount = profile.urgesOvercomeCount + 1L,
                longestStreakDays = bestStreak,
                triggerNote = triggerNote,
                intensity = intensity.toLong(),
                dhikrCount = dhikrCount.toLong()
            )
            _isSaving.value = false
            result.onSuccess {
                _actionMessage.value = "Alhamdulillah! You overcame this urge for Allah's sake."
                onComplete()
            }.onFailure { err ->
                _actionMessage.value = err.localizedMessage ?: "Could not save urge victory."
            }
        }
    }

    fun recordSlipAndReset(
        profile: UserChallengeProfile,
        triggerNote: String,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val elapsedDays = computeElapsedDays(profile.challengeStartMillis)
            val bestStreak = maxOf(profile.longestStreakDays, elapsedDays)
            val nowMillis = System.currentTimeMillis()
            val result = repository.recordSlipAndReset(
                userId = currentUserId,
                newTotalResetsCount = profile.totalResetsCount + 1L,
                newStartMillis = nowMillis,
                longestStreakDays = bestStreak,
                triggerNote = triggerNote
            )
            _isSaving.value = false
            result.onSuccess {
                _actionMessage.value = "New clean start begun. Allah loves those who turn back to Him."
                onComplete()
            }.onFailure { err ->
                _actionMessage.value = err.localizedMessage ?: "Could not reset streak."
            }
        }
    }

    fun saveEpisodeReflection(
        profile: UserChallengeProfile,
        episodeNumber: Int,
        reflection: String,
        completed: Boolean,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val currentList = profile.completedEpisodes.toMutableSet()
            if (completed) {
                currentList.add(episodeNumber.toLong())
            } else {
                currentList.remove(episodeNumber.toLong())
            }
            val result = repository.saveEpisodeReflection(
                userId = currentUserId,
                episodeNumber = episodeNumber,
                reflection = reflection,
                completed = completed,
                updatedCompletedList = currentList.toList()
            )
            _isSaving.value = false
            result.onSuccess {
                _actionMessage.value = if (completed) {
                    "Episode $episodeNumber completed. May Allah bless your step!"
                } else {
                    "Reflection saved for Episode $episodeNumber."
                }
                onComplete()
            }.onFailure { err ->
                _actionMessage.value = err.localizedMessage ?: "Could not save episode progress."
            }
        }
    }

    private fun computeElapsedDays(startMillis: Long): Long {
        if (startMillis <= 0L) return 0L
        val diff = (System.currentTimeMillis() - startMillis).coerceAtLeast(0L)
        return diff / MILLIS_PER_DAY
    }

    private companion object {
        const val TAG = "RecoveryVM"
        const val STOP_TIMEOUT_MILLIS = 5000L
        const val MILLIS_PER_DAY = 86_400_000L
    }
}
