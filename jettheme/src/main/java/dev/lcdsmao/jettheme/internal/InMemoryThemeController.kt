package dev.lcdsmao.jettheme.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.lcdsmao.jettheme.ThemeConfig
import dev.lcdsmao.jettheme.ThemeController
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack
import dev.lcdsmao.jettheme.ThemeSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
internal fun rememberInMemoryThemeController(
  config: ThemeConfig.InMemory,
): ThemeController {
  return remember(config) {
    InMemoryThemeController(
      themePack = config.themePack,
      initialThemeId = config.initialThemeId,
    )
  }
}

internal class InMemoryThemeController(
  private val themePack: ThemePack,
  initialThemeId: String,
) : ThemeController {

  private val _themeFlow: MutableStateFlow<ThemeSpec> = MutableStateFlow(themePack[initialThemeId])
  override val themeFlow: Flow<ThemeSpec> = _themeFlow.asStateFlow()

  private var _themeId: String by mutableStateOf(initialThemeId)
  override val themeId: String get() = _themeId

  override fun setThemeId(id: String) {
    checkCanSetSpecId(themePack, id)
    check(id != ThemeIds.SystemSettings) {
      "InMemoryThemeController does not support id $id."
    }
    _themeFlow.value = themePack[id]
    _themeId = _themeFlow.value.id
  }
}
