package io.github.sadeghit.mynote.data.local.datastore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeManager @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    var isDarkTheme by mutableStateOf(false)
        private set

    init {
        // هنگام شروع اپ، مقدار ذخیره شده را بخوان
        themeDataStore.isDarkMode
            .onEach { isDark ->
                isDarkTheme = isDark
            }
            .launchIn(viewModelScope)
    }

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        // ذخیره تغییر
        viewModelScope.launch {
            themeDataStore.setDarkMode(isDarkTheme)
        }
    }
}
