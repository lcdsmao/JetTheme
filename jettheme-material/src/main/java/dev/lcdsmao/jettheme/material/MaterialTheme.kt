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

/**
 * Construct a new material design [ThemePack].
 */
@OptIn(ExperimentalContracts::class)
fun buildMaterialThemePack(
  block: ThemePackBuilder<MaterialThemeSpec>.() -> Unit,
): ThemePack<MaterialThemeSpec> {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return buildThemePack {
    block()
    transformer { spec, defaultSpec ->
      require(defaultSpec is MaterialThemeSpec) { "Require ${spec.id} to be a MaterialThemeSpec" }
      require(spec is PartialMaterialThemeSpec) { "Require ${spec.id} to be a PartMaterialThemeSpec" }
      spec.toMaterialTheme(defaultSpec)
    }
  }
}

/**
 * Add a default [MaterialThemeSpec] to this [ThemePack].
 */
fun ThemePackBuilder<MaterialThemeSpec>.defaultMaterialTheme(
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

/**
 * Add a new [MaterialThemeSpec] to this [ThemePack].
 */
fun ThemePackBuilder<MaterialThemeSpec>.materialTheme(
  id: String,
  colors: Colors? = null,
  typography: Typography? = null,
  shapes: Shapes? = null,
) = theme(
  PartialMaterialThemeSpec(
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

private class PartialMaterialThemeSpec(
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
