package dev.lcdsmao.jettheme.material

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MaterialThemeProviderTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun testMaterialTheme() {
    val expectColors = lightColors(
      primary = Color(45, 45, 45),
      background = Color(128, 128, 128),
    )
    val expectTypography = Typography(h1 = TextStyle(
      fontSize = 108.sp,
    ))
    val expectShapes = Shapes(small = CutCornerShape(2.dp))

    val materialTheme = buildMaterialThemePack {
      defaultMaterialTheme(
        colors = expectColors,
        typography = expectTypography,
        shapes = expectShapes,
      )
    }

    lateinit var colors: Colors
    lateinit var typography: Typography
    lateinit var shapes: Shapes
    composeRule.setContent {
      ProvideAppMaterialTheme(themePack = materialTheme) {
        colors = MaterialTheme.colors
        typography = MaterialTheme.typography
        shapes = MaterialTheme.shapes
      }
    }

    composeRule.waitForIdle()
    assertEquals(expectColors.primary, colors.primary)
    assertEquals(expectColors.background, colors.background)
    assertEquals(expectTypography.h1, typography.h1)
    assertEquals(expectShapes.small, shapes.small)
  }
}
