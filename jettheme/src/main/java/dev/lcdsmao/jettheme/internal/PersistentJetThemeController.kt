package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ContextAmbient
import androidx.datastore.preferences.preferencesKey
import dev.lcdsmao.jettheme.JetTheme
import dev.lcdsmao.jettheme.JetThemeConfig
import dev.lcdsmao.jettheme.JetThemeController
import dev.lcdsmao.jettheme.JetThemeSpec
import dev.lcdsmao.jettheme.JetThemeSpecIds
import dev.lcdsmao.jettheme.themeSpecIdBasedOnSystemSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Composable
internal fun rememberPersistentJetThemeController(
  config: JetThemeConfig.Persistence,
): JetThemeController {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  val themeSpecIdBasedOnSystem = themeSpecIdBasedOnSystemSettings(config.darkModeThemeSpecId)
  return remember(config, themeSpecIdBasedOnSystem) {
    PersistentJetThemeControllerImpl(
      coroutineScope = coroutineScope,
      themeDataStore = JetThemeDataStore.get(context),
      theme = config.theme,
      themeSpecIdBasedOnSystem = themeSpecIdBasedOnSystem,
      dataStoreKey = config.persistenceKey,
    )
  }
}

internal class PersistentJetThemeControllerImpl(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: JetThemeDataStore,
  private val theme: JetTheme,
  private val themeSpecIdBasedOnSystem: String,
  dataStoreKey: String?,
) : JetThemeController {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: JetThemeDataStore.AppThemeKey

  override val themeSpecFlow: SharedFlow<JetThemeSpec> = themeDataStore.themeSpecIdFlow(key)
    .map { id -> getTheme(id) }
    .shareIn(coroutineScope, started = SharingStarted.Eagerly, replay = 1)

  override val themeSpecId: String
    get() = themeSpecFlow.replayCache.firstOrNull()?.id ?: JetThemeSpecIds.Default

  override fun setThemeSpecId(id: String) {
    checkCanSetSpecId(theme, id)
    coroutineScope.launch {
      themeDataStore.setThemeSpecId(key, id)
    }
  }

  private fun getTheme(id: String?): JetThemeSpec {
    val specId = if (id == null || id == JetThemeSpecIds.SystemSettings) {
      themeSpecIdBasedOnSystem
    } else {
      id
    }
    return theme[specId]
  }
}
