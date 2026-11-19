package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.content.AppLocalization
import com.example.content.AyatWithTarjama
import com.example.content.contextFor
import com.example.content.tarjamaFor
import com.example.prefs.AppLanguage
import com.example.prefs.AppThemeColor
import com.example.prefs.AppThemeMode
import com.example.prefs.UserAppearancePrefs

@Composable
fun AyatHelpCard(
    ayat: AyatWithTarjama,
    language: AppLanguage,
    onNextAyah: () -> Unit,
    modifier: Modifier = Modifier
) {
    val copy = AppLocalization.getUiCopy(language)
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = copy.ayatSectionTitle,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = copy.ayatSectionTitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextButton(
                    onClick = onNextAyah,
                    modifier = Modifier.testTag("next_ayah_button")
                ) {
                    Text(copy.nextAyah)
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = ayat.arabic,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 22.sp,
                            lineHeight = 34.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${copy.tarjamaLabel} ${ayat.tarjamaFor(language)}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = ayat.reference,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Text(
                text = ayat.contextFor(language),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppearanceControlsContent(
    prefs: UserAppearancePrefs,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit
) {
    val copy = AppLocalization.getUiCopy(prefs.language)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // 1. Language Picker
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Translate,
                    contentDescription = copy.languageLabel,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = copy.languageLabel,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppLanguage.entries.forEach { lang ->
                    FilterChip(
                        selected = prefs.language == lang,
                        onClick = { onSelectLanguage(lang) },
                        label = { Text(lang.nativeLabel) },
                        modifier = Modifier.testTag("lang_chip_${lang.code}")
                    )
                }
            }
        }

        // 2. Custom Color Swatches
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Palette,
                    contentDescription = copy.customColorLabel,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = copy.customColorLabel,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppThemeColor.entries.forEach { colorOption ->
                    val selected = prefs.themeColor == colorOption
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectThemeColor(colorOption) }
                            .padding(4.dp)
                            .testTag("color_swatch_${colorOption.id}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(colorOption.previewColorHex))
                                .then(
                                    if (selected) {
                                        Modifier.border(
                                            width = 3.dp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            shape = CircleShape
                                        )
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "${colorOption.label} selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = colorOption.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 3. Light / Dark / Auto Mode
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = copy.themeModeLabel,
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppThemeMode.entries.forEach { mode ->
                    FilterChip(
                        selected = prefs.themeMode == mode,
                        onClick = { onSelectThemeMode(mode) },
                        label = { Text(mode.label) },
                        modifier = Modifier.testTag("theme_mode_${mode.id}")
                    )
                }
            }
        }
    }
}

@Composable
fun AppearanceSettingsDialog(
    prefs: UserAppearancePrefs,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectThemeColor: (AppThemeColor) -> Unit,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val copy = AppLocalization.getUiCopy(prefs.language)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = copy.appearanceCardTitle,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            AppearanceControlsContent(
                prefs = prefs,
                onSelectLanguage = onSelectLanguage,
                onSelectThemeColor = onSelectThemeColor,
                onSelectThemeMode = onSelectThemeMode
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_appearance_dialog")
            ) {
                Text("Done")
            }
        }
    )
}
