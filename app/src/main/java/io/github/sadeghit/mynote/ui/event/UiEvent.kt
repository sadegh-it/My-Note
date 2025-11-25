package io.github.sadeghit.mynote.ui.event

sealed class UiEvent {
    data class ShowMessage(
        val message: String,
        val actionLabel: String? = null,
        // <<-- این بخش جدید است --<<
        val onActionPerformed: (() -> Unit)? = null
    ) : UiEvent()
     object NavigateBack : UiEvent()

}