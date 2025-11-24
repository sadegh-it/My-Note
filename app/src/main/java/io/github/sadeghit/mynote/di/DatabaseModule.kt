package io.github.sadeghit.mynote.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.sadeghit.mynote.data.local.db.AppDatabase
import io.github.sadeghit.mynote.data.local.db.dao.NoteDao
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "my_note_db" // اسم تمیز و استاندارد
        )
            .fallbackToDestructiveMigration() // فقط برای توسعه — بعداً Migration اضافه می‌کنیم
            .build()
    }

    @Provides
    @Singleton // اینم Singleton کنیم بهتره (هرچند Room خودش مدیریت می‌کنه)
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }
}