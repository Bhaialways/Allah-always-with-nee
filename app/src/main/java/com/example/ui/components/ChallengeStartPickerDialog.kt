package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.UserChallengeProfile
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChallengeStartPickerDialog(
    profile: UserChallengeProfile,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (
        displayName: String,
        niyyahTitle: String,
        startMillis: Long,
        targetDays: Long
    ) -> Unit
) {
    var displayName by remember(profile.displayName) { mutableStateOf(profile.displayName) }
    var niyyahTitle by remember(profile.niyyahTitle) { mutableStateOf(profile.niyyahTitle) }
    var selectedStartMillis by remember(profile.challengeStartMillis) {
        mutableLongStateOf(
            if (profile.challengeStartMillis > 0L) profile.challengeStartMillis
            else System.currentTimeMillis()
        )
    }
    var targetDaysText by remember(profile.targetDays) {
        mutableStateOf(profile.targetDays.coerceAtLeast(1L).toString())
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }
    val timeFormatter = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }

    if (showDatePicker) {
        val localCal = remember(selectedStartMillis) {
            Calendar.getInstance().apply { timeInMillis = selectedStartMillis }
        }
        val initialUtcMillis = remember(localCal) {
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                clear()
                set(
                    localCal.get(Calendar.YEAR),
                    localCal.get(Calendar.MONTH),
                    localCal.get(Calendar.DAY_OF_MONTH)
                )
            }.timeInMillis
        }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialUtcMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis() + 86_400_000L
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val utcMillis = datePickerState.selectedDateMillis
                        if (utcMillis != null) {
                            val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = utcMillis
                            }
                            val updatedLocal = Calendar.getInstance().apply {
                                timeInMillis = selectedStartMillis
                                set(Calendar.YEAR, utcCal.get(Calendar.YEAR))
                                set(Calendar.MONTH, utcCal.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_MONTH, utcCal.get(Calendar.DAY_OF_MONTH))
                            }
                            val candidate = updatedLocal.timeInMillis
                            selectedStartMillis = minOf(candidate, System.currentTimeMillis())
                            validationError = null
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Select Date")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val localCal = remember(selectedStartMillis) {
            Calendar.getInstance().apply { timeInMillis = selectedStartMillis }
        }
        val timePickerState = rememberTimePickerState(
            initialHour = localCal.get(Calendar.HOUR_OF_DAY),
            initialMinute = localCal.get(Calendar.MINUTE),
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Challenge Start Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updatedLocal = Calendar.getInstance().apply {
                            timeInMillis = selectedStartMillis
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val candidate = updatedLocal.timeInMillis
                        if (candidate > System.currentTimeMillis()) {
                            validationError = "Start time cannot be in the future; adjusted to now."
                            selectedStartMillis = System.currentTimeMillis()
                        } else {
                            selectedStartMillis = candidate
                            validationError = null
                        }
                        showTimePicker = false
                    }
                ) {
                    Text("Select Time")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Set Challenge Start & Niyyah",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Choose the exact date and time your clean challenge began. Your live Sober Step timer counts from this moment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pick_start_date_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Choose start date",
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = dateFormatter.format(Date(selectedStartMillis)),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pick_start_time_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            contentDescription = "Choose start time",
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = timeFormatter.format(Date(selectedStartMillis)),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }

                TextButton(
                    onClick = {
                        selectedStartMillis = System.currentTimeMillis()
                        validationError = null
                    },
                    modifier = Modifier.testTag("set_start_now_button")
                ) {
                    Text("Set Start Time to Right Now")
                }

                OutlinedTextField(
                    value = niyyahTitle,
                    onValueChange = { if (it.length <= 100) niyyahTitle = it },
                    label = { Text("Your Niyyah (Intention)") },
                    placeholder = { Text("Clean Heart & Peace with Allah") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("niyyah_input")
                )

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { if (it.length <= 60) displayName = it },
                    label = { Text("Your Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("display_name_input")
                )

                Text(
                    text = "Challenge Goal (Days)",
                    style = MaterialTheme.typography.labelLarge
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(7L, 21L, 30L, 40L, 90L).forEach { preset ->
                        FilterChip(
                            selected = targetDaysText == preset.toString(),
                            onClick = { targetDaysText = preset.toString() },
                            label = { Text("$preset d") }
                        )
                    }
                }

                OutlinedTextField(
                    value = targetDaysText,
                    onValueChange = { raw ->
                        targetDaysText = raw.filter { it.isDigit() }.take(4)
                    },
                    label = { Text("Custom Target Days (1 - 3650)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("target_days_input")
                )

                if (validationError != null) {
                    Text(
                        text = validationError ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedDays = targetDaysText.toLongOrNull() ?: 40L
                    if (parsedDays !in 1L..3650L) {
                        validationError = "Target days must be between 1 and 3650."
                        return@Button
                    }
                    onSave(
                        displayName.trim().ifBlank { "Believer" },
                        niyyahTitle.trim().ifBlank { "Clean Heart & Peace with Allah" },
                        selectedStartMillis.coerceAtMost(System.currentTimeMillis()),
                        parsedDays
                    )
                },
                enabled = !isSaving,
                modifier = Modifier.testTag("save_challenge_config_button")
            ) {
                Text(if (isSaving) "Saving..." else "Save Start Time")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SlipResetDialog(
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onConfirmReset: (triggerNote: String) -> Unit
) {
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Rise Again With Tawbah",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "A strike does not define your worth or erase Allah's mercy. Say 'Astaghfirullah', make fresh Wudu, and restart your clean clock right now.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { if (it.length <= 300) note = it },
                    label = { Text("What triggered this moment? (Optional)") },
                    placeholder = { Text("e.g., Late night loneliness, stress...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("slip_note_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmReset(note) },
                enabled = !isSaving,
                modifier = Modifier.testTag("confirm_slip_reset_button")
            ) {
                Text(if (isSaving) "Restarting..." else "Restart Clean Clock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Keep My Streak")
            }
        }
    )
}
