package dev.lcdsmao.jettheme.material

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import dev.lcdsmao.jettheme.LocalThemeController
import dev.lcdsmao.jettheme.ProvideAppTheme
import dev.lcdsmao.jettheme.ProvideTheme
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.ThemePack

/**
 * Theme material design version of [ProvideTheme].
 * Binds a [ThemeController] with configuration [themeConfig] to the [LocalThemeController] key.
 * Recompose the [content] when [ThemeController.themeFlow] value changed.
 *
 * @param themeConfig the configuration for the [ThemeController].
 */
@Composable
fun ProvideMaterialTheme(
  themeConfig: ThemeConfig<MaterialThemeSpec>,
  content: @Composable () -> Unit,
) {
  ProvideTheme(
    themeConfig = themeConfig,
  ) { theme ->
    MaterialTheme(theme, content)
  }
}

/**
 * Theme material design version of [ProvideAppTheme].
 * A convenient version of [ProvideMaterialTheme] suited to provide global app theme.
 * Configure the [ThemeController] by [ThemeConfig.Persistence], so theme preference will be
 * persisted in the local storage.
 *
 * @param themePack the [ThemePack] to construct a [ThemeConfig.Persistence]
 */
@Composable
fun ProvideAppMaterialTheme(
  themePack: ThemePack<MaterialThemeSpec>,
  content: @Composable () -> Unit,
) {
  ProvideAppTheme(
    themePack = themePack,
  ) { theme ->
    MaterialTheme(theme, content)
  }
}

@Composable
private fun MaterialTheme(
  theme: MaterialThemeSpec,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colors = theme.colors,
    typography = theme.typography,
    shapes = theme.shapes,
    content = content,
  )
}
