package io.github.sadeghit.mynote.ui.screen.notes.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sadeghit.mynote.ui.model.NoteUiModel

@Composable
fun NotesList(
    notes: List<NoteUiModel>,
    onNoteClick: (Long) -> Unit,
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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