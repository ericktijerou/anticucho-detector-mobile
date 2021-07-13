package com.ericktijerou.anticucho_detector.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ericktijerou.anticucho_detector.android.util.LocalSysUiController

private val DarkColorPalette = darkColors(
    primary = BlackLight,
    primaryVariant = BlackLight,
    secondary = Teal200,
    background = BackgroundDark,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = Color.White,
    secondary = Teal200,
    background = BackgroundLight,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val LightCustomColorPalette = DetectorThemeColors(
    textPrimaryColor = Color.Black,
    textSecondaryColor = TextSecondaryLight,
    isDark = false
)

private val DarkCustomColorPalette = DetectorThemeColors(
    textPrimaryColor = Color.White,
    textSecondaryColor = TextSecondaryDark,
    isDark = true
)
private val LightElevation = Elevations(defaultElevation = 0.dp)

private val DarkElevation = Elevations(defaultElevation = 0.dp)

@Composable
fun DetectorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val (colors, customColors) = if (darkTheme) DarkColorPalette to DarkCustomColorPalette else LightColorPalette to LightCustomColorPalette
    val sysUiController = LocalSysUiController.current
    val elevation = if (darkTheme) DarkElevation else LightElevation
    SideEffect {
        sysUiController.setStatusBarColor(color = colors.background, darkIcons = false)
        sysUiController.setNavigationBarColor(color = colors.background)
    }
    ProvideMyThemeColors(elevation, customColors) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

object MyTheme {
    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    val elevations: Elevations
        @Composable
        get() = LocalElevations.current

    val customColors: DetectorThemeColors
        @Composable
        get() = LocalMyThemeColors.current
}

@Composable
fun ProvideMyThemeColors(
    elevation: Elevations,
    colors: DetectorThemeColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalMyThemeColors provides colorPalette,
        content = content
    )
}

@Stable
class DetectorThemeColors(
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    isDark: Boolean
) {
    var textPrimaryColor by mutableStateOf(textPrimaryColor)
        private set
    var textSecondaryColor by mutableStateOf(textSecondaryColor)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: DetectorThemeColors) {
        textPrimaryColor = other.textPrimaryColor
        textSecondaryColor = other.textSecondaryColor
        isDark = other.isDark
    }
}

private val LocalMyThemeColors = staticCompositionLocalOf<DetectorThemeColors> {
    error("No ColorPalette provided")
}
