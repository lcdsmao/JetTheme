package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class JetTheme internal constructor(
  private val themeSpecMap: Map<String, JetThemeSpec>,
) {

  @Stable
  val default: JetThemeSpec
    get() = requireNotNull(this[JetThemeIds.Default])

  @Stable
  val size: Int
    get() = themeSpecMap.size

  @Stable
  operator fun get(key: String): JetThemeSpec? = themeSpecMap[key]

  @Stable
  operator fun contains(key: String): Boolean = key in themeSpecMap

  @Stable
  fun toList(): List<Pair<String, JetThemeSpec>> = themeSpecMap.toList()
}

fun JetTheme.nextThemeId(themeId: String): String {
  val entries = toList()
  val index = entries.indexOfFirst { it.first == themeId }
  return when {
    index < 0 || index + 1 >= size -> entries.first().first
    else -> entries[index + 1].first
  }
}

fun buildJetTheme(
  block: JetThemeSpecMapBuilder.() -> Unit,
): JetTheme {
  return JetThemeSpecMapBuilder().apply(block).build()
}

private typealias ThemeSpecTransformer = (
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

  internal fun build(): JetTheme {
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
    return JetTheme(transformedMap)
  }
}
