package dev.lcdsmao.jettheme

import androidx.compose.runtime.Stable

@Stable
interface JetThemeSpec {
  val id: String
}

object JetThemeIds {
  const val Default = "default"
}

@Stable
class JetThemeSpecMap internal constructor(
  map: Map<String, JetThemeSpec>,
) : Map<String, JetThemeSpec> by map {

  val default: JetThemeSpec
    get() = requireNotNull(this[JetThemeIds.Default])
}

fun buildJetThemes(
  block: JetThemeSpecMapBuilder.() -> Unit,
): JetThemeSpecMap {
  return JetThemeSpecMapBuilder().apply(block).build()
}

typealias ThemeSpecTransformer = (
  id: String,
  spec: JetThemeSpec,
  defaultSpec: JetThemeSpec,
) -> JetThemeSpec

class JetThemeSpecMapBuilder internal constructor() {

  private val themes = mutableListOf<JetThemeSpec>()

  private var transformer: ThemeSpecTransformer? = null

  fun theme(themeSpec: JetThemeSpec) {
    themes += themeSpec
  }

  fun transformer(f: ThemeSpecTransformer) {
    transformer = f
  }

  internal fun build(): JetThemeSpecMap {
    val themeMap = themes.associateBy { it.id }
    check(JetThemeIds.Default in themeMap) {
      "Must provide a default theme using with id ${JetThemeIds.Default}."
    }
    check(themeMap.size == themes.size) {
      "Provided theme data contain duplicate ids: ${themes.map { it.id }}."
    }
    val defaultSpec = requireNotNull(themeMap[JetThemeIds.Default])
    val transformedMap = transformer?.let { f ->
      themeMap.mapValues { (id, spec) -> f(id, spec, defaultSpec) }
    } ?: themeMap
    return JetThemeSpecMap(transformedMap)
  }
}
