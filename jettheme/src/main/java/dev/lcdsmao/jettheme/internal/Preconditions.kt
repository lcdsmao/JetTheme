package dev.lcdsmao.jettheme.internal

import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.ThemePack

internal fun checkCanSetSpecId(themePack: ThemePack<*>, id: String) {
  check(id == ThemeIds.SystemSettings || id in themePack) {
    "ThemeId $id does not existed in $themePack."
  }
}
