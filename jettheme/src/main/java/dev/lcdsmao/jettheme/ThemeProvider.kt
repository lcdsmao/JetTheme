package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Binds a [ThemeController] with configuration [themeConfig] to the [LocalThemeController] key.
 * Recompose the [content] when [ThemeController.themeFlow] value changed.
 *
 * @param themeConfig the configuration for the [ThemeController].
 */
@Composable
inline fun <reified T : ThemeSpec> ProvideTheme(
  themeConfig: ThemeConfig<T>,
  crossinline content: @Composable (T) -> Unit,
) {
  val themeController = rememberThemeController(themeConfig)
  CompositionLocalProvider(LocalThemeController provides themeController) {
    val theme = LocalThemeController.current.themeState<T?>(null).value
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
 * Uses this composition local to retrieve the [ThemeController] in current component tree.
 */
val LocalThemeController = staticCompositionLocalOf<ThemeController<out ThemeSpec>> {
  error("No ThemeController provided.")
}
