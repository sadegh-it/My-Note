package io.github.sadeghit.mynote.repository

import io.github.sadeghit.mynote.data.local.db.dao.NoteDao
import io.github.sadeghit.mynote.data.local.db.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDao: NoteDao
) {


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
        noteDao.updateIsPinned(
            id = noteId,
            isPinned = isPinned,
            currentTime = System.currentTimeMillis()
        )
    }

    // تغییر رنگ
    suspend fun changeColor(noteId: Long, color: Int) {
        val note = noteDao.getNoteById(noteId) ?: return
        noteDao.update(note.copy(color = color, updatedAt = System.currentTimeMillis()))
    }
}