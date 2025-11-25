package io.github.sadeghit.mynote.ui.screen.notes


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesEmptyState
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesList
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesSearchBar
import io.github.sadeghit.mynote.viewModel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    // پراپرتی‌های مربوط به مدیریت حذف
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val showDeleteAllDialog by viewModel.showDeleteAllDialog.collectAsStateWithLifecycle()
    val noteToDelete by viewModel.noteToDelete.collectAsStateWithLifecycle()


    // برای تغییر تم
    val toggleTheme = { viewModel.toggleTheme() }

    // ===============================================
    //               بخش مدیریت دایالوگ‌ها
    // ===============================================

    // --- دایالوگ حذف تکی ---
    if (showDeleteDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteNoteDialog,
            title = {
                Text(
                    "حذف یادداشت", style = typography.titleLarge.copy(
                        textAlign = TextAlign.Right,
                        textDirection = TextDirection.Rtl
                    ), modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "آیا مطمئنید که می‌خواهید  یادداشت‌ را حذف کنید؟",
                    style = typography.bodyMedium.copy(
                        textAlign = TextAlign.Right,
                        textDirection = TextDirection.Rtl
                    ), modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmDeleteNote) {
                    Text("حذف")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDeleteNoteDialog) {
                    Text("لغو")
                }
            }
        )
    }



    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteAllDialog,
            title = {
                Text(
                    "حذف همه یادداشت‌ها", style = typography.titleLarge.copy(
                        textAlign = TextAlign.Right,
                        textDirection = TextDirection.Rtl
                    ), modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    "آیا مطمئنید که می‌خواهید همه یادداشت‌ها را حذف کنید؟\nاین عمل غیر قابل بازگشت است.",
                    style = typography.bodyMedium.copy(
                        textAlign = TextAlign.Right,
                        textDirection = TextDirection.Rtl
                    ), modifier = Modifier.fillMaxWidth()
                )

            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmDeleteAllNotes) {
                    Text("حذف همه")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDeleteAllDialog) {
                    Text("لغو")
                }
            }
        )
    }


    // ===============================================
    //                  بخش Scaffold
    // ===============================================
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "یادداشت‌های من",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "تغییر تم"
                        )
                    }

                    IconButton(onClick = { viewModel.showDeleteAllDialog() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "حذف همه")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFFFF9800),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "افزودن"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // سرچ‌بار
            NotesSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                if (notes.isEmpty()) {
                    NotesEmptyState()
                } else {

                    NotesList(
                        notes = notes,
                        onNoteClick = onNoteClick,
                        searchQuery = searchQuery,
                        onDeleteClick = viewModel::showDeleteNoteDialog, // تابع حذف تکی
                        onTogglePin = viewModel::togglePin, // تابع پین کردن
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}