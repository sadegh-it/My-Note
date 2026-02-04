package io.github.sadeghit.mynote.ui.screen.notes


import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sadeghit.mynote.data.local.datastore.ThemeManager
import io.github.sadeghit.mynote.ui.screen.notes.components.AppAlertDialog
import io.github.sadeghit.mynote.ui.screen.notes.components.MessageHandler
import io.github.sadeghit.mynote.ui.screen.notes.components.MyCustomSnackbar
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesEmptyState
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesList
import io.github.sadeghit.mynote.ui.screen.notes.components.NotesSearchBar
import io.github.sadeghit.mynote.viewModel.NotesViewModel


 @Composable
fun NoteListScreen(
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),

) {
    val themeManager: ThemeManager = hiltViewModel()
    val focusManager = LocalFocusManager.current
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val isDarkTheme = themeManager.isDarkTheme


    // FIX: تعریف تابع toggleTheme جدید
    val toggleTheme = { themeManager.toggleTheme() }


    // پراپرتی‌های مربوط به مدیریت حذف
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val showDeleteAllDialog by viewModel.showDeleteAllDialog.collectAsStateWithLifecycle()
    val noteToDelete by viewModel.noteToDelete.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    MessageHandler(eventFlow = viewModel.event, snackbarHostState = snackbarHostState)
    // برای تغییر تم



    // ===============================================
    //               بخش مدیریت دایالوگ‌ها
    // ===============================================

    // --- دایالوگ حذف تکی ---
    AppAlertDialog(
        isVisible = showDeleteDialog && noteToDelete != null,
        title = "حذف یادداشت",
        message = "آیا مطمئنید که می‌خواهید یادداشت را حذف کنید؟",
        confirmText = "حذف",
        dismissText = "لغو",
        onConfirm = { viewModel.confirmDeleteNote() },
        onDismiss = { viewModel.hideDeleteNoteDialog() }
    )


    AppAlertDialog(
        isVisible = showDeleteAllDialog,
        title = "حذف همه یادداشت‌ها",
        message = "آیا مطمئنید که می‌خواهید همه یادداشت‌ها را حذف کنید؟\nاین عمل غیر قابل بازگشت است.",
        confirmText = "حذف همه",
        dismissText = "لغو",
        onConfirm = { viewModel.confirmDeleteAllNotes() },
        onDismiss = { viewModel.hideDeleteAllDialog() }
    )


    // ===============================================
    //                  بخش Scaffold
    // ===============================================
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) { data -> MyCustomSnackbar(data) } },
        topBar = {
            NotesTopBar(
                isDarkMode = isDarkTheme,
                onToggleTheme = toggleTheme,
                onDeleteAll = { viewModel.showDeleteAllDialog() }
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

                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
                .padding(paddingValues)
        )
        {

            // سرچ‌بار
            NotesSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)

            )


            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                if (notes.isEmpty()) {
                    NotesEmptyState()
                } else {

                    NotesList(
                        notes = notes,
                        onNoteClick = {
                            focusManager.clearFocus()
                            onNoteClick(it)
                        },
                        searchQuery = searchQuery,
                        onDeleteClick = viewModel::showDeleteNoteDialog,
                        onTogglePin = viewModel::togglePin,
                        modifier = Modifier.fillMaxSize(),
                    )

                }
            }
        }

    }
}
