package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.JetTheme
import dev.lcdsmao.jettheme.JetThemeConfig
import dev.lcdsmao.jettheme.JetThemeController
import dev.lcdsmao.jettheme.JetThemeSpec
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun rememberInMemoryJetThemeController(
  config: JetThemeConfig.InMemory,
): JetThemeController {
  return remember(config) {
    InMemoryJetThemeControllerImpl(
      theme = config.theme,
      initialThemeId = config.initialThemeId,
    )
  }
}

private class InMemoryJetThemeControllerImpl(
  private val theme: JetTheme,
  initialThemeId: String,
) : JetThemeController {

  override val themeSpecFlow: MutableStateFlow<JetThemeSpec> = MutableStateFlow(
    theme[initialThemeId] ?: theme.default
  )

  override val themeId: String
    get() = themeSpecFlow.value.id

  override fun setThemeId(themeId: String) {
    check(themeId in theme) {
      "ThemeId $themeId does not existed in themeSpecMap $theme."
    }
    themeSpecFlow.value = theme[themeId]!!
  }
}
