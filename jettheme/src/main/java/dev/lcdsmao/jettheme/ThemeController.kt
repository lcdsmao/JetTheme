package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.lcdsmao.jettheme.internal.rememberInMemoryThemeController
import dev.lcdsmao.jettheme.internal.rememberPersistentThemeController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.safeCast

/**
 * [ThemeController] manages the theme for the component tree under the [ProvideTheme].
 *
 * @see ProvideTheme
 * @see AmbientThemeController
 */
interface ThemeController {

  /**
   * Gets current theme as a [Flow].
   */
  val themeFlow: Flow<ThemeSpec>

  /**
   * Gets the current theme id.
   */
  val themeId: String

  /**
   * Selects the theme that associated with the [id].
   * This can trigger a recompose.
   */
  fun setThemeId(id: String)
}

/**
 * Observes the [ThemeController.themeFlow] as a [State].
 * The state will only contain values that are instance of specified type [T].
 */
@Composable
inline fun <reified T : ThemeSpec> ThemeController.themeState(initial: T? = null): State<T?> {
  val themeFlow = themeFlow
  val initialValue = if (themeFlow is SharedFlow) {
    themeFlow.replayCache.firstOrNull()?.let { T::class.safeCast(it) } ?: initial
  } else {
    initial
  }
  return themeFlow.filterIsInstance<T>().collectAsState(initial = initialValue)
}

/**
 * Selects the theme with id [ThemeIds.Default].
 */
fun ThemeController.setDefaultTheme() {
  setThemeId(ThemeIds.Default)
}

/**
 * Selects the theme with id [ThemeIds.Dark].
 */
fun ThemeController.setDarkModeTheme() {
  setThemeId(ThemeIds.Dark)
}

/**
 * Selects the theme with id [ThemeIds.SystemSettings].
 */
fun ThemeController.setThemeBasedOnSystemSettings() {
  setThemeId(ThemeIds.SystemSettings)
}

/**
 * Returns a property delegate to set/get themeId.
 */
operator fun ThemeController.provideDelegate(
  thisRef: Any?,
  prop: KProperty<*>,
): ReadWriteProperty<Any?, String> = object : ReadWriteProperty<Any?, String> {

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
    setThemeId(value)
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): String {
    return themeId
  }
}

@PublishedApi
@Composable
internal fun rememberThemeController(
  config: ThemeConfig,
): ThemeController = when (config) {
  is ThemeConfig.Persistence -> rememberPersistentThemeController(config)
  is ThemeConfig.InMemory -> rememberInMemoryThemeController(config)
}
