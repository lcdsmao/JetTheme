package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Immutable
data class ThemePack internal constructor(
  private val themeMap: Map<String, ThemeSpec>,
) {

  @Stable
  val default: ThemeSpec
    get() = requireNotNull(themeMap[ThemeIds.Default])

  @Stable
  val size: Int
    get() = themeMap.size

  @Stable
  operator fun get(specId: String): ThemeSpec = themeMap[specId] ?: default

  @Stable
  operator fun contains(specId: String): Boolean = specId in themeMap

  @Stable
  fun toList(): List<Pair<String, ThemeSpec>> = themeMap.toList()

  @Stable
  override fun toString(): String = "ThemePack: $themeMap"
}

fun ThemePack.nextThemeId(id: String): String {
  val entries = toList()
  val index = entries.indexOfFirst { it.first == id }
  return when {
    index < 0 || index + 1 >= size -> entries.first().first
    else -> entries[index + 1].first
  }
}

@OptIn(ExperimentalContracts::class)
fun buildThemePack(
  block: ThemePackBuilder.() -> Unit,
): ThemePack {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return ThemePackBuilder().apply(block).build()
}

private typealias ThemeSpecTransformer = (
  id: String,
  spec: ThemeSpec,
  defaultSpec: ThemeSpec,
) -> ThemeSpec

class ThemePackBuilder internal constructor() {

  private val themes = mutableListOf<ThemeSpec>()

  private var transformer: ThemeSpecTransformer? = null

  fun theme(theme: ThemeSpec) {
    themes += theme
  }

  fun transformer(f: ThemeSpecTransformer) {
    transformer = f
  }

  internal fun build(): ThemePack {
    val themeMap = themes.associateBy { it.id }
    check(ThemeIds.Default in themeMap) {
      "Must provide a default theme spec using with id ${ThemeIds.Default}."
    }
    check(themeMap.size == themes.size) {
      "Provided theme specs has duplication: ${themes.map { it.id }}."
    }
    val defaultSpec = requireNotNull(themeMap[ThemeIds.Default])
    val transformedMap = transformer?.let { f ->
      themeMap.mapValues { (id, spec) -> f(id, spec, defaultSpec) }
    } ?: themeMap
    return ThemePack(transformedMap)
  }
}

@Suppress("unused")
val ThemePackBuilder.defaultId
  get() = ThemeIds.Default

@Suppress("unused")
val ThemePackBuilder.darkId
  get() = ThemeIds.Dark
