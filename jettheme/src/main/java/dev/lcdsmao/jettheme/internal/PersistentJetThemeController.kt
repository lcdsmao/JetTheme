package dev.lcdsmao.jettheme.internal

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ContextAmbient
import androidx.datastore.preferences.preferencesKey
import dev.lcdsmao.jettheme.JetTheme
import dev.lcdsmao.jettheme.JetThemeConfig
import dev.lcdsmao.jettheme.JetThemeController
import dev.lcdsmao.jettheme.JetThemeIds
import dev.lcdsmao.jettheme.JetThemeSpec
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
  val themeIdBasedOnSystem = if (isSystemInDarkTheme()) {
    JetThemeIds.Dark
  } else {
    config.darkModeThemeId
  }
  return remember(config, themeIdBasedOnSystem) {
    PersistentJetThemeControllerImpl(
      coroutineScope = coroutineScope,
      themeDataStore = JetThemeDataStore.get(context),
      theme = config.theme,
      themeIdBasedOnSystem = themeIdBasedOnSystem,
      dataStoreKey = config.persistenceKey,
    )
  }
}

internal class PersistentJetThemeControllerImpl(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: JetThemeDataStore,
  private val theme: JetTheme,
  private val themeIdBasedOnSystem: String,
  dataStoreKey: String?,
) : JetThemeController {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: JetThemeDataStore.AppThemeKey

  override val themeSpecFlow: SharedFlow<JetThemeSpec> = themeDataStore.themeIdFlow(key)
    .map { id ->
      val themeId = id ?: themeIdBasedOnSystem
      theme[themeId] ?: theme.default
    }
    .shareIn(coroutineScope, started = SharingStarted.Eagerly, replay = 1)

  override val themeId: String
    get() = themeSpecFlow.replayCache.firstOrNull()?.id ?: JetThemeIds.Default

  override fun setThemeId(themeId: String) {
    if (themeId == JetThemeIds.SystemSettings) {
      coroutineScope.launch {
        themeDataStore.clear(key)
      }
      return
    }

    check(themeId in theme) {
      "ThemeId $themeId does not existed in themeSpecMap $theme."
    }
    coroutineScope.launch {
      themeDataStore.setThemeId(key, themeId)
    }
  }
}
