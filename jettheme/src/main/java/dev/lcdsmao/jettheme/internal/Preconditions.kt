package dev.lcdsmao.jettheme.internal

import dev.lcdsmao.jettheme.JetTheme
import dev.lcdsmao.jettheme.JetThemeSpecIds

internal fun checkCanSetSpecId(theme: JetTheme, id: String) {
  check(id == JetThemeSpecIds.SystemSettings || id in theme) {
    "ThemeSpecId $id does not existed in $theme."
  }
}
