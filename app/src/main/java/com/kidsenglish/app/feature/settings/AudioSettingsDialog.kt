package com.kidsenglish.app.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kidsenglish.app.R
import com.kidsenglish.app.core.audio.AudioEffect
import com.kidsenglish.app.core.audio.AudioManager
import com.kidsenglish.app.core.designsystem.color.AppColors
import com.kidsenglish.app.core.designsystem.dimens.Dimens

@Composable
fun AudioSettingsDialog(
    audioManager: AudioManager,
    onDismiss: () -> Unit,
    onResetProgress: () -> Unit
) {
    val settings by audioManager.settings.collectAsState()
    var showConfirmReset by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Dimens.RadiusL),
            color = Color.White,
            shadowElevation = Dimens.ElevationHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpaceL)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.action_close),
                            tint = AppColors.TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceM))

                // Music Toggle
                SettingToggleRow(
                    icon = Icons.Default.MusicNote,
                    title = stringResource(R.string.settings_music_label),
                    isChecked = settings.isMusicEnabled,
                    onCheckedChange = {
                        audioManager.playSfx(AudioEffect.CLICK)
                        audioManager.updateSettings(settings.copy(isMusicEnabled = it))
                    }
                )

                // Sound Effects Toggle
                SettingToggleRow(
                    icon = Icons.Default.VolumeUp,
                    title = stringResource(R.string.settings_sfx_label),
                    isChecked = settings.isSfxEnabled,
                    onCheckedChange = {
                        audioManager.updateSettings(settings.copy(isSfxEnabled = it))
                        if (it) audioManager.playSfx(AudioEffect.STAR)
                    }
                )

                // Voice Pronunciation Toggle
                SettingToggleRow(
                    icon = Icons.Default.RecordVoiceOver,
                    title = stringResource(R.string.settings_voice_label),
                    isChecked = settings.isVoiceEnabled,
                    onCheckedChange = {
                        audioManager.playSfx(AudioEffect.CLICK)
                        audioManager.updateSettings(settings.copy(isVoiceEnabled = it))
                    }
                )

                // Slow Voice Toggle for Kids
                SettingToggleRow(
                    icon = Icons.Default.RecordVoiceOver,
                    title = stringResource(R.string.settings_voice_speed),
                    isChecked = settings.isSlowVoice,
                    onCheckedChange = {
                        audioManager.playSfx(AudioEffect.CLICK)
                        audioManager.updateSettings(settings.copy(isSlowVoice = it))
                    }
                )

                Spacer(modifier = Modifier.height(Dimens.SpaceM))

                // Music Volume Slider
                if (settings.isMusicEnabled) {
                    Text(
                        text = stringResource(R.string.settings_music_volume),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.TextSecondary
                    )
                    Slider(
                        value = settings.musicVolume,
                        onValueChange = {
                            audioManager.updateSettings(settings.copy(musicVolume = it))
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = AppColors.PrimarySky,
                            activeTrackColor = AppColors.PrimarySky
                        )
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.SpaceS))

                // Reset Progress Action
                if (!showConfirmReset) {
                    Button(
                        onClick = { showConfirmReset = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(Dimens.RadiusM),
                        modifier = Modifier.fillMaxWidth().testTag("reset_progress_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = AppColors.ErrorDeep,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpaceS))
                        Text(
                            text = stringResource(R.string.settings_reset_progress),
                            color = AppColors.ErrorDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(Dimens.RadiusM))
                            .padding(Dimens.SpaceM)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_reset_confirm),
                            fontSize = 13.sp,
                            color = AppColors.ErrorDeep,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpaceS))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SpaceS)
                        ) {
                            Button(
                                onClick = {
                                    audioManager.playSfx(AudioEffect.CLICK)
                                    onResetProgress()
                                    showConfirmReset = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.ErrorDeep),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.action_reset), color = Color.White)
                            }
                            Button(
                                onClick = { showConfirmReset = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.action_close), color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpaceXS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppColors.PrimaryNavy,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(Dimens.SpaceS))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.TextPrimary
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AppColors.PrimarySky
            )
        )
    }
}
