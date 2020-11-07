package dev.lcdsmao.themeambient

import androidx.compose.animation.animate
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun animate(
  colors: Colors,
  animSpec: AnimationSpec<Color> = remember { spring() },
): Colors {

  @Composable
  fun animateColor(color: Color): Color = animate(target = color, animSpec = animSpec)

  return Colors(
    primary = animateColor(colors.primary),
    primaryVariant = animateColor(colors.primaryVariant),
    secondary = animateColor(colors.secondary),
    secondaryVariant = animateColor(colors.secondaryVariant),
    background = animateColor(colors.background),
    surface = animateColor(colors.surface),
    error = animateColor(colors.error),
    onPrimary = animateColor(colors.onPrimary),
    onSecondary = animateColor(colors.onSecondary),
    onBackground = animateColor(colors.onBackground),
    onSurface = animateColor(colors.onSurface),
    onError = animateColor(colors.onError),
    isLight = colors.isLight,
  )
}
