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
      initialThemeSpecId = config.initialThemeSpecId,
    )
  }
}

private class InMemoryJetThemeControllerImpl(
  private val theme: JetTheme,
  initialThemeSpecId: String,
) : JetThemeController {

  override val themeSpecFlow: MutableStateFlow<JetThemeSpec> = MutableStateFlow(
    theme[initialThemeSpecId]
  )

  override val themeSpecId: String
    get() = themeSpecFlow.value.id

  override fun setThemeSpecId(id: String) {
    checkCanSetSpecId(theme, id)
    themeSpecFlow.value = theme[id]
  }
}
