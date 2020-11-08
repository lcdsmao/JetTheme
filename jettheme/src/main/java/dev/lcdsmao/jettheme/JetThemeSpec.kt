package dev.lcdsmao.jettheme

import androidx.compose.runtime.Stable

@Stable
interface JetThemeSpec {
  val id: String
}

object JetThemeIds {
  const val Default = "jet-theme-default"
  const val Dark = "jet-theme-dark"
  internal const val SystemSettings = "dev.lcdsmao.jettheme.system.settings"
}
