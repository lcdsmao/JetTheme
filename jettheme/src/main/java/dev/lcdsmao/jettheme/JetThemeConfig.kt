package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
fun JetTheme.persistentConfig(
  persistenceKey: String? = null,
  darkModeThemeId: String = JetThemeIds.Dark,
) = JetThemeConfig.Persistence(
  theme = this,
  persistenceKey = persistenceKey,
  darkModeThemeId = darkModeThemeId,
)

@Stable
fun JetTheme.inMemoryConfig(
  initialThemeId: String = JetThemeIds.Default,
) = JetThemeConfig.InMemory(
  theme = this,
  initialThemeId = initialThemeId,
)

@Immutable
sealed class JetThemeConfig {

  abstract val theme: JetTheme

  @Immutable
  data class Persistence(
    override val theme: JetTheme,
    val persistenceKey: String? = null,
    val darkModeThemeId: String = JetThemeIds.Dark,
  ) : JetThemeConfig()

  @Immutable
  data class InMemory(
    override val theme: JetTheme,
    val initialThemeId: String = JetThemeIds.Default,
  ) : JetThemeConfig()
}
