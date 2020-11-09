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
  themeControllerConfig: JetThemeControllerConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = rememberJetThemeController(themeControllerConfig)
  Providers(JetThemeAmbient provides themeController) {
    val currentTheme = JetThemeAmbient.current.themeState<T>().value
    Crossfade(currentTheme, animation = crossfadeAnimSpec) { theme ->
      if (theme != null) {
        content(theme)
      }
    }
  }
}

@Composable
inline fun <reified T : JetThemeSpec> ProvideAppJetTheme(
  theme: JetTheme,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) = ProvideJetTheme(
  themeControllerConfig = JetThemeControllerConfig.Persistence(
    theme = theme,
  ),
  crossfadeAnimSpec = crossfadeAnimSpec,
  content = content,
)

val JetThemeAmbient = staticAmbientOf<JetThemeController>()
