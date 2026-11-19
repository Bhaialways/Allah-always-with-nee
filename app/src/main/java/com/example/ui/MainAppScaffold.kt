package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.R
import com.example.auth.AuthScreen
import com.example.auth.signOut
import com.example.content.AppLocalization
import com.example.data.RecoveryRepository
import com.example.model.EpisodeReflection
import com.example.model.UiState
import com.example.model.UrgeLogEntry
import com.example.model.UrgeOutcome
import com.example.model.UserChallengeProfile
import com.example.prefs.AppLanguage
import com.example.prefs.AppThemeColor
import com.example.prefs.AppThemeMode
import com.example.prefs.DailyZikrState
import com.example.prefs.UserAppearancePrefs
import com.example.ui.screens.EpisodeDetailScreen
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JourneyLogScreen
import com.example.ui.screens.UrgeRescueScreen
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

enum class MainTab(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(Icons.Filled.Home, Icons.Outlined.Home),
    GUIDE(Icons.Filled.AutoStories, Icons.Outlined.AutoStories),
    JOURNEY(Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents);

    fun labelFor(language: AppLanguage): String {
        val copy = AppLocalization.getUiCopy(language)
        return when (this) {
            HOME -> copy.tabHome
            GUIDE -> copy.tabGuide
            JOURNEY -> copy.tabMilestones
        }
    }
}

internal fun FirebaseAuth.authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth ->
        trySend(auth.currentUser)
    }
    addAuthStateListener(listener)
    awaitClose { removeAuthStateListener(listener) }
}

@Composable
fun AppNavigation(
    appearancePrefs: UserAppearancePrefs,
    dailyZikrState: DailyZikrState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onIncrementDailyZikr: () -> Unit,
    onSelectDailyZikr: (Int) -> Unit,
    onResetDailyZikr: () -> Unit,
    auth: FirebaseAuth? = null
) {
    val context = LocalContext.current
    val firebaseAuth = remember(auth, context) {
        auth ?: if (FirebaseApp.getApps(context).isNotEmpty()) Firebase.auth else null
    }

    if (firebaseAuth == null) {
        LocalSanctuaryRoot(
            appearancePrefs = appearancePrefs,
            dailyZikrState = dailyZikrState,
            onSelectLanguage = onSelectLanguage,
            onSelectThemeColor = onSelectThemeColor,
            onSelectThemeMode = onSelectThemeMode,
            onIncrementDailyZikr = onIncrementDailyZikr,
            onSelectDailyZikr = onSelectDailyZikr,
            onResetDailyZikr = onResetDailyZikr
        )
    } else {
        val currentUser by firebaseAuth.authStateFlow().collectAsStateWithLifecycle(
            initialValue = firebaseAuth.currentUser
        )
        val user = currentUser

        if (user == null) {
            AuthScreen(
                language = appearancePrefs.language,
                onSelectLanguage = onSelectLanguage,
                onAuthSuccess = {}
            )
        } else {
            val uid = user.uid
            val displayName = user.displayName?.takeIf { it.isNotBlank() } ?: "Believer"
            key(uid) {
                AuthenticatedRecoveryApp(
                    currentUserId = uid,
                    initialDisplayName = displayName,
                    appearancePrefs = appearancePrefs,
                    dailyZikrState = dailyZikrState,
                    onSelectLanguage = onSelectLanguage,
                    onSelectThemeColor = onSelectThemeColor,
                    onSelectThemeMode = onSelectThemeMode,
                    onIncrementDailyZikr = onIncrementDailyZikr,
                    onSelectDailyZikr = onSelectDailyZikr,
                    onResetDailyZikr = onResetDailyZikr
                )
            }
        }
    }
}

