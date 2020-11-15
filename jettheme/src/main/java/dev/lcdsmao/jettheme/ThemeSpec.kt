package dev.lcdsmao.jettheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
interface ThemeSpec {
  val id: String
}

object ThemeIds {
  const val Default = "jettheme-default"
  const val Dark = "jettheme-dark"
  const val SystemSettings = "jettheme-system-settings"
}

@Composable
fun themeIdBasedOnSystemSettings(
  darkThemeId: String = ThemeIds.Dark,
): String = if (isSystemInDarkTheme()) darkThemeId else ThemeIds.Default
