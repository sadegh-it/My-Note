package io.github.sadeghit.mynote.repository

import io.github.sadeghit.mynote.data.local.db.dao.NoteDao
import io.github.sadeghit.mynote.data.local.db.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDao: NoteDao
) {

    // فقط دیتابیس رو برمی‌گردونه — بدون هیچ منطق اضافه
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Long): NoteEntity? = noteDao.getNoteById(id)

    suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insert(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.update(note.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.delete(note)
    }

    suspend fun deleteNoteById(id: Long) {
        noteDao.deleteById(id)
    }

    suspend fun deleteAllNotes() {
        noteDao.deleteAll()
    }

    // پین کردن / آن‌پین کردن
    suspend fun togglePin(noteId: Long, isPinned: Boolean) {
        val note = noteDao.getNoteById(noteId) ?: return
        noteDao.update(note.copy(isPinned = isPinned, updatedAt = System.currentTimeMillis()))
    }

    // تغییر رنگ
    suspend fun changeColor(noteId: Long, color: Int) {
        val note = noteDao.getNoteById(noteId) ?: return
        noteDao.update(note.copy(color = color, updatedAt = System.currentTimeMillis()))
    }
}