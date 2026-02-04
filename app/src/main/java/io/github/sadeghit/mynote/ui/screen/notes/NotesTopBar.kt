package io.github.sadeghit.mynote.ui.screen.notes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onDeleteAll: () -> Unit
) {
    CenterAlignedTopAppBar(

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,  // ← این درست‌ترین حالت است
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Text(
                "یادداشت‌ها",
                fontWeight = FontWeight.Bold,
            )
        },

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
