package dev.lcdsmao.jettheme.material

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Stable
import dev.lcdsmao.jettheme.JetThemeIds
import dev.lcdsmao.jettheme.JetThemeSpec
import dev.lcdsmao.jettheme.JetThemeSpecMapBuilder
import dev.lcdsmao.jettheme.buildJetThemes

@Stable
interface MaterialThemeSpec : JetThemeSpec {
  val colors: Colors
  val typography: Typography
  val shapes: Shapes
}

fun buildMaterialThemes(
  block: JetThemeSpecMapBuilder.() -> Unit,
) = buildJetThemes {
  block()
  transformer { id, spec, defaultSpec ->
    require(defaultSpec is MaterialThemeSpec) { "Require ${spec.id} to be a MaterialThemeSpec" }
    if (id == JetThemeIds.Default) {
      defaultSpec
    } else {
      require(spec is PartMaterialThemeSpec) { "Require ${spec.id} to be a PartMaterialThemeSpec" }
      spec.toMaterialThemeData(defaultSpec)
    }
  }
}

fun JetThemeSpecMapBuilder.defaultTheme(
  colors: Colors,
  typography: Typography,
  shapes: Shapes,
) = theme(
  MaterialThemeSpecImpl(
    id = JetThemeIds.Default,
    colors = colors,
    typography = typography,
    shapes = shapes,
  )
)

fun JetThemeSpecMapBuilder.theme(
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
) : JetThemeSpec {
  fun toMaterialThemeData(defaultTheme: MaterialThemeSpec): MaterialThemeSpec {
    return MaterialThemeSpecImpl(
      id = id,
      colors = colors ?: defaultTheme.colors,
      typography = typography ?: defaultTheme.typography,
      shapes = shapes ?: defaultTheme.shapes,
    )
  }
}
