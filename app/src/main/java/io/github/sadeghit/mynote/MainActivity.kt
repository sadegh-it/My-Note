package io.github.sadeghit.mynote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.sadeghit.mynote.navigation.SetupNavigation
import io.github.sadeghit.mynote.ui.theme.MyNoteTheme
import io.github.sadeghit.mynote.viewModel.NotesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: NotesViewModel = hiltViewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            MyNoteTheme(darkTheme = isDarkMode) {
                SetupNavigation()
            }
        }
    }
}