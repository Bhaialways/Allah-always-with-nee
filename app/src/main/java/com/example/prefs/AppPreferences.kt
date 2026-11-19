package com.example.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appearanceDataStore by preferencesDataStore(name = "sanctuary_appearance_prefs")

enum class AppLanguage(val code: String, val label: String, val nativeLabel: String) {
    ENGLISH("en", "English", "English"),
    URDU("ur", "Urdu", "اردو");

    companion object {
        fun fromCode(code: String?): AppLanguage =
            entries.firstOrNull { it.code == code } ?: ENGLISH
    }
}

enum class AppThemeColor(
    val id: String,
    val label: String,
    val previewColorHex: Long
) {
    EMERALD("emerald", "Emerald", 0xFF0E5A43),
    TEAL("teal", "Madinah Teal", 0xFF0E5E6F),
    INDIGO("indigo", "Tahajjud Indigo", 0xFF2E3B8E),
    AMBER("amber", "Desert Amber", 0xFF8E5A14),
    ROSEWOOD("rosewood", "Rosewood", 0xFF7B2D43);

    companion object {
        fun fromId(id: String?): AppThemeColor =
            entries.firstOrNull { it.id == id } ?: EMERALD
    }
}

enum class AppThemeMode(val id: String, val label: String) {
    SYSTEM("system", "Auto"),
    LIGHT("light", "Light"),
    DARK("dark", "Dark");

    companion object {
        fun fromId(id: String?): AppThemeMode =
            entries.firstOrNull { it.id == id } ?: SYSTEM
    }
}

data class UserAppearancePrefs(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeColor: AppThemeColor = AppThemeColor.EMERALD,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM
)

data class DailyZikrState(
    val count: Int = 0,
    val dateKey: String = "",
    val selectedIndex: Int = 0
)

class AppPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val THEME_COLOR = stringPreferencesKey("app_theme_color")
        val THEME_MODE = stringPreferencesKey("app_theme_mode")
        val DAILY_ZIKR_COUNT = intPreferencesKey("daily_zikr_count")
        val DAILY_ZIKR_DATE = stringPreferencesKey("daily_zikr_date")
        val DAILY_ZIKR_INDEX = intPreferencesKey("daily_zikr_index")
    }

    val appearancePrefs: Flow<UserAppearancePrefs> = context.applicationContext.appearanceDataStore.data
        .map { prefs ->
            UserAppearancePrefs(
                language = AppLanguage.fromCode(prefs[Keys.LANGUAGE]),
                themeColor = AppThemeColor.fromId(prefs[Keys.THEME_COLOR]),
                themeMode = AppThemeMode.fromId(prefs[Keys.THEME_MODE])
            )
        }

    val dailyZikrState: Flow<DailyZikrState> = context.applicationContext.appearanceDataStore.data
        .map { prefs ->
            DailyZikrState(
                count = prefs[Keys.DAILY_ZIKR_COUNT] ?: 0,
                dateKey = prefs[Keys.DAILY_ZIKR_DATE] ?: "",
                selectedIndex = prefs[Keys.DAILY_ZIKR_INDEX] ?: 0
            )
        }

    suspend fun setLanguage(language: AppLanguage) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.code
        }
    }

    suspend fun setThemeColor(themeColor: AppThemeColor) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            prefs[Keys.THEME_COLOR] = themeColor.id
        }
    }

    suspend fun setThemeMode(themeMode: AppThemeMode) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = themeMode.id
        }
    }

    suspend fun incrementDailyZikr(todayKey: String) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            val savedDate = prefs[Keys.DAILY_ZIKR_DATE] ?: ""
            val currentCount = if (savedDate == todayKey) {
                prefs[Keys.DAILY_ZIKR_COUNT] ?: 0
            } else {
                0
            }
            prefs[Keys.DAILY_ZIKR_DATE] = todayKey
            prefs[Keys.DAILY_ZIKR_COUNT] = currentCount + 1
        }
    }

    suspend fun selectDailyZikr(index: Int, todayKey: String) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            val savedDate = prefs[Keys.DAILY_ZIKR_DATE] ?: ""
            if (savedDate != todayKey) {
                prefs[Keys.DAILY_ZIKR_DATE] = todayKey
                prefs[Keys.DAILY_ZIKR_COUNT] = 0
            }
            prefs[Keys.DAILY_ZIKR_INDEX] = index
        }
    }

    suspend fun resetDailyZikr(todayKey: String) {
        context.applicationContext.appearanceDataStore.edit { prefs ->
            prefs[Keys.DAILY_ZIKR_DATE] = todayKey
            prefs[Keys.DAILY_ZIKR_COUNT] = 0
        }
    }
}
