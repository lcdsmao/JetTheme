package dev.lcdsmao.jettheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Represent the specification of a design system.
 * For example, a material spec can contains colors, typography, and shapes.
 */
@Stable
interface ThemeSpec {

  /**
   * The id of the theme instance.
   */
  val id: String
}

/**
 * Common use theme ids.
 */
object ThemeIds {
  /**
   * The default id of a [ThemePack].
   */
  const val Default = "jettheme-default"

  /**
   * The default id of a dark mode theme.
   * Using other ids for dark mode is also acceptable.
   *
   * @see ThemeConfig.Persistence
   * @see ThemeController.setDarkModeTheme
   * @see themeIdBasedOnSystemSettings
   */
  const val Dark = "jettheme-dark"

  /**
   * A virtual theme id that used to select themes based on system settings (dark mode).
   */
  const val SystemSettings = "jettheme-system-settings"
}

/**
 * A convenient function to retrieve system settings corresponded theme id.
 * If system in dark mode than returns [darkThemeId], otherwise returns [ThemeIds.Default].
 */
@Composable
fun themeIdBasedOnSystemSettings(
  darkThemeId: String = ThemeIds.Dark,
): String = if (isSystemInDarkTheme()) darkThemeId else ThemeIds.Default
