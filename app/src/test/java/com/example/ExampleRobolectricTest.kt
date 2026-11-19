package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.content.AppLocalization
import com.example.content.RecoveryCurriculum
import com.example.data.RecoveryRepository
import com.example.data.local.RecoveryDatabase
import com.example.prefs.AppLanguage
import com.example.prefs.AppThemeColor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Allah always with me", appName)
    }

    @Test
    fun `recovery curriculum has all 11 episodes and urge relief steps`() {
        assertEquals(11, RecoveryCurriculum.episodes.size)
        assertEquals((1..11).toList(), RecoveryCurriculum.episodes.map { it.number })
        assertTrue(RecoveryCurriculum.urgeReliefSteps.isNotEmpty())
        assertTrue(RecoveryCurriculum.motivationCards.isNotEmpty())
    }

    @Test
    fun `localization has Ayat with Tarjama, 2 languages (English and Urdu), 5 colors, and creator credit`() {
        assertTrue(AppLocalization.helpAyatList.size >= 7)
        AppLocalization.helpAyatList.forEach { ayat ->
            assertTrue(ayat.arabic.isNotBlank())
            assertTrue(ayat.englishTarjama.isNotBlank())
            assertTrue(ayat.urduTarjama.isNotBlank())
        }
        assertEquals(listOf(AppLanguage.ENGLISH, AppLanguage.URDU), AppLanguage.entries)
        assertEquals(5, AppThemeColor.entries.size)
        AppLanguage.entries.forEach { lang ->
            val copy = AppLocalization.getUiCopy(lang)
            assertTrue(copy.aboutCreatorBody.contains("Muhammad Waqas") || copy.aboutCreatorBody.contains("محمد وقاص"))
            assertTrue(copy.timerMotivations.isNotEmpty())
            assertTrue(copy.urgeTimerMotivation.isNotBlank())
            assertTrue(AppLocalization.formatDayAndDate(1763510400000L, lang).isNotBlank())
        }
        assertTrue(AppLocalization.dailyZikrList.size >= 6)
        AppLocalization.dailyZikrList.forEach { zikr ->
            assertTrue(zikr.arabic.isNotBlank())
            assertTrue(zikr.transliteration.isNotBlank())
            assertTrue(zikr.meaningEn.isNotBlank())
            assertTrue(zikr.meaningUr.isNotBlank())
        }
    }

    @Test
    fun `offline Room repository persists challenge profile, urge victories, and reflections without login`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, RecoveryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        try {
            val repo = RecoveryRepository(db.recoveryDao())
            assertTrue(repo.ensureInitialProfile("Believer").isSuccess)

            val initialProfile = repo.observeProfile().first()
            assertNotNull(initialProfile)
            assertEquals("Believer", initialProfile?.displayName)

            assertTrue(
                repo.recordUrgeVictory(
                    newUrgesOvercomeCount = 1L,
                    longestStreakDays = 2L,
                    triggerNote = "Late night temptation defeated with Dhikr",
                    intensity = 4L,
                    dhikrCount = 33L
                ).isSuccess
            )
            assertTrue(
                repo.saveEpisodeReflection(
                    episodeNumber = 1,
                    reflection = "Allah is always with me",
                    completed = true
                ).isSuccess
            )

            val updatedProfile = repo.observeProfile().first()
            assertEquals(1L, updatedProfile?.urgesOvercomeCount)
            assertEquals(listOf(1L), updatedProfile?.completedEpisodes)

            val logs = repo.observeUrgeLogs().first()
            assertEquals(1, logs.size)
            assertEquals("Late night temptation defeated with Dhikr", logs.first().triggerNote)
        } finally {
            db.close()
        }
    }
}
