package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.lcdsmao.jettheme.internal.rememberInMemoryThemeController
import dev.lcdsmao.jettheme.internal.rememberPersistentThemeController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

/**
 * [ThemeController] manages the theme for the component tree under the [ProvideTheme].
 *
 * @see ProvideTheme
 * @see ThemeControllerAmbient
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
  return themeFlow.filterIsInstance<T>().collectAsState(initial = initial)
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
 * Returns [ThemeController.themeId].
 */
operator fun ThemeController.component1(): String = themeId

/**
 * Returns [ThemeController.setThemeId] as a lambda.
 */
operator fun ThemeController.component2(): (themeId: String) -> Unit = ::setThemeId

@PublishedApi
@Composable
internal fun rememberThemeController(
  config: ThemeConfig,
): ThemeController = when (config) {
  is ThemeConfig.Persistence -> rememberPersistentThemeController(config)
  is ThemeConfig.InMemory -> rememberInMemoryThemeController(config)
}
