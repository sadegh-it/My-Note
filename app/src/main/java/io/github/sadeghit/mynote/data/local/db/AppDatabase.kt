package io.github.sadeghit.mynote.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sadeghit.mynote.data.local.db.dao.NoteDao
import io.github.sadeghit.mynote.data.local.db.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = AppDatabase.VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object{
        const val VERSION = 1

    }
    abstract fun noteDao(): NoteDao
}