package dev.lcdsmao.jettheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ContextAmbient
import androidx.datastore.preferences.preferencesKey
import dev.lcdsmao.jettheme.internal.JetThemeDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@Composable
internal fun PersistentJetThemeController(
  themeSpecMap: JetThemeSpecMap,
  key: String? = null,
): JetThemeController {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  val themeIdBasedOnSystem = if (isSystemInDarkTheme()) {
    JetThemeIds.Dark
  } else {
    JetThemeIds.Default
  }
  return PersistentJetThemeControllerImpl(
    coroutineScope = coroutineScope,
    themeDataStore = JetThemeDataStore.get(context),
    themeSpecMap = themeSpecMap,
    themeIdBasedOnSystem = themeIdBasedOnSystem,
    dataStoreKey = key,
  )
}

internal class PersistentJetThemeControllerImpl(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: JetThemeDataStore,
  private val themeSpecMap: JetThemeSpecMap,
  private val themeIdBasedOnSystem: String,
  dataStoreKey: String?,
) : JetThemeController {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: JetThemeDataStore.AppThemeKey

  override val themeSpecFlow: SharedFlow<JetThemeSpec> = themeDataStore.themeIdFlow(key)
    .map { id ->
      val themeId = id ?: themeIdBasedOnSystem
      themeSpecMap[themeId] ?: themeSpecMap.default
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

    check(themeId in themeSpecMap) {
      "ThemeId $themeId does not existed in themeSpecMap $themeSpecMap."
    }
    coroutineScope.launch {
      themeDataStore.setThemeId(key, themeId)
    }
  }
}
