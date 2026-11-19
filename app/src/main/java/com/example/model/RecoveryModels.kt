package com.example.model

data class UserChallengeProfile(
    val id: String = "local_user",
    val userId: String = "local_user",
    val displayName: String = "Believer",
    val niyyahTitle: String = "Clean Heart & Peace with Allah",
    val challengeStartMillis: Long = 0L,
    val targetDays: Long = 40L,
    val longestStreakDays: Long = 0L,
    val urgesOvercomeCount: Long = 0L,
    val totalResetsCount: Long = 0L,
    val completedEpisodes: List<Long> = emptyList(),
    val createdAtMillis: Long = 0L,
    val updatedAtMillis: Long = 0L
)

enum class UrgeOutcome {
    OVERCOME,
    SLIP;

    companion object {
        fun fromRaw(raw: String?): UrgeOutcome =
            entries.firstOrNull { it.name == raw } ?: OVERCOME
    }
}

data class UrgeLogEntry(
    val id: String = "",
    val outcome: String = UrgeOutcome.OVERCOME.name,
    val triggerNote: String = "",
    val intensity: Long = 3L,
    val dhikrCount: Long = 0L,
    val createdAtMillis: Long = 0L
)

data class EpisodeReflection(
    val id: String = "",
    val episodeNumber: Long = 1L,
    val reflection: String = "",
    val completed: Boolean = false,
    val createdAtMillis: Long = 0L,
    val updatedAtMillis: Long = 0L
)

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
