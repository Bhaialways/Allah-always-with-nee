package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChallengeProfileEntity::class,
        UrgeLogEntity::class,
        EpisodeReflectionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RecoveryDatabase : RoomDatabase() {
    abstract fun recoveryDao(): RecoveryDao

    companion object {
        @Volatile
        private var INSTANCE: RecoveryDatabase? = null

        fun getInstance(context: Context): RecoveryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecoveryDatabase::class.java,
                    "sanctuary_recovery.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
