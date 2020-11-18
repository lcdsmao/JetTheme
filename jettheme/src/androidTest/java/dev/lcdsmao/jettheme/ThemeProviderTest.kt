package dev.lcdsmao.jettheme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ThemeProviderTest {

  @get:Rule
  val composeRule = createComposeRule()

  private val themePack = buildThemePack {
    theme(DummyTheme(id = defaultId, value = "Default"))
    theme(DummyTheme(id = darkId, value = "Dark"))
    theme(DummyTheme(id = "id_other", value = "Other"))
  }

  @Test
  fun testPersistenceThemeController() {
    var toThemeId = ThemeIds.Dark

    composeRule.setContent {
      ProvideTheme<DummyTheme>(
        themePack.persistenceConfig(
          persistenceKey = "test_key",
          darkModeThemeId = ThemeIds.Default,
        ),
        crossfadeAnimSpec = tween(durationMillis = 0)
      ) { theme ->
        val controller = ThemeControllerAmbient.current
        TestTextNode(
          theme = theme,
          modifier = Modifier.clickable(onClick = {
            controller.setThemeId(toThemeId)
          }),
        )
      }
    }

    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Default")
      .performClick()

    composeRule.waitForIdle()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .also { toThemeId = "id_other" }
      .performClick()

    composeRule.waitForIdle()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Other")
  }

  @Test
  fun testInMemoryThemeController() {
    var toThemeId = ThemeIds.Dark

    composeRule.setContent {
      ProvideTheme<DummyTheme>(
        themePack.inMemoryConfig(
          initialThemeSpecId = ThemeIds.Dark
        ),
        crossfadeAnimSpec = tween(durationMillis = 0)
      ) { theme ->
        val controller = ThemeControllerAmbient.current
        TestTextNode(
          theme = theme,
          modifier = Modifier.clickable(onClick = {
            controller.setThemeId(toThemeId)
          }),
        )
      }
    }

    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .performClick()

    composeRule.waitForIdle()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .also { toThemeId = "id_other" }
      .performClick()

    composeRule.waitForIdle()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Other")
  }

  @Suppress("TestFunctionName")
  @Composable
  private fun TestTextNode(
    theme: DummyTheme,
    modifier: Modifier = Modifier,
  ) {
    BasicText(
      text = theme.value,
      modifier = modifier.testTag(TestTag)
    )
  }

  private data class DummyTheme(override val id: String, val value: String = "") : ThemeSpec

  companion object {
    private const val TestTag = "test_tag"
  }
}
