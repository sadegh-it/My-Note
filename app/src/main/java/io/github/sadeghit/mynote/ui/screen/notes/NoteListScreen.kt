package io.github.sadeghit.mynote.ui.screen.notes


import NotesSearchBar
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sadeghit.mynote.ui.screen.notes.components.*
import io.github.sadeghit.mynote.viewModel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    // برای تغییر تم
    val toggleTheme = { viewModel.toggleTheme() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "یادداشت‌های من",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "تغییر تم"
                        )
                    }
                    IconButton(onClick = { viewModel.deleteAllNotes() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "حذف همه")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = androidx.compose.ui.graphics.Color(0xFFFF9800), // نارنجی گوگل
                contentColor = androidx.compose.ui.graphics.Color.White
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "افزودن"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // سرچ‌بار
            NotesSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )

            // لیست یا گرید — فعلاً لیست، اگه خواستی گرید هم اضافه می‌کنم
            if (notes.isEmpty()) {
                NotesEmptyState()
            } else {
                NotesList(
                    notes = notes,
                    onNoteClick = onNoteClick,
                    searchQuery = searchQuery,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}