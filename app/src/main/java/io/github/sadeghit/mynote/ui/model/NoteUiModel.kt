package io.github.sadeghit.mynote.ui.model

import io.github.sadeghit.mynote.core.util.PersianDate
import io.github.sadeghit.mynote.data.local.db.entity.NoteEntity

data class NoteUiModel(
    val id: Long,
    val title: String,
    val content: String,
    val color: Int,
    val isPinned: Boolean,
    val createdAtPersian: String,
    val updatedAtPersian: String
)

fun NoteEntity.toUiModel(persianDate: PersianDate): NoteUiModel {
    persianDate.update()
    return NoteUiModel(
        id = id,
        title = title,
        content = content,
        color = color,
        isPinned = isPinned,
        createdAtPersian = persianDate.getFullDate(),
        updatedAtPersian = persianDate.getFullDate()
    )
}