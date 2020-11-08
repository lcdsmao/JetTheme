package dev.lcdsmao.jettheme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.platform.ContextAmbient
import dev.lcdsmao.jettheme.internal.JetThemeSpecDataStore

val JetThemeAmbient = staticAmbientOf<JetThemeController>()

@Composable
fun ProvideJetTheme(
  themeSpecMap: JetThemeSpecMap,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  placeholder: @Composable () -> Unit = {},
  children: @Composable () -> Unit,
) {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  val themeController = remember(themeSpecMap) {
    JetThemeController(
      coroutineScope = coroutineScope,
      themeDataStore = JetThemeSpecDataStore.get(context),
      themeSpecMap = themeSpecMap,
    )
  }

  Providers(JetThemeAmbient provides themeController) {
    val currentTheme = JetThemeAmbient.current.themeState().value
    Crossfade(currentTheme, animation = crossfadeAnimSpec) { theme ->
      if (theme != null && theme is MaterialThemeSpec) {
        MaterialTheme(
          colors = theme.colors,
          typography = theme.typography,
          shapes = theme.shapes,
          children,
        )
      } else {
        val defaultTheme = themeSpecMap.default as MaterialThemeSpec
        MaterialTheme(
          colors = defaultTheme.colors,
          typography = defaultTheme.typography,
          shapes = defaultTheme.shapes,
          content = placeholder,
        )
      }
    }
  }
}
