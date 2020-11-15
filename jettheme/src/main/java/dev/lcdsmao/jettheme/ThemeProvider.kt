package dev.lcdsmao.jettheme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticAmbientOf

@Composable
inline fun <reified T : ThemeSpec> ProvideTheme(
  themeConfig: ThemeConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = rememberThemeController(themeConfig)
  Providers(ThemeAmbient provides themeController) {
    val currentTheme = ThemeAmbient.current.themeState<T>().value
    Crossfade(currentTheme, animation = crossfadeAnimSpec) { theme ->
      if (theme != null) {
        content(theme)
      }
    }
  }
}

@Composable
inline fun <reified T : ThemeSpec> ProvideAppTheme(
  themePack: ThemePack,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) = ProvideTheme(
  themeConfig = themePack.persistenceConfig(),
  crossfadeAnimSpec = crossfadeAnimSpec,
  content = content,
)

val ThemeAmbient = staticAmbientOf<ThemeController>()
