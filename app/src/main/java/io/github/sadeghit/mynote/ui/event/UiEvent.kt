package io.github.sadeghit.mynote.ui.event

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
    data class NavigateToEdit(val noteId: Long) : UiEvent()
}