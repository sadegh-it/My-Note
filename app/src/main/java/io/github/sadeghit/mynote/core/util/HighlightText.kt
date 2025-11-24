package io.github.sadeghit.mynote.core.util


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun highlightText(
    text: String,
    query: String,
    highlightColor: Color = Color.Red
): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)

    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()

    return buildAnnotatedString {
        var startIndex = 0

        while (true) {
            val foundIndex = lowerText.indexOf(lowerQuery, startIndex)
            if (foundIndex == -1) {
                append(text.substring(startIndex))
                break
            }

            // متن قبل از کلمه پیدا شده
            if (foundIndex > startIndex) {
                append(text.substring(startIndex, foundIndex))
            }

            // کلمه پیدا شده → قرمز و بولد
            withStyle(
                style = SpanStyle(
                    color = highlightColor,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(text.substring(foundIndex, foundIndex + query.length))
            }

            startIndex = foundIndex + query.length
        }
    }
}