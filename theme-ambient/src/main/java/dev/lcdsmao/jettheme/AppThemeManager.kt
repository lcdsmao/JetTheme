package dev.lcdsmao.jettheme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppThemeManager(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: AppThemeDataStore,
  private val themeDataMap: MaterialThemeDataMap,
) {

  internal val themeFlow: StateFlow<MaterialThemeData?> = themeDataStore.themeIdFlow
    .map { id -> themeDataMap[id] ?: themeDataMap.default }
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, null)

  val theme: MaterialThemeData?
    get() = themeFlow.value

  fun setTheme(themeId: String) {
    coroutineScope.launch {
      themeDataStore.setThemeId(themeId)
    }
  }
}