@Composable
private fun LocalSanctuaryRoot(
    appearancePrefs: UserAppearancePrefs,
    dailyZikrState: DailyZikrState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onIncrementDailyZikr: () -> Unit,
    onSelectDailyZikr: (Int) -> Unit,
    onResetDailyZikr: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var nowMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            nowMillis = System.currentTimeMillis()
        }
    }
    var profile by remember {
        mutableStateOf(
            UserChallengeProfile(
                id = "local_user",
                userId = "local_user",
                displayName = "Believer",
                niyyahTitle = "Clean Heart & Peace with Allah",
                challengeStartMillis = System.currentTimeMillis(),
                targetDays = 40L
            )
        )
    }
    var urgeLogs by remember { mutableStateOf<List<UrgeLogEntry>>(emptyList()) }
    var reflections by remember { mutableStateOf<Map<Int, EpisodeReflection>>(emptyMap()) }

    SanctuaryAppContent(
        profile = profile,
        urgeLogs = urgeLogs,
        reflections = reflections,
        currentTimeMillis = nowMillis,
        isSaving = false,
        snackbarHostState = snackbarHostState,
        appearancePrefs = appearancePrefs,
        dailyZikrState = dailyZikrState,
        onSelectLanguage = onSelectLanguage,
        onSelectThemeColor = onSelectThemeColor,
        onSelectThemeMode = onSelectThemeMode,
        onIncrementDailyZikr = onIncrementDailyZikr,
        onSelectDailyZikr = onSelectDailyZikr,
        onResetDailyZikr = onResetDailyZikr,
        onUpdateChallengeConfig = { name, niyyah, start, target, done ->
            profile = profile.copy(
                displayName = name,
                niyyahTitle = niyyah,
                challengeStartMillis = start,
                targetDays = target
            )
            done()
            scope.launch {
                snackbarHostState.showSnackbar("Challenge start time & Niyyah saved. Allah is with you.")
            }
        },
        onRecordUrgeVictory = { note, intensity, dhikrCount, done ->
            val elapsedDays = ((System.currentTimeMillis() - profile.challengeStartMillis).coerceAtLeast(0L)) / 86_400_000L
            profile = profile.copy(
                urgesOvercomeCount = profile.urgesOvercomeCount + 1L,
                longestStreakDays = maxOf(profile.longestStreakDays, elapsedDays)
            )
            urgeLogs = listOf(
                UrgeLogEntry(
                    id = "log_${System.currentTimeMillis()}",
                    userId = "local_user",
                    outcome = UrgeOutcome.OVERCOME.name,
                    triggerNote = note,
                    intensity = intensity.toLong(),
                    dhikrCount = dhikrCount.toLong(),
                    createdAt = Timestamp.now()
                )
            ) + urgeLogs
            done()
            scope.launch {
                snackbarHostState.showSnackbar("Alhamdulillah! You overcame this urge for Allah's sake.")
            }
        },
        onRecordSlipAndReset = { note, done ->
            val now = System.currentTimeMillis()
            val elapsedDays = ((now - profile.challengeStartMillis).coerceAtLeast(0L)) / 86_400_000L
            profile = profile.copy(
                totalResetsCount = profile.totalResetsCount + 1L,
                challengeStartMillis = now,
                longestStreakDays = maxOf(profile.longestStreakDays, elapsedDays)
            )
            urgeLogs = listOf(
                UrgeLogEntry(
                    id = "slip_${now}",
                    userId = "local_user",
                    outcome = UrgeOutcome.SLIP.name,
                    triggerNote = note,
                    intensity = 3L,
                    dhikrCount = 0L,
                    createdAt = Timestamp.now()
                )
            ) + urgeLogs
            done()
            scope.launch {
                snackbarHostState.showSnackbar("New clean start begun. Allah loves those who turn back to Him.")
            }
        },
        onSaveEpisodeReflection = { epNum, text, completed ->
            val updatedSet = profile.completedEpisodes.toMutableSet()
            if (completed) updatedSet.add(epNum.toLong()) else updatedSet.remove(epNum.toLong())
            profile = profile.copy(completedEpisodes = updatedSet.toList())
            reflections = reflections + (
                epNum to EpisodeReflection(
                    id = "ep_$epNum",
                    userId = "local_user",
                    episodeNumber = epNum.toLong(),
                    reflection = text,
                    completed = completed,
                    updatedAt = Timestamp.now()
                )
            )
            scope.launch {
                snackbarHostState.showSnackbar(
                    if (completed) "Episode $epNum completed. May Allah bless your step!"
                    else "Reflection saved for Episode $epNum."
                )
            }
        },
        onSignOutClick = {
            scope.launch {
                snackbarHostState.showSnackbar("Running in offline mode on this build.")
            }
        }
    )
}

