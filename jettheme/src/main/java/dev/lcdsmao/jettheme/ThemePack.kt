package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

/**
 * Defines the available themes for an app or a subcomponent within the app.
 */
@Immutable
data class ThemePack<T : ThemeSpec> internal constructor(
  private val themeMap: Map<String, T>,
) {

  /**
   * The theme with id [ThemeIds.Dark] in this [ThemePack].
   */
  @Stable
  val default: T
    get() = requireNotNull(themeMap[ThemeIds.Default])

  /**
   * The size of available themes.
   */
  @Stable
  val size: Int
    get() = themeMap.size

  /**
   * Returns the theme that associated with the [themeId].
   * If the [themeId] does not existed in this [ThemePack], returns [default] theme.
   */
  @Stable
  operator fun get(themeId: String): T = themeMap[themeId] ?: default

  /**
   * Check the [themeId] existed in this [ThemePack] or not.
   */
  @Stable
  operator fun contains(themeId: String): Boolean = themeId in themeMap

  /**
   * Convert the available themes into a list of theme id and [ThemeSpec] pairs.
   */
  @Stable
  fun toList(): List<Pair<String, T>> = themeMap.toList()

  @Stable
  override fun toString(): String = "ThemePack: $themeMap"
}

/**
 * Convenient method for retrieving next theme id after [id] in [this] ThemePack.
 * If the [id] is the last theme id, then returns the first theme id.
 */
fun ThemePack<*>.nextThemeId(id: String): String {
  val entries = toList()
  val index = entries.indexOfFirst { it.first == id }
  return when {
    index < 0 || index + 1 >= size -> entries.first().first
    else -> entries[index + 1].first
  }
}

/**
 * Construct a new [ThemePack].
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T : ThemeSpec> buildThemePack(
  block: ThemePackBuilder<T>.() -> Unit,
): ThemePack<T> {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return ThemePackBuilder(T::class).apply(block).build()
}

/**
 * DSL for constructing a new [ThemePack].
 */
class ThemePackBuilder<T : ThemeSpec> @PublishedApi internal constructor(
  private val kClass: KClass<T>,
) {

  private val themes = mutableListOf<ThemeSpec>()

  private var transformer: ThemeSpecTransformer<T>? = null

  /**
   * Add a new [ThemeSpec] to this [ThemePack].
   * Must add at least a theme with id [ThemeIds.Default].
   */
  fun theme(theme: ThemeSpec) {
    themes += theme
  }

  /**
   * Optional transformer for all added [ThemeSpec] expect the theme with id [ThemeIds.Default].
   * The transformer [f] will be invoked before constructing the [ThemePack].
   */
  fun transformer(f: ThemeSpecTransformer<T>) {
    transformer = f
  }

  @PublishedApi
  internal fun build(): ThemePack<T> {
    val defaultSpec = themes.find { it.id == ThemeIds.Default }
    check(defaultSpec != null) {
      "Must provide a default theme with id ${ThemeIds.Default}."
    }
    val transformedList = transformer?.let { f ->
      themes.map { spec ->
        if (spec.id != ThemeIds.Default) f(spec, defaultSpec) else spec
      }
    } ?: themes
    val themeMap = transformedList.filterIsInstance(kClass.java).associateBy { it.id }
    check(ThemeIds.SystemSettings !in themeMap) {
      "Id ${ThemeIds.SystemSettings} should not be used in a real theme."
    }
    check(themeMap.size == themes.size) {
      "Provided themes has duplication: ${themes.map { it.id }}."
    }
    return ThemePack(themeMap)
  }
}

/**
 * Returns the [ThemeIds.Default].
 */
@Suppress("unused")
val ThemePackBuilder<*>.defaultId
  get() = ThemeIds.Default

/**
 * Returns the [ThemeIds.Dark].
 */
@Suppress("unused")
val ThemePackBuilder<*>.darkId
  get() = ThemeIds.Dark

private typealias ThemeSpecTransformer<T> = (
  theme: ThemeSpec,
  defaultTheme: ThemeSpec,
) -> T
