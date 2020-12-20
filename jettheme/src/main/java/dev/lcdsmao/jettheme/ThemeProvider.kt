package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticAmbientOf

/**
 * Binds a [ThemeController] with configuration [themeConfig] to the [AmbientThemeController] key.
 * Recompose the [content] with a crossfade animation when [ThemeController.themeFlow] value changed.
 *
 * @param themeConfig the configuration for the [ThemeController].
 */
@Composable
inline fun <reified T : ThemeSpec> ProvideTheme(
  themeConfig: ThemeConfig<T>,
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = rememberThemeController(themeConfig)
  Providers(AmbientThemeController provides themeController) {
    val theme = AmbientThemeController.current.themeState<T?>(null).value
    if (theme != null) {
      content(theme)
    }
  }
}

/**
 * A convenient version of [ProvideTheme] suited to provide global app theme.
 * Configure the [ThemeController] by [ThemeConfig.Persistence], so theme preference will be
 * persisted in the local storage.
 *
 * @param themePack the [ThemePack] to construct a [ThemeConfig.Persistence]
 */
@Composable
inline fun <reified T : ThemeSpec> ProvideAppTheme(
  themePack: ThemePack<T>,
  crossinline content: @Composable (T) -> Unit,
) = ProvideTheme(
  themeConfig = themePack.persistenceConfig(),
  content = content,
)

/**
 * Uses this ambient to retrieve the [ThemeController] in current component tree.
 */
val AmbientThemeController = staticAmbientOf<ThemeController<out ThemeSpec>> {
  error("No ThemeController provided.")
}
