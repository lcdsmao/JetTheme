package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.preferencesKey
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack
import dev.lcdsmao.jettheme.ThemeSpec
import dev.lcdsmao.jettheme.themeIdBasedOnSystemSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Composable
internal fun <T : ThemeSpec> rememberPersistentThemeController(
  config: ThemeConfig.Persistence<T>,
): ThemeController<T> {
  val context = LocalContext.current
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

internal class PersistentThemeController<T : ThemeSpec>(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: ThemeDataStore,
  private val themePack: ThemePack<T>,
  private val themeIdBasedOnSystem: String,
  dataStoreKey: String?,
) : ThemeController<T> {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: ThemeDataStore.AppThemeKey

  private var _themeId: String by mutableStateOf(ThemeIds.Default)
  override val themeId: String get() = _themeId

  override val themeFlow: Flow<T> = themeDataStore.themeIdFlow(key)
    .map { id -> getTheme(id) }
    .onEach { _themeId = it.id }
    .shareIn(coroutineScope, started = SharingStarted.Eagerly, replay = 1)

  override fun setThemeId(id: String) {
    checkCanSetSpecId(themePack, id)
    coroutineScope.launch {
      themeDataStore.setThemeId(key, id)
    }
  }

  private fun getTheme(id: String?): T {
    val specId = if (id == null || id == ThemeIds.SystemSettings) {
      themeIdBasedOnSystem
    } else {
      id
    }
    return themePack[specId]
  }
}
