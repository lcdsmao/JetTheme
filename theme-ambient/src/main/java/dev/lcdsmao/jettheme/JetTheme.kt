package dev.lcdsmao.jettheme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticAmbientOf

@Composable
inline fun <reified T : JetThemeSpec> ProvideJetTheme(
  themeSpecMap: JetThemeSpecMap,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = JetThemeController(themeSpecMap)
  Providers(JetThemeAmbient provides themeController) {
    val currentTheme = JetThemeAmbient.current.themeState().value
    Crossfade(currentTheme, animation = crossfadeAnimSpec) { theme ->
      if (theme != null) {
        content(theme as T)
      }
    }
  }
}

val JetThemeAmbient = staticAmbientOf<JetThemeController>()
