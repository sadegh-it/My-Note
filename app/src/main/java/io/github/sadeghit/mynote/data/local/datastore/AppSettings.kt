package io.github.sadeghit.mynote.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppSettings @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {

        private val IS_PERSIAN_DATE =
            booleanPreferencesKey("is_persian_date")  // بعداً برای سوییچ شمسی/میلادی
        private val GRID_MODE = booleanPreferencesKey("is_grid_mode")
        private val APP_LANGUAGE = intPreferencesKey("app_language") // 0 = fa, 1 = en
    }


    // --- Persian Date ---
    val isPersianDateEnabled: Flow<Boolean> = dataStore.data
        .map { it[IS_PERSIAN_DATE] ?: true } // پیش‌فرض شمسی

    suspend fun setPersianDateEnabled(enabled: Boolean) {
        dataStore.edit { it[IS_PERSIAN_DATE] = enabled }
    }

    // --- Grid / List Mode ---
    val isGridMode: Flow<Boolean> = dataStore.data
        .map { it[GRID_MODE] ?: true } // پیش‌فرض گرید

    suspend fun setGridMode(enabled: Boolean) {
        dataStore.edit { it[GRID_MODE] = enabled }
    }

    // --- Language ---
    val language: Flow<Int> = dataStore.data
        .map { it[APP_LANGUAGE] ?: 0 } // 0 = فارسی

    suspend fun setLanguage(lang: Int) {
        dataStore.edit { it[APP_LANGUAGE] = lang }
    }
}