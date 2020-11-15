package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack
import dev.lcdsmao.jettheme.ThemeSpec
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
internal fun rememberInMemoryThemeController(
  config: ThemeConfig.InMemory,
): ThemeController {
  return remember(config) {
    InMemoryThemeController(
      themePack = config.themePack,
      initialThemeSpecId = config.initialThemeId,
    )
  }
}

internal class InMemoryThemeController(
  private val themePack: ThemePack,
  initialThemeSpecId: String,
) : ThemeController {

  override val themeFlow: MutableStateFlow<ThemeSpec> = MutableStateFlow(
    themePack[initialThemeSpecId]
  )

  override val themeId: String
    get() = themeFlow.value.id

  override fun setThemeId(id: String) {
    checkCanSetSpecId(themePack, id)
    check(id != ThemeIds.SystemSettings) {
      "InMemoryThemeController does not support id $id."
    }
    themeFlow.value = themePack[id]
  }
}
