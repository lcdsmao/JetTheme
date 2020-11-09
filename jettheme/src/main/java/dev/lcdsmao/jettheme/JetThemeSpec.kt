package dev.lcdsmao.jettheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
interface JetThemeSpec {
  val id: String
}

object JetThemeSpecIds {
  const val Default = "jettheme-default"
  const val Dark = "jettheme-dark"
  const val SystemSettings = "jettheme-system-settings"
}

@Composable
fun themeSpecIdBasedOnSystemSettings(
  darkModeSpecId: String = JetThemeSpecIds.Dark,
): String = if (isSystemInDarkTheme()) darkModeSpecId else JetThemeSpecIds.Default
