package dev.lcdsmao.jettheme.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.JetThemeControllerConfig
import dev.lcdsmao.jettheme.JetThemeSpecMap
import dev.lcdsmao.jettheme.ProvideJetTheme

@Composable
fun ProvideMaterialTheme(
  themeControllerConfig: JetThemeControllerConfig,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideJetTheme<MaterialThemeSpec>(
    themeControllerConfig = themeControllerConfig,
    crossfadeAnimSpec = crossfadeAnimSpec,
  ) { themeSpec ->
    MaterialTheme(
      colors = themeSpec.colors,
      typography = themeSpec.typography,
      shapes = themeSpec.shapes,
      content = content,
    )
  }
}

@Composable
fun ProvideAppMaterialTheme(
  themeSpecMap: JetThemeSpecMap,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideMaterialTheme(
    themeControllerConfig = JetThemeControllerConfig.Persistence(
      themeSpecMap = themeSpecMap
    ),
    crossfadeAnimSpec = crossfadeAnimSpec,
    content = content,
  )
}
