package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RecoveryRepository
import com.example.model.EpisodeReflection
import com.example.model.UiState
import com.example.model.UrgeLogEntry
import com.example.model.UserChallengeProfile
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
    private val repository: RecoveryRepository
) : ViewModel() {

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

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
        repository.observeProfile()
            .map<UserChallengeProfile?, UiState<UserChallengeProfile>> { profile ->
                if (profile != null) {
                    UiState.Success(profile)
                } else {
                    UiState.Loading
                }
            }
            .catch { error ->
                if (error is CancellationException) throw error
                Log.w(TAG, "Error observing local profile", error)
                emit(UiState.Error(error.message ?: "Unable to load your local challenge profile."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    val urgeLogsState: StateFlow<UiState<List<UrgeLogEntry>>> =
        repository.observeUrgeLogs()
            .map<List<UrgeLogEntry>, UiState<List<UrgeLogEntry>>> { UiState.Success(it) }
            .catch { error ->
                if (error is CancellationException) throw error
                Log.w(TAG, "Error observing local urge logs", error)
                emit(UiState.Error(error.message ?: "Unable to load urge history."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    val reflectionsState: StateFlow<UiState<Map<Int, EpisodeReflection>>> =
        repository.observeEpisodeReflections()
            .map<Map<Int, EpisodeReflection>, UiState<Map<Int, EpisodeReflection>>> {
                UiState.Success(it)
            }
            .catch { error ->
                if (error is CancellationException) throw error
                Log.w(TAG, "Error observing local episode notes", error)
                emit(UiState.Error(error.message ?: "Unable to load episode progress."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )

    init {
        viewModelScope.launch {
            val result = repository.ensureInitialProfile()
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
        episodeNumber: Int,
        reflection: String,
        completed: Boolean,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            val result = repository.saveEpisodeReflection(
                episodeNumber = episodeNumber,
                reflection = reflection,
                completed = completed
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
