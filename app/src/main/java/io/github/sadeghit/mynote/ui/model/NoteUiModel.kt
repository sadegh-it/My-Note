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

    // 1. به‌روزرسانی با تاریخ ایجاد (createdAt)
    persianDate.update(this.createdAt)
    val createdDateString = persianDate.getFullDate()

    // 2. به‌روزرسانی مجدد با تاریخ ویرایش (updatedAt)
    persianDate.update(this.updatedAt)
    val updatedDateString = persianDate.getFullDate()

    return NoteUiModel(
        id = id,
        title = title,
        content = content,
        color = color,
        isPinned = isPinned,
        createdAtPersian = createdDateString, // استفاده از تاریخ ایجاد
        updatedAtPersian = updatedDateString  // استفاده از تاریخ ویرایش
    )
}