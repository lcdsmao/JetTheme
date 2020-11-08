package dev.lcdsmao.jettheme.material

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.JetThemeSpecMap
import dev.lcdsmao.jettheme.ProvideJetTheme

@Composable
fun ProvideMaterialTheme(
  themeSpecMap: JetThemeSpecMap,
  crossfadeAnimSpec: AnimationSpec<Float> = remember { tween() },
  content: @Composable () -> Unit,
) {
  ProvideJetTheme<MaterialThemeSpec>(
    themeSpecMap = themeSpecMap,
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
