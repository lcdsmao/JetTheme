package dev.lcdsmao.jettheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Construct a [ThemeConfig.Persistence].
 */
@Stable
fun <T : ThemeSpec> ThemePack<T>.persistenceConfig(
  persistenceKey: String? = null,
  darkModeThemeId: String = ThemeIds.Dark,
) = ThemeConfig.Persistence(
  themePack = this,
  persistenceKey = persistenceKey,
  darkModeThemeId = darkModeThemeId,
)

/**
 * Construct a [ThemeConfig.InMemory].
 */
@Stable
fun <T : ThemeSpec> ThemePack<T>.inMemoryConfig(
  initialThemeSpecId: String = ThemeIds.Default,
) = ThemeConfig.InMemory(
  themePack = this,
  initialThemeId = initialThemeSpecId,
)

/**
 * Configuration for the [ThemeController].
 */
@Immutable
sealed class ThemeConfig<T : ThemeSpec> {

  /**
   * Available themes for the [ThemeController].
   */
  abstract val themePack: ThemePack<T>

  /**
   * Configure the [ThemeController] to save theme preference (theme id) into local storage.
   * If there is no preference, then [ThemeController] will choose the proper theme based on [isSystemInDarkTheme].
   *
   * @param persistenceKey the local storage key to store theme preference.
   *                       If the value is null then a default key will be used.
   * @param darkModeThemeId use this theme if there is no preference and [isSystemInDarkTheme] is true.
   */
  @Immutable
  data class Persistence<T : ThemeSpec>(
    override val themePack: ThemePack<T>,
    val persistenceKey: String? = null,
    val darkModeThemeId: String = ThemeIds.Dark,
  ) : ThemeConfig<T>()

  /**
   * Configure the [ThemeController] to save theme preference in current component tree.
   *
   * @param initialThemeId initial theme for the component tree.
   */
  @Immutable
  data class InMemory<T : ThemeSpec>(
    override val themePack: ThemePack<T>,
    val initialThemeId: String = ThemeIds.Default,
  ) : ThemeConfig<T>()
}
