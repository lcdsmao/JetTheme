package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
fun JetTheme.persistenceConfig(
  persistenceKey: String? = null,
  darkModeThemeSpecId: String = JetThemeSpecIds.Dark,
) = JetThemeConfig.Persistence(
  theme = this,
  persistenceKey = persistenceKey,
  darkModeThemeSpecId = darkModeThemeSpecId,
)

@Stable
fun JetTheme.inMemoryConfig(
  initialThemeSpecId: String = JetThemeSpecIds.Default,
) = JetThemeConfig.InMemory(
  theme = this,
  initialThemeSpecId = initialThemeSpecId,
)

@Immutable
sealed class JetThemeConfig {

  abstract val theme: JetTheme

  @Immutable
  data class Persistence(
    override val theme: JetTheme,
    val persistenceKey: String? = null,
    val darkModeThemeSpecId: String = JetThemeSpecIds.Dark,
  ) : JetThemeConfig()

  @Immutable
  data class InMemory(
    override val theme: JetTheme,
    val initialThemeSpecId: String = JetThemeSpecIds.Default,
  ) : JetThemeConfig()
}
