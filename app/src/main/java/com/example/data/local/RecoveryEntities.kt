package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_profile")
data class ChallengeProfileEntity(
    @PrimaryKey val id: Int = 1,
    val displayName: String = "Believer",
    val niyyahTitle: String = "Clean Heart & Peace with Allah",
    val challengeStartMillis: Long = 0L,
    val targetDays: Long = 40L,
    val longestStreakDays: Long = 0L,
    val urgesOvercomeCount: Long = 0L,
    val totalResetsCount: Long = 0L,
    val createdAtMillis: Long = 0L,
    val updatedAtMillis: Long = 0L
)

@Entity(tableName = "urge_logs")
data class UrgeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val outcome: String,
    val triggerNote: String = "",
    val intensity: Long = 3L,
    val dhikrCount: Long = 0L,
    val createdAtMillis: Long = 0L
)

@Entity(tableName = "episode_reflections")
data class EpisodeReflectionEntity(
    @PrimaryKey val episodeNumber: Int,
    val reflection: String = "",
    val completed: Boolean = false,
    val createdAtMillis: Long = 0L,
    val updatedAtMillis: Long = 0L
)
