package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.cast

@Stable
interface JetThemeController {

  val themeSpecFlow: Flow<JetThemeSpec>

  val themeId: String

  fun setThemeId(themeId: String)
}

@Immutable
sealed class JetThemeControllerConfig {

  @Immutable
  data class Persistence(
    val themeSpecMap: JetThemeSpecMap,
    val persistenceKey: String? = null,
    val darkModeThemeId: String = JetThemeIds.Dark,
  ) : JetThemeControllerConfig()

  @Immutable
  data class InMemory(
    val themeSpecMap: JetThemeSpecMap,
    val initialThemeId: String = JetThemeIds.Default,
  ) : JetThemeControllerConfig()
}

@PublishedApi
@Composable
internal fun JetThemeController(
  config: JetThemeControllerConfig,
): JetThemeController = when (config) {
  is JetThemeControllerConfig.Persistence -> {
    PersistentJetThemeController(
      themeSpecMap = config.themeSpecMap,
      key = config.persistenceKey,
      darkModeThemeId = config.darkModeThemeId,
    )
  }
  is JetThemeControllerConfig.InMemory -> {
    InMemoryJetThemeController(
      themeSpecMap = config.themeSpecMap,
      initialThemeId = config.initialThemeId,
    )
  }
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
