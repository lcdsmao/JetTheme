package dev.lcdsmao.jettheme.material

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack
import dev.lcdsmao.jettheme.ThemePackBuilder
import dev.lcdsmao.jettheme.ThemeSpec
import dev.lcdsmao.jettheme.buildThemePack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun buildMaterialTheme(
  block: ThemePackBuilder.() -> Unit,
): ThemePack {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return buildThemePack {
    block()
    transformer { id, spec, defaultSpec ->
      require(defaultSpec is MaterialThemeSpec) { "Require ${spec.id} to be a MaterialThemeSpec" }
      if (id == ThemeIds.Default) {
        defaultSpec
      } else {
        require(spec is PartMaterialThemeSpec) { "Require ${spec.id} to be a PartMaterialThemeSpec" }
        spec.toMaterialTheme(defaultSpec)
      }
    }
  }
}

fun ThemePackBuilder.defaultMaterialSpec(
  colors: Colors,
  typography: Typography,
  shapes: Shapes,
) = theme(
  MaterialThemeSpecImpl(
    id = ThemeIds.Default,
    colors = colors,
    typography = typography,
    shapes = shapes,
  )
)

fun ThemePackBuilder.materialSpec(
  id: String,
  colors: Colors? = null,
  typography: Typography? = null,
  shapes: Shapes? = null,
) = theme(
  PartMaterialThemeSpec(
    id = id,
    colors = colors,
    typography = typography,
    shapes = shapes,
  )
)

private class MaterialThemeSpecImpl(
  override val id: String,
  override val colors: Colors,
  override val typography: Typography,
  override val shapes: Shapes,
) : MaterialThemeSpec

private class PartMaterialThemeSpec(
  override val id: String,
  val colors: Colors?,
  val typography: Typography?,
  val shapes: Shapes?,
) : ThemeSpec {
  fun toMaterialTheme(defaultTheme: MaterialThemeSpec): MaterialThemeSpec {
    return MaterialThemeSpecImpl(
      id = id,
      colors = colors ?: defaultTheme.colors,
      typography = typography ?: defaultTheme.typography,
      shapes = shapes ?: defaultTheme.shapes,
    )
  }
}
