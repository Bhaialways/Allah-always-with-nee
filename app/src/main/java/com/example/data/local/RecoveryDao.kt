package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecoveryDao {

    @Query("SELECT * FROM challenge_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<ChallengeProfileEntity?>

    @Query("SELECT * FROM challenge_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileOnce(): ChallengeProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ChallengeProfileEntity)

    @Query("SELECT * FROM urge_logs ORDER BY createdAtMillis DESC LIMIT 100")
    fun observeUrgeLogs(): Flow<List<UrgeLogEntity>>

    @Insert
    suspend fun insertUrgeLog(entry: UrgeLogEntity): Long

    @Query("SELECT * FROM episode_reflections ORDER BY episodeNumber ASC")
    fun observeEpisodeReflections(): Flow<List<EpisodeReflectionEntity>>

    @Query("SELECT * FROM episode_reflections WHERE episodeNumber = :episodeNumber LIMIT 1")
    suspend fun getEpisodeReflectionOnce(episodeNumber: Int): EpisodeReflectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEpisodeReflection(entry: EpisodeReflectionEntity)
}
