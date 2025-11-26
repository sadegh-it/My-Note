package io.github.sadeghit.mynote.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sadeghit.mynote.core.util.PersianDate
import io.github.sadeghit.mynote.data.local.datastore.AppSettings
import io.github.sadeghit.mynote.data.local.db.entity.NoteEntity
import io.github.sadeghit.mynote.repository.NotesRepository
import io.github.sadeghit.mynote.ui.event.UiEvent
import io.github.sadeghit.mynote.ui.model.NoteUiModel
import io.github.sadeghit.mynote.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val persianDate: PersianDate,
    private val appSettings: AppSettings
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = appSettings.isDarkMode
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    private val _isNoteLoaded = MutableStateFlow(false)
    val isNoteLoaded: StateFlow<Boolean> = _isNoteLoaded.asStateFlow()

    fun loadNoteForEdit(noteId: Long?) {
        if (noteId == null || noteId == 0L) {
            resetEditState()
            _originalNote.value = null
            _isNoteLoaded.value = true // نوت جدید، آماده
            return
        }

        viewModelScope.launch {
            repository.getNoteById(noteId)?.let { note ->
                _originalNote.value = note
                _editTitle.value = note.title
                _editContent.value = note.content
                _editColor.value = Color(note.color)
                _editIsPinned.value = note.isPinned
                _isNoteLoaded.value = true // لود کامل شد
            }
        }
    }

    // برای نگهداری یادداشتی که باید حذف شود (در حذف تکی)
    private val _noteToDelete = MutableStateFlow<NoteUiModel?>(null)
    val noteToDelete: StateFlow<NoteUiModel?> = _noteToDelete.asStateFlow()

    // نمایش/عدم نمایش دایالوگ حذف تکی
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    // نمایش/عدم نمایش دایالوگ حذف همه
    private val _showDeleteAllDialog = MutableStateFlow(false)
    val showDeleteAllDialog: StateFlow<Boolean> = _showDeleteAllDialog.asStateFlow()

    private var recentlyUnpinnedNote: NoteEntity? = null

    fun togglePin(noteId: Long, currentIsPinned: Boolean) {
        viewModelScope.launch {
            val noteEntity = repository.getNoteById(noteId) ?: return@launch
            val newPinState = !currentIsPinned

            // اگه داریم از پین درمیاریم → برای Undo ذخیره کن
            recentlyUnpinnedNote = if (currentIsPinned) {
                noteEntity
            } else {
                null // اگه پین کردیم، Undo لازم نیست
            }

            val updatedNote = noteEntity.copy(isPinned = newPinState)
            repository.updateNote(updatedNote)

            val message = if (newPinState) "یادداشت پین شد" else "یادداشت از پین برداشته شد"

            _event.emit(
                UiEvent.ShowMessage(
                    message = message,
                    actionLabel = if (!newPinState) "برگرداندن" else null, // فقط وقتی از پین درآوردیم، دکمه برگرداندن باشه
                    onActionPerformed = if (!newPinState) {
                        { onUndoUnpin() }
                    } else null
                )
            )
        }
    }

    // تابع Undo برای برگرداندن پین
    private fun onUndoUnpin() {
        recentlyUnpinnedNote?.let { note ->
            viewModelScope.launch {
                repository.updateNote(note.copy(isPinned = true))
                recentlyUnpinnedNote = null
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleTheme() {
        viewModelScope.launch {
            appSettings.setDarkMode(!isDarkMode.value)
        }
    }


    fun showDeleteNoteDialog(note: NoteUiModel) {
        _noteToDelete.value = note
        _showDeleteDialog.value = true
    }

    fun hideDeleteNoteDialog() {
        _noteToDelete.value = null
        _showDeleteDialog.value = false
    }


    fun showDeleteAllDialog() {
        _showDeleteAllDialog.value = true
    }

    fun hideDeleteAllDialog() {
        _showDeleteAllDialog.value = false
    }


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val notes: StateFlow<List<NoteUiModel>> = combine(
        repository.getAllNotes(),
        _searchQuery
    ) { notes, query ->
        notes.map { it.toUiModel(persianDate) }
            .filter {
                if (query.isBlank()) true else {
                    it.title.contains(query, ignoreCase = true) ||
                            it.content.contains(query, ignoreCase = true)
                }
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )


    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    // --- Add/Edit State ---
    private val _editTitle = MutableStateFlow("")
    val editTitle: StateFlow<String> = _editTitle.asStateFlow()

    private val _editContent = MutableStateFlow("")
    val editContent: StateFlow<String> = _editContent.asStateFlow()

    private val _editColor = MutableStateFlow(Color(0x00000000))
    val editColor: StateFlow<Color> = _editColor.asStateFlow()

    private val _editIsPinned = MutableStateFlow(false)
    val editIsPinned: StateFlow<Boolean> = _editIsPinned.asStateFlow()

    private val _showDiscardDialog = MutableStateFlow(false)
    val showDiscardDialog: StateFlow<Boolean> = _showDiscardDialog.asStateFlow()

    private val _originalNote = MutableStateFlow<NoteEntity?>(null)


    val hasChanges: StateFlow<Boolean> = combine(
        _originalNote,
        _editTitle,
        _editContent,
        _editColor,
        _editIsPinned
    ) { originalNote, title, content, color, isPinned ->
        // اگر نوت جدید باشد (originalNote == null) و محتوایی وجود داشته باشد، یعنی تغییرات وجود دارد
        if (originalNote == null) {
            title.isNotBlank() || content.isNotBlank()
        } else {
            // اگر نوت موجود باشد، چک می‌کنیم که آیا چیزی تغییر کرده است یا خیر
            title.trim() != originalNote.title.trim() ||
                    content.trim() != originalNote.content.trim() ||
                    color.toArgb() != originalNote.color ||
                    isPinned != originalNote.isPinned
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    fun onTitleChanged(title: String) {
        _editTitle.value = title
    }

    fun onContentChanged(content: String) {
        _editContent.value = content
    }

    fun onColorChanged(color: Color) {
        _editColor.value = color
    }

    fun onPinChanged(isPinned: Boolean) {
        _editIsPinned.value = isPinned
    }

    fun showDiscardDialog() {
        _showDiscardDialog.value = true
    }

    fun hideDiscardDialog() {
        _showDiscardDialog.value = false
    }

    fun saveNote(noteId: Long?) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val note = NoteEntity(
                id = noteId ?: 0L,
                title = _editTitle.value.trim(),
                content = _editContent.value.trim(),
                color = _editColor.value.toArgb(),
                isPinned = _editIsPinned.value,
                // isArchived را به false تنظیم می‌کنیم
                isArchived = false,
                createdAt = if (noteId == null) currentTime else repository.getNoteById(noteId)?.createdAt
                    ?: currentTime,
                updatedAt = currentTime
            )

            if (noteId == null || noteId == 0L) { // اگر نوت جدید است
                repository.insertNote(note)
            } else { // اگر نوت موجود است
                repository.updateNote(note)
            }

            _event.emit(UiEvent.ShowMessage("یادداشت ذخیره شد"))
            _event.emit(UiEvent.NavigateBack)
            resetEditState()
        }
    }

    fun resetEditState() {
        _editTitle.value = ""
        _editContent.value = ""
        _editColor.value = Color(0xFFFFFFFF)
        _editIsPinned.value = false
    }


    private var recentlyDeletedNoteEntity: NoteEntity? = null


    // این متد توسط دایالوگ در UI فراخوانی می‌شود
    fun confirmDeleteNote() {
        noteToDelete.value?.let { noteUiModel ->
            viewModelScope.launch {
                // ۱. یادداشت را از دیتابیس بخوان تا نسخه کامل Entity آن را داشته باشیم
                val noteEntityToDelete = repository.getNoteById(noteUiModel.id)

                if (noteEntityToDelete != null) {
                    // ۲. یادداشت Entity را در متغیر موقت ذخیره کن
                    recentlyDeletedNoteEntity = noteEntityToDelete

                    // ۳. حالا یادداشت را از پایگاه داده حذف کن
                    repository.deleteNote(noteEntityToDelete)

                    // ۴. دایالوگ را ببند
                    hideDeleteNoteDialog()

                    // ۵. پیام را با دکمه "برگرداندن" برای UI ارسال کن
                    _event.emit(
                        UiEvent.ShowMessage(
                            message = "یادداشت حذف شد",
                            actionLabel = "برگرداندن",
                            onActionPerformed = { onUndoDeleteClick() }
                        )
                    )
                } else {
                    // اگر به هر دلیلی یادداشت پیدا نشد، فقط یک پیام خطا بده
                    hideDeleteNoteDialog()
                    _event.emit(UiEvent.ShowMessage("خطا در حذف یادداشت"))
                }
            }
        }
    }

    // این متد باید توسط MessageHandler فراخوانی شود وقتی کاربر روی "برگرداندن" کلیک می‌کند
    fun onUndoDeleteClick() {
        recentlyDeletedNoteEntity?.let { noteToRestore ->
            viewModelScope.launch {
                // یادداشت حذف شده را دوباره به پایگاه داده اضافه کن
                repository.insertNote(noteToRestore)
                // متغیر موقت را خالی کن تا دوباره استفاده نشود
                recentlyDeletedNoteEntity = null
            }
        }
    }

    private var recentlyDeletedAllNotes: List<NoteEntity> = emptyList()
    fun confirmDeleteAllNotes() {
        viewModelScope.launch {
            // گرفتن لیست فعلی یادداشت‌ها از Flow
            val allNotes = repository.getAllNotes().first()

            // اگر خالی بود، فقط دیالوگ رو ببند
            if (allNotes.isEmpty()) {
                hideDeleteAllDialog()
                return@launch
            }

            // ذخیره موقت برای Undo
            recentlyDeletedAllNotes = allNotes

            // حذف همه از دیتابیس
            repository.deleteAllNotes()

            // بستن دیالوگ
            hideDeleteAllDialog()

            // نمایش اسنک‌بار با دکمه‌دار
            _event.emit(
                UiEvent.ShowMessage(
                    message = "همه یادداشت‌ها حذف شدند",
                    actionLabel = "برگرداندن",
                    onActionPerformed = { onUndoDeleteAll() }
                )
            )
        }
    }

    fun onUndoDeleteAll() {
        if (recentlyDeletedAllNotes.isEmpty()) return

        viewModelScope.launch {
            // همه یادداشت‌های حذف شده رو دوباره اضافه کن
            recentlyDeletedAllNotes.forEach { note ->
                repository.insertNote(note)
            }

            // پاک کردن حافظه موقت
            recentlyDeletedAllNotes = emptyList()

            // پیام موفقیت (اختیاری)
            _event.emit(UiEvent.ShowMessage("یادداشت‌ها برگشتند"))
        }
    }


}