@Composable
private fun AuthenticatedRecoveryApp(
    currentUserId: String,
    initialDisplayName: String,
    appearancePrefs: UserAppearancePrefs,
    dailyZikrState: DailyZikrState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onIncrementDailyZikr: () -> Unit,
    onSelectDailyZikr: (Int) -> Unit,
    onResetDailyZikr: () -> Unit
) {
    val viewModel: RecoveryViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                RecoveryViewModel(
                    repository = RecoveryRepository(db),
                    currentUserId = currentUserId,
                    initialDisplayName = initialDisplayName
                )
            }
        }
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val urgeLogsState by viewModel.urgeLogsState.collectAsStateWithLifecycle()
    val reflectionsState by viewModel.reflectionsState.collectAsStateWithLifecycle()
    val currentTimeMillis by viewModel.currentTimeMillis.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val actionMessage by viewModel.actionMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(actionMessage) {
        val msg = actionMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearActionMessage()
        }
    }

    var activeTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    var showUrgeRescue by rememberSaveable { mutableStateOf(false) }
    var selectedEpisodeNumber by rememberSaveable { mutableStateOf<Int?>(null) }

    when (val pState = profileState) {
        is UiState.Loading -> {
            Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Preparing your sanctuary...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Connection Issue",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = pState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { signOut(credentialManager, scope) }
                        ) {
                            Text("Sign Out & Retry")
                        }
                    }
                }
            }
        }

        is UiState.Success -> {
            val profile = pState.data
            val urgeLogs = (urgeLogsState as? UiState.Success)?.data ?: emptyList()
            val reflections = (reflectionsState as? UiState.Success)?.data ?: emptyMap()

            SanctuaryAppContent(
                profile = profile,
                urgeLogs = urgeLogs,
                reflections = reflections,
                currentTimeMillis = currentTimeMillis,
                isSaving = isSaving,
                snackbarHostState = snackbarHostState,
                appearancePrefs = appearancePrefs,
                dailyZikrState = dailyZikrState,
                onSelectLanguage = onSelectLanguage,
                onSelectThemeColor = onSelectThemeColor,
                onSelectThemeMode = onSelectThemeMode,
                onIncrementDailyZikr = onIncrementDailyZikr,
                onSelectDailyZikr = onSelectDailyZikr,
                onResetDailyZikr = onResetDailyZikr,
                onUpdateChallengeConfig = { name, niyyah, start, target, done ->
                    viewModel.updateChallengeConfig(
                        displayName = name,
                        niyyahTitle = niyyah,
                        challengeStartMillis = start,
                        targetDays = target,
                        onComplete = done
                    )
                },
                onRecordUrgeVictory = { note, intensity, dhikrCount, done ->
                    viewModel.recordUrgeVictory(
                        profile = profile,
                        triggerNote = note,
                        intensity = intensity,
                        dhikrCount = dhikrCount,
                        onComplete = done
                    )
                },
                onRecordSlipAndReset = { note, done ->
                    viewModel.recordSlipAndReset(
                        profile = profile,
                        triggerNote = note,
                        onComplete = done
                    )
                },
                onSaveEpisodeReflection = { epNum, text, completed ->
                    viewModel.saveEpisodeReflection(
                        profile = profile,
                        episodeNumber = epNum,
                        reflection = text,
                        completed = completed
                    )
                },
                onSignOutClick = {
                    signOut(credentialManager, scope)
                }
            )
        }
    }
}

