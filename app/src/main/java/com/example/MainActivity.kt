package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.content.AppLocalization
import com.example.prefs.AppPreferencesRepository
import com.example.prefs.DailyZikrState
import com.example.prefs.UserAppearancePrefs
import com.example.ui.AppNavigation
import com.example.ui.theme.AllahAlwaysWithMeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prefsRepo = remember { AppPreferencesRepository(applicationContext) }
            val appearancePrefs by prefsRepo.appearancePrefs.collectAsStateWithLifecycle(
                initialValue = UserAppearancePrefs()
            )
            val dailyZikrState by prefsRepo.dailyZikrState.collectAsStateWithLifecycle(
                initialValue = DailyZikrState()
            )
            val scope = rememberCoroutineScope()

            AllahAlwaysWithMeTheme(
                themeColor = appearancePrefs.themeColor,
                themeMode = appearancePrefs.themeMode
            ) {
                AppNavigation(
                    appearancePrefs = appearancePrefs,
                    dailyZikrState = dailyZikrState,
                    onSelectLanguage = { lang -> scope.launch { prefsRepo.setLanguage(lang) } },
                    onSelectThemeColor = { color -> scope.launch { prefsRepo.setThemeColor(color) } },
                    onSelectThemeMode = { mode -> scope.launch { prefsRepo.setThemeMode(mode) } },
                    onIncrementDailyZikr = {
                        val todayKey = AppLocalization.dateKeyFor(System.currentTimeMillis())
                        scope.launch { prefsRepo.incrementDailyZikr(todayKey) }
                    },
                    onSelectDailyZikr = { index ->
                        val todayKey = AppLocalization.dateKeyFor(System.currentTimeMillis())
                        scope.launch { prefsRepo.selectDailyZikr(index, todayKey) }
                    },
                    onResetDailyZikr = {
                        val todayKey = AppLocalization.dateKeyFor(System.currentTimeMillis())
                        scope.launch { prefsRepo.resetDailyZikr(todayKey) }
                    }
                )
            }
        }
    }
}
