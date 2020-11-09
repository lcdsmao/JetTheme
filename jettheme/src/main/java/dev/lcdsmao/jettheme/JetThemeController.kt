package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.lcdsmao.jettheme.internal.PersistentJetThemeControllerImpl
import dev.lcdsmao.jettheme.internal.rememberInMemoryJetThemeController
import dev.lcdsmao.jettheme.internal.rememberPersistentJetThemeController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.cast

@Stable
interface JetThemeController {

  val themeSpecFlow: Flow<JetThemeSpec>

  val themeId: String

  fun setThemeId(themeId: String)
}

@PublishedApi
@Composable
internal fun rememberJetThemeController(
  config: JetThemeConfig,
): JetThemeController = when (config) {
  is JetThemeConfig.Persistence -> rememberPersistentJetThemeController(config)
  is JetThemeConfig.InMemory -> rememberInMemoryJetThemeController(config)
}

@Composable
inline fun <reified T : JetThemeSpec> JetThemeController.themeState(): State<T?> {
  return themeSpecFlow.map { T::class.cast(it) }.collectAsState(initial = null)
}

fun JetThemeController.setThemeBasedOnSystemSettings() {
  check(this is PersistentJetThemeControllerImpl) {
    "Only PersistenceJetThemeController supports change theme based on system settings."
  }
  setThemeId(JetThemeIds.SystemSettings)
}

operator fun JetThemeController.component1(): String = themeId

operator fun JetThemeController.component2(): (themeId: String) -> Unit = ::setThemeId
