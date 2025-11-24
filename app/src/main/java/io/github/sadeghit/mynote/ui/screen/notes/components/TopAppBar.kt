package io.github.sadeghit.mynote.ui.screen.notes.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onDeleteAll: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Notes") },
        actions = {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "تغییر تم"
                )
            }
            IconButton(onClick = onDeleteAll) {
                Icon(Icons.Default.DeleteSweep, contentDescription = "حذف همه")
            }
        }
    )
}