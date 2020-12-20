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
internal fun <T : ThemeSpec> rememberInMemoryThemeController(
  config: ThemeConfig.InMemory<T>,
): ThemeController<T> {
  return remember(config) {
    InMemoryThemeController<T>(
      themePack = config.themePack,
      initialThemeId = config.initialThemeId,
    )
  }
}

internal class InMemoryThemeController<T : ThemeSpec>(
  private val themePack: ThemePack<T>,
  initialThemeId: String,
) : ThemeController<T> {

  private val _themeFlow: MutableStateFlow<T> = MutableStateFlow(themePack[initialThemeId])
  override val themeFlow: Flow<T> = _themeFlow.asStateFlow()

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
