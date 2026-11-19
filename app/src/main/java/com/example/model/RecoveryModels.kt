package com.example.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class UserChallengeProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Believer",
    val niyyahTitle: String = "Clean Heart & Peace with Allah",
    val challengeStartMillis: Long = 0L,
    val targetDays: Long = 40L,
    val longestStreakDays: Long = 0L,
    val urgesOvercomeCount: Long = 0L,
    val totalResetsCount: Long = 0L,
    val completedEpisodes: List<Long> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

enum class UrgeOutcome {
    OVERCOME,
    SLIP;

    companion object {
        fun fromRaw(raw: String?): UrgeOutcome =
            entries.firstOrNull { it.name == raw } ?: OVERCOME
    }
}

@Keep
@IgnoreExtraProperties
data class UrgeLogEntry(
    @DocumentId val id: String = "",
    val userId: String = "",
    val outcome: String = UrgeOutcome.OVERCOME.name,
    val triggerNote: String = "",
    val intensity: Long = 3L,
    val dhikrCount: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@Keep
@IgnoreExtraProperties
data class EpisodeReflection(
    @DocumentId val id: String = "",
    val userId: String = "",
    val episodeNumber: Long = 1L,
    val reflection: String = "",
    val completed: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
