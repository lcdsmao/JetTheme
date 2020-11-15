package dev.lcdsmao.jettheme.material

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Stable
import dev.lcdsmao.jettheme.ThemeSpec

/**
 * Material design specification.
 *
 * @see androidx.compose.material.MaterialTheme
 */
@Stable
interface MaterialThemeSpec : ThemeSpec {
  val colors: Colors
  val typography: Typography
  val shapes: Shapes
}
