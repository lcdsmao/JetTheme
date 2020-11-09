package dev.lcdsmao.jettheme.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.JetTheme
import dev.lcdsmao.jettheme.JetThemeConfig
import dev.lcdsmao.jettheme.ProvideAppJetTheme
import dev.lcdsmao.jettheme.ProvideJetTheme

@Composable
fun ProvideMaterialTheme(
  themeConfig: JetThemeConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideJetTheme<MaterialThemeSpec>(
    themeConfig = themeConfig,
    crossfadeAnimSpec = crossfadeAnimSpec,
  ) { themeSpec ->
    MaterialTheme(themeSpec, content)
  }
}

@Composable
fun ProvideAppMaterialTheme(
  theme: JetTheme,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideAppJetTheme<MaterialThemeSpec>(
    theme = theme,
    crossfadeAnimSpec = crossfadeAnimSpec,
  ) { themeSpec ->
    MaterialTheme(themeSpec, content)
  }
}

@Composable
fun MaterialTheme(
  themeSpec: MaterialThemeSpec,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colors = themeSpec.colors,
    typography = themeSpec.typography,
    shapes = themeSpec.shapes,
    content = content,
  )
}
