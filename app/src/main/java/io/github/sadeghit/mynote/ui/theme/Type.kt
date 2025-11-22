package io.github.sadeghit.mynote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.sadeghit.mynote.R

val Vazirmatn = FontFamily(
    Font(R.font.vazirregular, FontWeight.Normal),
    Font(R.font.vazirbold, FontWeight.Bold)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Vazirmatn,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Vazirmatn,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Vazirmatn,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Vazirmatn,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Vazirmatn,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
)