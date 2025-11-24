
package io.github.sadeghit.mynote.ui.screen.notes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.sadeghit.mynote.core.util.highlightText

import io.github.sadeghit.mynote.ui.model.NoteUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: NoteUiModel,
    searchQuery: String = "",        // این اضافه شد
    onClick: () -> Unit,

) {
    val cardColor by animateColorAsState(
        targetValue = if (note.color == 0) MaterialTheme.colorScheme.surfaceVariant else Color(note.color),
        label = "card_color"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(cardColor)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = highlightText(note.title.ifBlank { "بدون عنوان" }, searchQuery),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = highlightText(note.content.ifBlank { "بدون محتوا" }, searchQuery),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = 0.75f),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = note.updatedAtPersian,
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.6f)
                )
            }

            if (note.isPinned) {
                Icon(
                    Icons.Default.PushPin,
                    contentDescription = "پین شده",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(20.dp)
                )
            }
        }
    }
}