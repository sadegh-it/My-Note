package io.github.sadeghit.mynote.ui.screen.notes.components


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sadeghit.mynote.ui.model.NoteUiModel

@Composable
fun NotesList(
    notes: List<NoteUiModel>,
    onNoteClick: (Long) -> Unit,
    searchQuery: String = "",
    onDeleteClick: (NoteUiModel) -> Unit,  // <--- پارامتر جدید
    onTogglePin: (Long, Boolean) -> Unit,  // <--- پارامتر جدید
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    LazyVerticalStaggeredGrid(

        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                searchQuery = searchQuery,
                onClick = { onNoteClick(note.id) },
                onDeleteClick = onDeleteClick,
                onTogglePin = onTogglePin
            )
        }
    }
}