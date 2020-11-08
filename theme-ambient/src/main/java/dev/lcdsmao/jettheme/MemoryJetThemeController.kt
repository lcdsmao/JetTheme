package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Composable
internal fun InMemoryJetThemeController(
  themeSpecMap: JetThemeSpecMap,
  initialThemeId: String = JetThemeIds.Default,
): JetThemeController {
  return InMemoryJetThemeControllerImpl(
    themeSpecMap = themeSpecMap,
    initialThemeId = initialThemeId,
  )
}

private class InMemoryJetThemeControllerImpl(
  private val themeSpecMap: JetThemeSpecMap,
  initialThemeId: String,
) : JetThemeController {

  private val themeIdFlow: MutableStateFlow<String> = MutableStateFlow(initialThemeId)

  override val themeSpecFlow: Flow<JetThemeSpec> = themeIdFlow
    .map { id -> themeSpecMap[id] ?: themeSpecMap.default }

  override val themeId: String
    get() = themeIdFlow.value

  override fun setThemeId(themeId: String) {
    themeIdFlow.value = themeId
  }
}
