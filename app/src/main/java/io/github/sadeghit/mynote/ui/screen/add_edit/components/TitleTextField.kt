package io.github.sadeghit.mynote.ui.screen.add_edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.TextFieldDefaults

@Composable
fun TitleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        colors = TextFieldDefaults.colors(
        ),
        onValueChange = onValueChange,
        label = { Text("عنوان", fontWeight = FontWeight.Medium) },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        )
        ,
        modifier = modifier.fillMaxWidth()
    )
}