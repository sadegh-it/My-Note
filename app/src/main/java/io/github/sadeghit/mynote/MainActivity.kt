package io.github.sadeghit.mynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.sadeghit.mynote.data.local.datastore.ThemeManager
import io.github.sadeghit.mynote.navigation.SetupNavigation
import io.github.sadeghit.mynote.ui.theme.MyNoteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            val themeManager: ThemeManager = hiltViewModel()

            // خواندن وضعیت تم مستقیماً از mutableStateOf
            val isDarkMode = themeManager.isDarkTheme

            MyNoteTheme(darkTheme = isDarkMode) {

                SetupNavigation()
            }
        }
    }
}
