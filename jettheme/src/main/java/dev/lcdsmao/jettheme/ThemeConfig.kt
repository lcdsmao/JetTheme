package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
fun ThemePack.persistenceConfig(
  persistenceKey: String? = null,
  darkModeThemeId: String = ThemeIds.Dark,
) = ThemeConfig.Persistence(
  themePack = this,
  persistenceKey = persistenceKey,
  darkModeThemeId = darkModeThemeId,
)

@Stable
fun ThemePack.inMemoryConfig(
  initialThemeSpecId: String = ThemeIds.Default,
) = ThemeConfig.InMemory(
  themePack = this,
  initialThemeId = initialThemeSpecId,
)

@Immutable
sealed class ThemeConfig {

  abstract val themePack: ThemePack

  @Immutable
  data class Persistence(
    override val themePack: ThemePack,
    val persistenceKey: String? = null,
    val darkModeThemeId: String = ThemeIds.Dark,
  ) : ThemeConfig()

  @Immutable
  data class InMemory(
    override val themePack: ThemePack,
    val initialThemeId: String = ThemeIds.Default,
  ) : ThemeConfig()
}
