package dev.lcdsmao.jettheme.internal

import dev.lcdsmao.jettheme.DummyTheme
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.buildThemePack
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class InMemoryThemeControllerTest : StringSpec({
  val themePack = buildThemePack {
    theme(DummyTheme(ThemeIds.Default))
    theme(DummyTheme(ThemeIds.Dark))
    theme(DummyTheme("id_other"))
  }
  lateinit var controller: InMemoryThemeController

  beforeEach {
    controller = InMemoryThemeController(
      themePack = themePack,
      initialThemeId = "id_other"
    )
  }

  "Set valid theme should success" {
    controller.setThemeId("id_other")
    controller.themeFlow.value shouldBe DummyTheme(id = "id_other")
    controller.themeId shouldBe "id_other"

    controller.setThemeId(ThemeIds.Default)
    controller.themeFlow.value shouldBe DummyTheme(id = ThemeIds.Default)
    controller.themeId shouldBe ThemeIds.Default
  }

  "Set invalid id should fail" {
    shouldThrowAny {
      controller.setThemeId("id_none")
    }

    shouldThrowAny {
      controller.setThemeId(ThemeIds.SystemSettings)
    }
  }
})
