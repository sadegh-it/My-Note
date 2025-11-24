package io.github.sadeghit.mynote.ui.screen.notes.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sadeghit.mynote.ui.model.NoteUiModel

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun NotesGrid(
    notes: List<NoteUiModel>,
    onNoteClick: (Long) -> Unit,
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        modifier = modifier
    ) {
        items(notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                searchQuery = searchQuery,     // این خط بود که ارور می‌داد
                onClick = { onNoteClick(note.id) }
            )
        }
    }
}