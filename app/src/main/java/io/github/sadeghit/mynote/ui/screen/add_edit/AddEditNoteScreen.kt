package io.github.sadeghit.mynote.ui.screen.add_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sadeghit.mynote.ui.event.UiEvent
import io.github.sadeghit.mynote.ui.screen.add_edit.components.AddEditTopAppBar
import io.github.sadeghit.mynote.ui.screen.add_edit.components.ColorPalette
import io.github.sadeghit.mynote.ui.screen.add_edit.components.ContentTextField
import io.github.sadeghit.mynote.ui.screen.add_edit.components.PinCheckbox
import io.github.sadeghit.mynote.ui.screen.add_edit.components.TitleTextField
import io.github.sadeghit.mynote.viewModel.NotesViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    noteId: Long? = null,
    onBack: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    // لود یادداشت در صورت ویرایش
    LaunchedEffect(noteId) {
        noteId?.let { viewModel.loadNoteForEdit(it) }
    }

    // وضعیت فیلدها
    val title by viewModel.editTitle.collectAsStateWithLifecycle()
    val content by viewModel.editContent.collectAsStateWithLifecycle()
    val selectedColor by viewModel.editColor.collectAsStateWithLifecycle()
    val isPinned by viewModel.editIsPinned.collectAsStateWithLifecycle()
    val showDiscardDialog by viewModel.showDiscardDialog.collectAsStateWithLifecycle()
    val hasContent = title.isNotBlank() || content.isNotBlank()

    // BackHandler
    BackHandler(enabled = hasContent) {
        viewModel.showDiscardDialog()
    }

    // مدیریت رویدادها
    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                is UiEvent.NavigateBack -> onBack()
                else -> Unit
            }
        }
    }

    // دیالوگ دور انداختن تغییرات
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("دور انداختن تغییرات؟") },
            text = { Text("تغییرات ذخیره نشده حذف خواهند شد.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.hideDiscardDialog()
                    viewModel.resetEditState()
                    onBack()
                }) {
                    Text("دور انداختن")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDiscardDialog) {
                    Text("لغو")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AddEditTopAppBar(
                onBackClick = {
                    if (hasContent) viewModel.showDiscardDialog() else onBack()
                },
                onSaveClick = { viewModel.saveNote(noteId) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            TitleTextField(
                value = title,
                onValueChange = viewModel::onTitleChanged
            )

            Spacer(Modifier.height(16.dp))

            ContentTextField(
                value = content,
                onValueChange = viewModel::onContentChanged
            )

            Spacer(Modifier.height(24.dp))

            PinCheckbox(
                isPinned = isPinned,
                onCheckedChange = viewModel::onPinChanged
            )

            Spacer(Modifier.height(24.dp))

            ColorPalette(
                selectedColor = selectedColor,
                onColorSelected = viewModel::onColorChanged
            )
        }
    }
}