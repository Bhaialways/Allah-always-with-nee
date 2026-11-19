package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.prefs.AppThemeColor
import com.example.prefs.AppThemeMode

private fun lightSchemeFor(themeColor: AppThemeColor): ColorScheme = when (themeColor) {
    AppThemeColor.EMERALD -> lightColorScheme(
        primary = EmeraldPrimary,
        onPrimary = EmeraldOnPrimary,
        primaryContainer = EmeraldPrimaryContainer,
        onPrimaryContainer = EmeraldOnPrimaryContainer,
        secondary = AmberSecondary,
        onSecondary = AmberOnSecondary,
        secondaryContainer = AmberSecondaryContainer,
        onSecondaryContainer = AmberOnSecondaryContainer,
        tertiary = TealTertiary,
        onTertiary = TealOnTertiary,
        tertiaryContainer = TealTertiaryContainer,
        onTertiaryContainer = TealOnTertiaryContainer,
        background = CreamBackground,
        onBackground = CreamOnBackground,
        surface = CreamSurface,
        onSurface = CreamOnBackground,
        surfaceVariant = CreamSurfaceVariant,
        onSurfaceVariant = CreamOnSurfaceVariant,
        outline = WarmOutline,
        error = RescueCoral,
        onError = Color.White,
        errorContainer = RescueCoralContainer,
        onErrorContainer = RescueOnCoralContainer
    )

    AppThemeColor.TEAL -> lightColorScheme(
        primary = Color(0xFF0E5E6F),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFC5ECF5),
        onPrimaryContainer = Color(0xFF001F27),
        secondary = Color(0xFF8C6321),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFE1B8),
        onSecondaryContainer = Color(0xFF2D1A00),
        tertiary = Color(0xFF2F6B55),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFC9EEDD),
        onTertiaryContainer = Color(0xFF062116),
        background = Color(0xFFF5F9FA),
        onBackground = Color(0xFF171D1F),
        surface = Color(0xFFFCFEFF),
        onSurface = Color(0xFF171D1F),
        surfaceVariant = Color(0xFFDEE7EA),
        onSurfaceVariant = Color(0xFF40484B),
        outline = Color(0xFF70787C),
        error = RescueCoral,
        onError = Color.White,
        errorContainer = RescueCoralContainer,
        onErrorContainer = RescueOnCoralContainer
    )

    AppThemeColor.INDIGO -> lightColorScheme(
        primary = Color(0xFF2E3B8E),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDBE0FF),
        onPrimaryContainer = Color(0xFF071357),
        secondary = Color(0xFF936120),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFDFB8),
        onSecondaryContainer = Color(0xFF301B00),
        tertiary = Color(0xFF1D665C),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFC0ECE3),
        onTertiaryContainer = Color(0xFF00201C),
        background = Color(0xFFF6F7FC),
        onBackground = Color(0xFF191B24),
        surface = Color(0xFFFDFDFF),
        onSurface = Color(0xFF191B24),
        surfaceVariant = Color(0xFFE1E3F0),
        onSurfaceVariant = Color(0xFF444757),
        outline = Color(0xFF747788),
        error = RescueCoral,
        onError = Color.White,
        errorContainer = RescueCoralContainer,
        onErrorContainer = RescueOnCoralContainer
    )

    AppThemeColor.AMBER -> lightColorScheme(
        primary = Color(0xFF8E5A14),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFE0BA),
        onPrimaryContainer = Color(0xFF2E1800),
        secondary = Color(0xFF1A634C),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFC5EFE0),
        onSecondaryContainer = Color(0xFF022117),
        tertiary = Color(0xFF5D5F2B),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFE3E6A3),
        onTertiaryContainer = Color(0xFF1B1D00),
        background = Color(0xFFFAF7F1),
        onBackground = Color(0xFF1E1B16),
        surface = Color(0xFFFFFCF7),
        onSurface = Color(0xFF1E1B16),
        surfaceVariant = Color(0xFFEDE2D3),
        onSurfaceVariant = Color(0xFF4D453B),
        outline = Color(0xFF7E7569),
        error = RescueCoral,
        onError = Color.White,
        errorContainer = RescueCoralContainer,
        onErrorContainer = RescueOnCoralContainer
    )

    AppThemeColor.ROSEWOOD -> lightColorScheme(
        primary = Color(0xFF7B2D43),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFD9E1),
        onPrimaryContainer = Color(0xFF340615),
        secondary = Color(0xFF8A6121),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFDFB3),
        onSecondaryContainer = Color(0xFF2D1900),
        tertiary = Color(0xFF27645C),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFC1EDE5),
        onTertiaryContainer = Color(0xFF01201C),
        background = Color(0xFFFAF5F6),
        onBackground = Color(0xFF201A1B),
        surface = Color(0xFFFFFBFC),
        onSurface = Color(0xFF201A1B),
        surfaceVariant = Color(0xFFEDE0E3),
        onSurfaceVariant = Color(0xFF4F4346),
        outline = Color(0xFF817376),
        error = RescueCoral,
        onError = Color.White,
        errorContainer = RescueCoralContainer,
        onErrorContainer = RescueOnCoralContainer
    )
}

