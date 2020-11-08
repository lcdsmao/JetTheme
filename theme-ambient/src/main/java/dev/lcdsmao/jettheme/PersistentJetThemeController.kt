package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ContextAmbient
import androidx.datastore.preferences.preferencesKey
import dev.lcdsmao.jettheme.internal.JetThemeDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
internal fun PersistentJetThemeController(
  themeSpecMap: JetThemeSpecMap,
  key: String? = null,
): JetThemeController {
  val context = ContextAmbient.current
  val coroutineScope = rememberCoroutineScope()
  return PersistentJetThemeControllerImpl(
    coroutineScope = coroutineScope,
    themeDataStore = JetThemeDataStore.get(context),
    themeSpecMap = themeSpecMap,
    dataStoreKey = key,
  )
}

private class PersistentJetThemeControllerImpl(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: JetThemeDataStore,
  private val themeSpecMap: JetThemeSpecMap,
  dataStoreKey: String?,
) : JetThemeController {

  private val key = dataStoreKey?.let { preferencesKey(it) }
    ?: JetThemeDataStore.AppThemeKey

  private val themeIdFlow: StateFlow<String> = themeDataStore.themeIdFlow(key)
    .filterNotNull()
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, JetThemeIds.Default)

  override val themeSpecFlow: Flow<JetThemeSpec> = themeDataStore.themeIdFlow(key)
    .map { id ->
      if (id == null) themeSpecMap.default
      else themeSpecMap[id] ?: themeSpecMap.default
    }

  override val themeId: String
    get() = themeIdFlow.value

  override fun setThemeId(themeId: String) {
    check(themeId in themeSpecMap) {
      "ThemeId $themeId not in themeSpecMap $themeSpecMap."
    }
    coroutineScope.launch {
      themeDataStore.setThemeId(key, themeId)
    }
  }
}
