package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ContextAmbient
import androidx.datastore.preferences.preferencesKey
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack
import dev.lcdsmao.jettheme.ThemeSpec
import dev.lcdsmao.jettheme.themeIdBasedOnSystemSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Composable
internal fun rememberPersistentThemeController(
  config: ThemeConfig.Persistence,
): ThemeController {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  val themeIdBasedOnSystem = themeIdBasedOnSystemSettings(config.darkModeThemeId)
  return remember(config, themeIdBasedOnSystem) {
    PersistentThemeController(
      coroutineScope = coroutineScope,
      themeDataStore = ThemeDataStore(context),
      themePack = config.themePack,
      themeIdBasedOnSystem = themeIdBasedOnSystem,
      dataStoreKey = config.persistenceKey,
    )
  }
}

internal class PersistentThemeController(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: ThemeDataStore,
  private val themePack: ThemePack,
  private val themeIdBasedOnSystem: String,
  dataStoreKey: String?,
) : ThemeController {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: ThemeDataStore.AppThemeKey

  override val themeFlow: SharedFlow<ThemeSpec> = themeDataStore.themeIdFlow(key)
    .map { id -> getTheme(id) }
    .shareIn(coroutineScope, started = SharingStarted.Eagerly, replay = 1)

  override val themeId: String
    get() = themeFlow.replayCache.firstOrNull()?.id ?: ThemeIds.Default

  override fun setThemeId(id: String) {
    checkCanSetSpecId(themePack, id)
    coroutineScope.launch {
      themeDataStore.setThemeId(key, id)
    }
  }

  private fun getTheme(id: String?): ThemeSpec {
    val specId = if (id == null || id == ThemeIds.SystemSettings) {
      themeIdBasedOnSystem
    } else {
      id
    }
    return themePack[specId]
  }
}