private fun darkSchemeFor(themeColor: AppThemeColor): ColorScheme = when (themeColor) {
    AppThemeColor.EMERALD -> darkColorScheme(
        primary = DarkEmeraldPrimary,
        onPrimary = DarkEmeraldOnPrimary,
        primaryContainer = DarkEmeraldPrimaryContainer,
        onPrimaryContainer = DarkEmeraldOnPrimaryContainer,
        secondary = DarkAmberSecondary,
        onSecondary = DarkAmberOnSecondary,
        secondaryContainer = DarkAmberSecondaryContainer,
        onSecondaryContainer = DarkAmberOnSecondaryContainer,
        tertiary = DarkTealTertiary,
        onTertiary = DarkTealOnTertiary,
        tertiaryContainer = DarkTealTertiaryContainer,
        onTertiaryContainer = DarkTealOnTertiaryContainer,
        background = DarkSanctuaryBackground,
        onBackground = DarkSanctuaryOnSurface,
        surface = DarkSanctuarySurface,
        onSurface = DarkSanctuaryOnSurface,
        surfaceVariant = DarkSanctuarySurfaceVariant,
        onSurfaceVariant = DarkSanctuaryOnSurfaceVariant,
        outline = DarkSanctuaryOutline,
        error = RescueCoralContainer,
        onError = RescueOnCoralContainer,
        errorContainer = RescueCoral,
        onErrorContainer = RescueCoralContainer
    )

    AppThemeColor.TEAL -> darkColorScheme(
        primary = Color(0xFF83D5E6),
        onPrimary = Color(0xFF003641),
        primaryContainer = Color(0xFF004E5D),
        onPrimaryContainer = Color(0xFFC5ECF5),
        secondary = DarkAmberSecondary,
        onSecondary = DarkAmberOnSecondary,
        secondaryContainer = DarkAmberSecondaryContainer,
        onSecondaryContainer = DarkAmberOnSecondaryContainer,
        tertiary = Color(0xFF93D4B8),
        onTertiary = Color(0xFF003828),
        tertiaryContainer = Color(0xFF14523E),
        onTertiaryContainer = Color(0xFFC9EEDD),
        background = Color(0xFF0B1417),
        onBackground = Color(0xFFE1E9EC),
        surface = Color(0xFF111D21),
        onSurface = Color(0xFFE1E9EC),
        surfaceVariant = Color(0xFF233338),
        onSurfaceVariant = Color(0xFFB7C5CA),
        outline = Color(0xFF819095),
        error = RescueCoralContainer,
        onError = RescueOnCoralContainer,
        errorContainer = RescueCoral,
        onErrorContainer = RescueCoralContainer
    )

    AppThemeColor.INDIGO -> darkColorScheme(
        primary = Color(0xFFB5C2FF),
        onPrimary = Color(0xFF122070),
        primaryContainer = Color(0xFF253281),
        onPrimaryContainer = Color(0xFFDBE0FF),
        secondary = DarkAmberSecondary,
        onSecondary = DarkAmberOnSecondary,
        secondaryContainer = DarkAmberSecondaryContainer,
        onSecondaryContainer = DarkAmberOnSecondaryContainer,
        tertiary = Color(0xFF8CD5CA),
        onTertiary = Color(0xFF003731),
        tertiaryContainer = Color(0xFF0A4F47),
        onTertiaryContainer = Color(0xFFC0ECE3),
        background = Color(0xFF0E111A),
        onBackground = Color(0xFFE3E5F0),
        surface = Color(0xFF151926),
        onSurface = Color(0xFFE3E5F0),
        surfaceVariant = Color(0xFF272D3F),
        onSurfaceVariant = Color(0xFFBDC2D6),
        outline = Color(0xFF878CA1),
        error = RescueCoralContainer,
        onError = RescueOnCoralContainer,
        errorContainer = RescueCoral,
        onErrorContainer = RescueCoralContainer
    )

    AppThemeColor.AMBER -> darkColorScheme(
        primary = Color(0xFFF5C17B),
        onPrimary = Color(0xFF472A00),
        primaryContainer = Color(0xFF694104),
        onPrimaryContainer = Color(0xFFFFE0BA),
        secondary = Color(0xFF8CD4B8),
        onSecondary = Color(0xFF003828),
        secondaryContainer = Color(0xFF0E513B),
        onSecondaryContainer = Color(0xFFC5EFE0),
        tertiary = Color(0xFFC7C989),
        onTertiary = Color(0xFF303204),
        tertiaryContainer = Color(0xFF464818),
        onTertiaryContainer = Color(0xFFE3E6A3),
        background = Color(0xFF16120D),
        onBackground = Color(0xFFECE4DA),
        surface = Color(0xFF1F1A13),
        onSurface = Color(0xFFECE4DA),
        surfaceVariant = Color(0xFF362E24),
        onSurfaceVariant = Color(0xFFCCC0B2),
        outline = Color(0xFF968B7E),
        error = RescueCoralContainer,
        onError = RescueOnCoralContainer,
        errorContainer = RescueCoral,
        onErrorContainer = RescueCoralContainer
    )

    AppThemeColor.ROSEWOOD -> darkColorScheme(
        primary = Color(0xFFF5B1C3),
        onPrimary = Color(0xFF4F1126),
        primaryContainer = Color(0xFF6A2138),
        onPrimaryContainer = Color(0xFFFFD9E1),
        secondary = DarkAmberSecondary,
        onSecondary = DarkAmberOnSecondary,
        secondaryContainer = DarkAmberSecondaryContainer,
        onSecondaryContainer = DarkAmberOnSecondaryContainer,
        tertiary = Color(0xFF8CD5CA),
        onTertiary = Color(0xFF003731),
        tertiaryContainer = Color(0xFF0D4E47),
        onTertiaryContainer = Color(0xFFC1EDE5),
        background = Color(0xFF170F12),
        onBackground = Color(0xFFEDE1E4),
        surface = Color(0xFF21161A),
        onSurface = Color(0xFFEDE1E4),
        surfaceVariant = Color(0xFF38272D),
        onSurfaceVariant = Color(0xFFCDBDC2),
        outline = Color(0xFF97888D),
        error = RescueCoralContainer,
        onError = RescueOnCoralContainer,
        errorContainer = RescueCoral,
        onErrorContainer = RescueCoralContainer
    )
}

val SanctuaryShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun AllahAlwaysWithMeTheme(
    themeColor: AppThemeColor = AppThemeColor.EMERALD,
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }
    val colorScheme = if (useDark) darkSchemeFor(themeColor) else lightSchemeFor(themeColor)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = SanctuaryShapes,
        content = content
    )
}
