package dev.ysengoku.swiftycompanion.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Navy = Color(0xFF000032)
val OceanBlue = Color(0xFF184664)
val LightGreen = Color(0xFF4EB490)
val BlueWhite = Color(0xFFF8FAFB)
val SuccessGreen = Color(0xFF236B67)
val SuccessGreenDark = Color(0xFF80d5cf)
val ErrorRed = Color(0xFFC94C4C)
val ErrorRedDark = Color(0xFFFFB3AF)

val BabyBlueEyes = Color(0xFF97CCF8)

val BrandGradient = Brush.linearGradient(colors = listOf(Navy, OceanBlue, LightGreen))

data class ExtendedColors(
    val success: Color
)

val LightExtendedColors = ExtendedColors(
    success = SuccessGreen
)

val DarkExtendedColors = ExtendedColors(
    success = SuccessGreenDark
)
