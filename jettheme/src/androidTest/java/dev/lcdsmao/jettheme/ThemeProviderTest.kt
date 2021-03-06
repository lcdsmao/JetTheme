package dev.lcdsmao.jettheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dev.lcdsmao.jettheme.testfixtures.android.sleepAndWait
import org.junit.Rule
import org.junit.Test

class ThemeProviderTest {

  @get:Rule
  val composeRule = createComposeRule()

  private val themePack = buildThemePack<DummyTheme> {
    theme(DummyTheme(id = defaultId, value = "Default"))
    theme(DummyTheme(id = darkId, value = "Dark"))
    theme(DummyTheme(id = "id_other", value = "Other"))
  }

  @Test
  fun testPersistenceThemeController() {
    var toThemeId = ThemeIds.Dark

    composeRule.setContent {
      ProvideTheme(
        themePack.persistenceConfig(
          persistenceKey = "test_key",
          darkModeThemeId = ThemeIds.Default,
        ),
      ) { theme ->
        val controller = LocalThemeController.current
        TestTextNode(
          theme = theme,
          modifier = Modifier.clickable(
            onClick = {
              controller.setThemeId(toThemeId)
            }
          ),
        )
      }
    }

    composeRule.sleepAndWait()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Default")
      .performClick()

    composeRule.sleepAndWait()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .also { toThemeId = "id_other" }
      .performClick()

    composeRule.sleepAndWait()
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
      ) { theme ->
        val controller = LocalThemeController.current
        TestTextNode(
          theme = theme,
          modifier = Modifier.clickable(
            onClick = {
              controller.setThemeId(toThemeId)
            }
          ),
        )
      }
    }

    composeRule.sleepAndWait()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .performClick()

    composeRule.sleepAndWait()
    composeRule.onNodeWithTag(TestTag)
      .assertTextEquals("Dark")
      .also { toThemeId = "id_other" }
      .performClick()

    composeRule.sleepAndWait()
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
