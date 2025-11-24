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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAllNotes()
            _event.emit(UiEvent.ShowMessage("همه یادداشت‌ها حذف شدند"))
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val current = appSettings.isDarkMode.first()  // مقدار واقعی از DataStore
            appSettings.setDarkMode(!current)             // ذخیره می‌شه و همه جا اعمال می‌شه
        }
    }




    // تنظیمات
    val isDarkMode: StateFlow<Boolean> = appSettings.isDarkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isGridMode: StateFlow<Boolean> = appSettings.isGridMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // لیست یادداشت‌ها
    private val _notes = MutableStateFlow<List<NoteUiModel>>(emptyList())
    val notes: StateFlow<List<NoteUiModel>> = _notes.asStateFlow()

    // رویدادهای UI (Toast, Navigation و ...)
    private val _event = MutableSharedFlow<UiEvent>()
    val event: SharedFlow<UiEvent> = _event.asSharedFlow()

    // حالت ویرایش یادداشت
    private val _editTitle = MutableStateFlow("")
    val editTitle: StateFlow<String> = _editTitle.asStateFlow()

    private val _editContent = MutableStateFlow("")
    val editContent: StateFlow<String> = _editContent.asStateFlow()

    private val _editColor = MutableStateFlow(Color(0xFFFFFFFF))
    val editColor: StateFlow<Color> = _editColor.asStateFlow()

    private val _editIsPinned = MutableStateFlow(false)
    val editIsPinned: StateFlow<Boolean> = _editIsPinned.asStateFlow()

    private val _showDiscardDialog = MutableStateFlow(false)
    val showDiscardDialog: StateFlow<Boolean> = _showDiscardDialog.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            repository.getAllNotes()
                .catch { _event.emit(UiEvent.ShowMessage("خطا در بارگذاری یادداشت‌ها")) }
                .collectLatest { entities ->
                    _notes.value = entities.map { it.toUiModel(persianDate) }
                }
        }
    }

    // === عملیات روی یادداشت‌ها ===
    fun togglePin(noteId: Long) {
        viewModelScope.launch {
            val note = repository.getNoteById(noteId) ?: return@launch
            repository.togglePin(noteId, !note.isPinned)
        }
    }

    fun changeNoteColor(noteId: Long, color: Int) {
        viewModelScope.launch {
            repository.changeColor(noteId, color)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _event.emit(UiEvent.ShowMessage("یادداشت حذف شد"))
        }
    }

    // === حالت ویرایش ===
    fun loadNoteForEdit(noteId: Long) {
        viewModelScope.launch {
            repository.getNoteById(noteId)?.let { note ->
                _editTitle.value = note.title
                _editContent.value = note.content
                _editColor.value = Color(note.color)
                _editIsPinned.value = note.isPinned
            }
        }
    }

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
                createdAt = if (noteId == null) currentTime else repository.getNoteById(noteId!!)?.createdAt
                    ?: currentTime,
                updatedAt = currentTime
            )

            if (noteId == null) {
                repository.insertNote(note)
            } else {
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
        _showDiscardDialog.value = false
    }
}