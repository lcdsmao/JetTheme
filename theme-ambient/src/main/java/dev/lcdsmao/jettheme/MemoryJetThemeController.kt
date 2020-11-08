package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow

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

  override val themeSpecFlow: MutableStateFlow<JetThemeSpec> = MutableStateFlow(
    themeSpecMap[initialThemeId] ?: themeSpecMap.default
  )

  override val themeId: String
    get() = themeSpecFlow.value.id

  override fun setThemeId(themeId: String) {
    check(themeId in themeSpecMap) {
      "ThemeId $themeId does not existed in themeSpecMap $themeSpecMap."
    }
    themeSpecFlow.value = themeSpecMap[themeId]!!
  }
}
