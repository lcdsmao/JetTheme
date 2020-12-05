package dev.lcdsmao.jettheme.material

import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.darkId
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs

class MaterialThemeSpecTest : StringSpec({
  "Build theme success" {
    val expectLightColors = lightColors()
    val expectDarkColors = darkColors()
    val expectTypography = Typography()
    val expectShapes = Shapes()
    val themePack = buildMaterialThemePack {
      defaultMaterialTheme(
        colors = expectLightColors,
        typography = expectTypography,
        shapes = expectShapes,
      )
      materialTheme(
        darkId,
        colors = expectDarkColors,
      )
    }

    themePack.size shouldBe 2

    themePack.default should instanceOf(MaterialThemeSpec::class)
    with(themePack.default as MaterialThemeSpec) {
      colors shouldBeSameInstanceAs expectLightColors
      typography shouldBeSameInstanceAs expectTypography
      shapes shouldBeSameInstanceAs expectShapes
    }

    themePack[ThemeIds.Dark] should instanceOf(MaterialThemeSpec::class)
    with(themePack[ThemeIds.Dark] as MaterialThemeSpec) {
      colors shouldBeSameInstanceAs expectDarkColors
      typography shouldBeSameInstanceAs expectTypography
      shapes shouldBeSameInstanceAs expectShapes
    }
  }

  "Build theme without default id should fail" {
    shouldThrowAny {
      buildMaterialThemePack {
        materialTheme("id")
      }
    }
  }
})
