package io.github.sadeghit.mynote.ui.screen.add_edit.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun TitleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("عنوان", fontWeight = FontWeight.Medium) },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier.fillMaxWidth()
    )
}}