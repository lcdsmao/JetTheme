package dev.lcdsmao.jettheme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticAmbientOf

/**
 * Binds a [ThemeController] with configuration [themeConfig] to the [AmbientThemeController] key.
 * Recompose the [content] with a crossfade animation when [ThemeController.themeFlow] value changed.
 *
 * @param themeConfig the configuration for the [ThemeController].
 * @param crossfadeAnimSpec the [AnimationSpec] to configure [Crossfade].
 */
@Composable
inline fun <reified T : ThemeSpec> ProvideTheme(
  themeConfig: ThemeConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = rememberThemeController(themeConfig)
  Providers(AmbientThemeController provides themeController) {
    val currentTheme = AmbientThemeController.current.themeState<T>().value
    Crossfade(currentTheme, animation = crossfadeAnimSpec) { theme ->
      if (theme != null) {
        content(theme)
      }
    }
  }
}

/**
 * A convenient version of [ProvideTheme] suited to provide global app theme.
 * Configure the [ThemeController] by [ThemeConfig.Persistence], so theme preference will be
 * persisted in the local storage.
 *
 * @param themePack the [ThemePack] to construct a [ThemeConfig.Persistence]
 * @param crossfadeAnimSpec the [AnimationSpec] to configure [Crossfade].
 */
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

/**
 * Uses this ambient to retrieve the [ThemeController] in current component tree.
 */
val AmbientThemeController = staticAmbientOf<ThemeController> {
  error("No ThemeController provided.")
}

/**
 * Uses this ambient to retrieve the [ThemeController] in current component tree.
 */
@Deprecated(
  "Replace with AmbientThemeController",
  ReplaceWith("AmbientThemeController"),
)
val ThemeControllerAmbient
  get() = AmbientThemeController
