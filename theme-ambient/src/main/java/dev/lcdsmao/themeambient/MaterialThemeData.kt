package dev.lcdsmao.themeambient

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Stable

fun buildMaterialThemeDataMap(
  block: MaterialThemeDataMapBuilder.() -> Unit,
): MaterialThemeDataMap {
  val map = MaterialThemeDataMapBuilder().apply(block).build()
  return MaterialThemeDataMap(map)
}

@Stable
class MaterialThemeData internal constructor(
  val themeId: String,
  val themeName: String?,
  val colors: Colors,
  val typography: Typography,
  val shapes: Shapes,
) {
  companion object {
    const val DefaultThemeId = "default"
  }
}

@Stable
class MaterialThemeDataMap internal constructor(
  map: Map<String, MaterialThemeData>,
) : Map<String, MaterialThemeData> by map {

  val default: MaterialThemeData
    get() = requireNotNull(this[MaterialThemeData.DefaultThemeId])
}

class MaterialThemeDataMapBuilder internal constructor() {

  private val rawThemes = mutableListOf<RawMaterialThemeData>()

  fun defaultTheme(
    colors: Colors,
    typography: Typography,
    shapes: Shapes,
    themeName: String? = null,
  ) {
    rawThemes += RawMaterialThemeData(
      themeId = MaterialThemeData.DefaultThemeId,
      themeName = themeName,
      colors = colors,
      typography = typography,
      shapes = shapes,
    )
  }

  fun theme(
    themeId: String,
    themeName: String? = null,
    colors: Colors? = null,
    typography: Typography? = null,
    shapes: Shapes? = null,
  ) {
    check(themeId != MaterialThemeData.DefaultThemeId) {
      "Use method defaultTheme to provide the default theme data."
    }
    rawThemes += RawMaterialThemeData(
      themeId = themeId,
      themeName = themeName,
      colors = colors,
      typography = typography,
      shapes = shapes,
    )
  }

  internal fun build(): Map<String, MaterialThemeData> {
    val rawMap = rawThemes.associateBy { it.themeId }.toMutableMap()
    check(MaterialThemeData.DefaultThemeId in rawMap) {
      "Must provide a default theme data using method defaultTheme."
    }
    check(rawMap.size == rawThemes.size) {
      "Provided theme data contain duplicate ids: ${rawThemes.map { it.themeId }}."
    }
    val rawDefaultTheme = requireNotNull(rawMap[MaterialThemeData.DefaultThemeId])
    val defaultTheme = MaterialThemeData(
      themeId = rawDefaultTheme.themeId,
      themeName = rawDefaultTheme.themeName,
      colors = requireNotNull(rawDefaultTheme.colors),
      typography = requireNotNull(rawDefaultTheme.typography),
      shapes = requireNotNull(rawDefaultTheme.shapes),
    )
    return rawMap.mapValues { (id, theme) ->
      if (id == MaterialThemeData.DefaultThemeId) {
        defaultTheme
      } else {
        theme.toMaterialThemeData(defaultTheme)
      }
    }
  }
}

private class RawMaterialThemeData(
  val themeId: String,
  val themeName: String?,
  val colors: Colors?,
  val typography: Typography?,
  val shapes: Shapes?,
) {
  fun toMaterialThemeData(defaultTheme: MaterialThemeData): MaterialThemeData {
    return MaterialThemeData(
      themeId = themeId,
      themeName = themeName,
      colors = colors ?: defaultTheme.colors,
      typography = typography ?: defaultTheme.typography,
      shapes = shapes ?: defaultTheme.shapes,
    )
  }
}
