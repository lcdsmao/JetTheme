package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.lcdsmao.jettheme.internal.rememberInMemoryThemeController
import dev.lcdsmao.jettheme.internal.rememberPersistentThemeController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.cast

@Stable
interface ThemeController {

  val themeFlow: Flow<ThemeSpec>

  val themeId: String

  fun setThemeId(id: String)
}

@PublishedApi
@Composable
internal fun rememberThemeController(
  config: ThemeConfig,
): ThemeController = when (config) {
  is ThemeConfig.Persistence -> rememberPersistentThemeController(config)
  is ThemeConfig.InMemory -> rememberInMemoryThemeController(config)
}

@Composable
inline fun <reified T : ThemeSpec> ThemeController.themeState(): State<T?> {
  return themeFlow.map { T::class.cast(it) }.collectAsState(initial = null)
}

fun ThemeController.setDefaultTheme() {
  setThemeId(ThemeIds.Default)
}

fun ThemeController.setDarkModeTheme() {
  setThemeId(ThemeIds.Dark)
}

fun ThemeController.setThemeBasedOnSystemSettings() {
  setThemeId(ThemeIds.SystemSettings)
}

operator fun ThemeController.component1(): String = themeId

operator fun ThemeController.component2(): (themeId: String) -> Unit = ::setThemeId
