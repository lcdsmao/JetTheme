package dev.lcdsmao.jettheme.material

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.ProvideAppTheme
import dev.lcdsmao.jettheme.ProvideTheme
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.AmbientThemeController
import dev.lcdsmao.jettheme.ThemePack

/**
 * Theme material design version of [ProvideTheme].
 * Binds a [ThemeController] with configuration [themeConfig] to the [AmbientThemeController] key.
 * Recompose the [content] with a crossfade animation when [ThemeController.themeFlow] value changed.
 *
 * @param themeConfig the configuration for the [ThemeController].
 * @param crossfadeAnimSpec the [AnimationSpec] to configure [Crossfade].
 */
@Composable
fun ProvideMaterialTheme(
  themeConfig: ThemeConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideTheme<MaterialThemeSpec>(
    themeConfig = themeConfig,
    crossfadeAnimSpec = crossfadeAnimSpec,
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
 * @param crossfadeAnimSpec the [AnimationSpec] to configure [Crossfade].
 */
@Composable
fun ProvideAppMaterialTheme(
  themePack: ThemePack,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideAppTheme<MaterialThemeSpec>(
    themePack = themePack,
    crossfadeAnimSpec = crossfadeAnimSpec,
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