@Composable
private fun SanctuaryAppContent(
    profile: UserChallengeProfile,
    urgeLogs: List<UrgeLogEntry>,
    reflections: Map<Int, EpisodeReflection>,
    currentTimeMillis: Long,
    isSaving: Boolean,
    snackbarHostState: SnackbarHostState,
    appearancePrefs: UserAppearancePrefs,
    dailyZikrState: DailyZikrState,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onIncrementDailyZikr: () -> Unit,
    onSelectDailyZikr: (Int) -> Unit,
    onResetDailyZikr: () -> Unit,
    onUpdateChallengeConfig: (String, String, Long, Long, () -> Unit) -> Unit,
    onRecordUrgeVictory: (String, Int, Int, () -> Unit) -> Unit,
    onRecordSlipAndReset: (String, () -> Unit) -> Unit,
    onSaveEpisodeReflection: (Int, String, Boolean) -> Unit,
    onSignOutClick: () -> Unit
) {
    var activeTab by rememberSaveable { mutableStateOf(MainTab.HOME) }
    var showUrgeRescue by rememberSaveable { mutableStateOf(false) }
    var selectedEpisodeNumber by rememberSaveable { mutableStateOf<Int?>(null) }

    if (showUrgeRescue) {
        UrgeRescueScreen(
            language = appearancePrefs.language,
            isSaving = isSaving,
            onBack = { showUrgeRescue = false },
            onVictoryLogged = { note, intensity, dhikrCount ->
                onRecordUrgeVictory(note, intensity, dhikrCount) {
                    showUrgeRescue = false
                }
            }
        )
    } else if (selectedEpisodeNumber != null) {
        val epNum = selectedEpisodeNumber!!
        EpisodeDetailScreen(
            episodeNumber = epNum,
            existingReflection = reflections[epNum],
            isInitiallyCompleted = profile.completedEpisodes.contains(epNum.toLong()),
            isSaving = isSaving,
            onBack = { selectedEpisodeNumber = null },
            onOpenUrgeRescue = {
                selectedEpisodeNumber = null
                showUrgeRescue = true
            },
            onNavigateEpisode = { nextEp -> selectedEpisodeNumber = nextEp },
            onSaveReflection = { text, completed ->
                onSaveEpisodeReflection(epNum, text, completed)
            }
        )
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                floatingActionButton = {
                    if (activeTab != MainTab.HOME) {
                        val fabCopy = AppLocalization.getUiCopy(appearancePrefs.language)
                        ExtendedFloatingActionButton(
                            onClick = { showUrgeRescue = true },
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Shield,
                                    contentDescription = fabCopy.fabUrgeHelp
                                )
                            },
                            text = { Text(fabCopy.fabUrgeHelp) },
                            modifier = Modifier.testTag("fab_urge_help")
                        )
                    }
                },
                bottomBar = {
                    if (!useRail) {
                        NavigationBar {
                            MainTab.entries.forEach { tab ->
                                val selected = activeTab == tab
                                val label = tab.labelFor(appearancePrefs.language)
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { activeTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = label
                                        )
                                    },
                                    label = { Text(label) },
                                    modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useRail) {
                        NavigationRail(modifier = Modifier.fillMaxHeight()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            MainTab.entries.forEach { tab ->
                                val selected = activeTab == tab
                                val label = tab.labelFor(appearancePrefs.language)
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { activeTab = tab },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = label,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = { Text(label) },
                                    modifier = Modifier.testTag("rail_${tab.name.lowercase()}")
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        when (activeTab) {
                            MainTab.HOME -> HomeScreen(
                                profile = profile,
                                currentTimeMillis = currentTimeMillis,
                                isSaving = isSaving,
                                appearancePrefs = appearancePrefs,
                                dailyZikrState = dailyZikrState,
                                onSelectLanguage = onSelectLanguage,
                                onSelectThemeColor = onSelectThemeColor,
                                onSelectThemeMode = onSelectThemeMode,
                                onIncrementDailyZikr = onIncrementDailyZikr,
                                onSelectDailyZikr = onSelectDailyZikr,
                                onResetDailyZikr = onResetDailyZikr,
                                onOpenUrgeRescue = { showUrgeRescue = true },
                                onOpenGuide = { activeTab = MainTab.GUIDE },
                                onUpdateChallengeConfig = onUpdateChallengeConfig,
                                onRecordSlipAndReset = onRecordSlipAndReset
                            )

                            MainTab.GUIDE -> GuideScreen(
                                profile = profile,
                                reflections = reflections,
                                language = appearancePrefs.language,
                                isSaving = isSaving,
                                onToggleEpisodeComplete = { epNum, completed, existingReflection ->
                                    onSaveEpisodeReflection(epNum, existingReflection, completed)
                                },
                                onSelectEpisode = { epNum -> selectedEpisodeNumber = epNum }
                            )

                            MainTab.JOURNEY -> JourneyLogScreen(
                                profile = profile,
                                currentTimeMillis = currentTimeMillis,
                                urgeLogs = urgeLogs,
                                appearancePrefs = appearancePrefs,
                                onSelectLanguage = onSelectLanguage,
                                onSelectThemeColor = onSelectThemeColor,
                                onSelectThemeMode = onSelectThemeMode,
                                onSignOutClick = onSignOutClick
                            )
                        }
                    }
                }
            }
        }
    }
}
