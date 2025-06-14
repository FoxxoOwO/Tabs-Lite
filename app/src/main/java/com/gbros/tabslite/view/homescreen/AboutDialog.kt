package com.gbros.tabslite.view.homescreen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gbros.tabslite.R
import com.gbros.tabslite.ui.theme.AppTheme

@Composable
fun AboutDialog(modifier: Modifier = Modifier, onDismissRequest: () -> Unit, onExportPlaylistsClicked: () -> Unit, onImportPlaylistsClicked: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    var followSystemThemePref by remember {
        mutableStateOf(sharedPreferences.getBoolean("follow_system_theme", false))
    }
    var useDarkThemePref by remember {
        mutableStateOf(sharedPreferences.getBoolean("use_dark_theme", false))
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(modifier = Modifier.padding(all = 4.dp), onClick = onDismissRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(id = R.string.generic_action_close))
                    }
                }
                Row(
                    modifier = Modifier
                        .matchParentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleLarge)
                }
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                shape = MaterialTheme.shapes.extraLarge.copy(bottomStart = MaterialTheme.shapes.extraSmall.bottomStart, bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd)
            ) {
                Text(modifier = Modifier.padding(all = 16.dp), text = stringResource(id = R.string.app_about))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer),
                shape = MaterialTheme.shapes.extraLarge.copy(topStart = MaterialTheme.shapes.extraSmall.topStart, topEnd = MaterialTheme.shapes.extraSmall.topEnd)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                        .clickable { onImportPlaylistsClicked() }
                ) {
                    Icon(modifier = Modifier.padding(all = 8.dp), imageVector = ImageVector.vectorResource(id = R.drawable.ic_download), contentDescription = "")
                    Text(modifier = Modifier.padding(all = 8.dp), text = stringResource(id = R.string.app_action_import_playlists))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                        .clickable { onExportPlaylistsClicked() }
                ) {
                    Icon(modifier = Modifier.padding(all = 8.dp), imageVector = ImageVector.vectorResource(id = R.drawable.ic_upload), contentDescription = "")
                    Text(modifier = Modifier.padding(all = 8.dp), text = stringResource(id = R.string.app_action_export_playlists))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(modifier = Modifier.padding(all = 8.dp).weight(1f), text = stringResource(id = R.string.app_action_follow_system_theme))
                    Switch(
                        checked = followSystemThemePref,
                        onCheckedChange = {
                            followSystemThemePref = it
                            sharedPreferences.edit()
                                .putBoolean("follow_system_theme",it)
                                .apply()
                        }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(modifier = Modifier.padding(all = 8.dp).weight(1f), text = stringResource(id = R.string.app_action_switch_theme))
                    Switch(
                        checked = useDarkThemePref,
                        onCheckedChange = {
                            // Only change theme if not following system
                            if (!followSystemThemePref) {
                                useDarkThemePref = it
                                sharedPreferences.edit()
                                    .putBoolean("use_dark_theme",it)
                                    .apply()

                            }
                        },
                        enabled = !followSystemThemePref // Disable switch if following system theme
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val uriHandler = LocalUriHandler.current
                TextButton(onClick = { uriHandler.openUri("https://play.google.com/store/apps/details?id=com.gbros.tabslite") }) {
                    Text(text = stringResource(id = R.string.app_action_leave_review))
                }
                TextButton(onClick = { uriHandler.openUri("https://github.com/sponsors/More-Than-Solitaire") }) {
                    Text(text = stringResource(id = R.string.app_action_donate))
                }
            }
        }
    }
}

@Composable @Preview
private fun AboutDialogPreview() {
    AppTheme {
        AboutDialog(Modifier, {}, {}) {}
    }
}