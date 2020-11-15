package dev.lcdsmao.jettheme.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.ProvideAppTheme
import dev.lcdsmao.jettheme.ProvideTheme
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemePack

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
