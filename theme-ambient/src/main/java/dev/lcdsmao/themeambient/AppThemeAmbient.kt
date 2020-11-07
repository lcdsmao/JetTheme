package dev.lcdsmao.themeambient

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient

val AppThemeAmbient = staticAmbientOf<AppThemeManager>()

@Composable
fun ProvideAppTheme(
  themeDataMap: MaterialThemeDataMap,
  animSpec: AnimationSpec<Color> = remember { spring() },
  children: @Composable () -> Unit,
) {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  val themeManager = remember(themeDataMap) {
    AppThemeManager(
      coroutineScope = coroutineScope,
      themeDataStore = AppThemeDataStore.get(context),
      themeDataMap = themeDataMap,
    )
  }

  Providers(AppThemeAmbient provides themeManager) {
    WrapMaterialTheme(animSpec, children)
  }
}

@Composable
private fun WrapMaterialTheme(
  themeColorAnimSpec: AnimationSpec<Color> = remember { spring() },
  children: @Composable () -> Unit,
) {
  val themeManager = AppThemeAmbient.current
  val theme = themeManager.themeFlow.collectAsState(initial = null).value

  if (theme != null) {
    MaterialTheme(
      colors = animate(theme.colors, themeColorAnimSpec),
      typography = theme.typography,
      shapes = theme.shapes,
      children,
    )
  }
}
