package io.github.sadeghit.mynote.ui.screen.notes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.sadeghit.mynote.core.util.highlightText
import io.github.sadeghit.mynote.ui.model.NoteUiModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: NoteUiModel,
    searchQuery: String = "",
    onClick: () -> Unit,
    onDeleteClick: (NoteUiModel) -> Unit,
    onTogglePin: (Long, Boolean) -> Unit
) {
    LocalContentColor.current


    val cardColor by animateColorAsState(
        targetValue = if (note.color == 0) {
            MaterialTheme.colorScheme.surface
        } else {
            Color(note.color)
        },
        label = "CardColor"
    )


    val baseTextColor = if (note.color != 0) {
        if (cardColor.luminance() > 0.5f) Color.Black else Color.White
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val finalTextColor = if (note.color == Color.White.toArgb() &&
        MaterialTheme.colorScheme.background.luminance() < 0.3f
    ) {
        Color.Black
    } else {
        baseTextColor
    }
    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = { onDeleteClick(note) } // <--- فراخوانی حذف با نگه داشتن انگشت
            ),
        color = cardColor,
        contentColor = finalTextColor

    ) {

        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // عنوان
                Text(
                    text = highlightText(note.title.ifBlank { "بدون عنوان" }, searchQuery),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                // محتوا
                Text(
                    text = highlightText(note.content.ifBlank { "بدون محتوا" }, searchQuery),
                    style = MaterialTheme.typography.bodyMedium,


                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))

                // تاریخ
                Text(
                    text = note.updatedAtPersian,
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.6f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.PushPin,
                    contentDescription = "پین شده",
                    tint = if (note.isPinned)
                        MaterialTheme.colorScheme.primary
                    else LocalContentColor.current.copy(alpha = 0.4f),
                    modifier = Modifier

                        .clickable { onTogglePin(note.id, note.isPinned) }
                        .padding(4.dp)
                        .size(24.dp)
                )

                Spacer(Modifier.width(12.dp))

                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = LocalContentColor.current.copy(alpha = 0.7f),
                    modifier = Modifier
                        .clickable { onDeleteClick(note) }
                        .padding(4.dp)
                        .size(24.dp)
                )
            }


        }
    }
}
