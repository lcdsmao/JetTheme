/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Forked from: https://github.com/android/compose-samples/
 */

package dev.lcdsmao.jettheme.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.lcdsmao.jettheme.ProvideAppTheme
import dev.lcdsmao.jettheme.ThemeAmbient
import dev.lcdsmao.jettheme.ThemeSpec
import dev.lcdsmao.jettheme.buildThemePack
import dev.lcdsmao.jettheme.component1
import dev.lcdsmao.jettheme.component2
import dev.lcdsmao.jettheme.darkId
import dev.lcdsmao.jettheme.defaultId
import dev.lcdsmao.jettheme.nextThemeId

private val LightColorPalette = SimpleColors(
  background = Color.White,
  text = Color.Black,
  isDark = false
)

private val DarkColorPalette = SimpleColors(
  background = Color.Black,
  text = Color.White,
  isDark = true
)

@Stable
data class SimpleThemeSpec(
  override val id: String,
  val colors: SimpleColors,
  val typography: Typography = Typography(),
  val shapes: Shapes = Shapes(),
) : ThemeSpec

private val AppTheme = buildThemePack {
  theme(
    SimpleThemeSpec(
      id = defaultId,
      colors = LightColorPalette,
    )
  )
  theme(
    SimpleThemeSpec(
      id = darkId,
      colors = DarkColorPalette,
    )
  )
}

@Composable
fun CustomDesignSystemApp() {
  ProvideSimpleTheme {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(SimpleTheme.colors.background)
        .padding(32.dp),
    ) {
      val (themeId, setThemeId) = ThemeAmbient.current
      Text(
        "Custom Design System",
        modifier = Modifier
          .align(Alignment.Center)
          .clickable(onClick = { setThemeId(AppTheme.nextThemeId(themeId)) }),
        color = SimpleTheme.colors.text,
        style = SimpleTheme.typography.h3,
      )
    }
  }
}

@Composable
private fun ProvideSimpleTheme(
  content: @Composable () -> Unit,
) {
  ProvideAppTheme<SimpleThemeSpec>(
    themePack = AppTheme,
  ) { theme ->
    val colorPalette = remember { theme.colors }
    colorPalette.update(theme.colors)
    Providers(AmbientSimpleColors provides colorPalette) {
      MaterialTheme(
        colors = debugColors(theme.colors.isDark),
        typography = theme.typography,
        shapes = theme.shapes,
        content = content,
      )
    }
  }
}

object SimpleTheme {
  @Composable
  val colors: SimpleColors
    get() = AmbientSimpleColors.current

  @Composable
  val typography: Typography
    get() = MaterialTheme.typography
}

@Stable
class SimpleColors(
  background: Color,
  text: Color,
  isDark: Boolean,
) {
  var background by mutableStateOf(background)
    private set
  var text by mutableStateOf(text)
    private set
  var isDark by mutableStateOf(isDark)
    private set

  fun update(other: SimpleColors) {
    background = other.background
    text = other.text
    isDark = other.isDark
  }
}

private val AmbientSimpleColors = staticAmbientOf<SimpleColors>()

private fun debugColors(
  darkTheme: Boolean,
  debugColor: Color = Color.Magenta,
) = Colors(
  primary = debugColor,
  primaryVariant = debugColor,
  secondary = debugColor,
  secondaryVariant = debugColor,
  background = debugColor,
  surface = debugColor,
  error = debugColor,
  onPrimary = debugColor,
  onSecondary = debugColor,
  onBackground = debugColor,
  onSurface = debugColor,
  onError = debugColor,
  isLight = !darkTheme
)
