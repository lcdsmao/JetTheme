package dev.lcdsmao.jettheme

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ThemePackTest : StringSpec({
  "Build theme success" {
    val themePack = buildThemePack {
      theme(DummyTheme(id = ThemeIds.Default))
      theme(DummyTheme(id = "id_test"))
    }
    themePack.default shouldBe DummyTheme(id = ThemeIds.Default)
    themePack.size shouldBe 2
    themePack["id_test"] shouldBe DummyTheme(id = "id_test")
    themePack["id_test_no"] shouldBe DummyTheme(id = ThemeIds.Default)
    ("id_test" in themePack) shouldBe true
    ("id_test_no" in themePack) shouldBe false
    themePack.toList() shouldBe listOf(
      ThemeIds.Default to DummyTheme(id = ThemeIds.Default),
      "id_test" to DummyTheme(id = "id_test"),
    )
  }

  "Build theme without default id should fail" {
    shouldThrowAny {
      buildThemePack {
        theme(DummyTheme(id = "id_test"))
      }
    }
  }

  "Build theme with same ids should fail" {
    shouldThrowAny {
      buildThemePack {
        theme(DummyTheme(id = ThemeIds.Default))
        theme(DummyTheme(id = ThemeIds.Default))
      }
    }
  }

  "Build theme with same system settings should fail" {
    shouldThrowAny {
      buildThemePack {
        theme(DummyTheme(id = ThemeIds.Default))
        theme(DummyTheme(id = ThemeIds.SystemSettings))
      }
    }
  }

  "Transformer should transform theme expect default" {
    val themePack = buildThemePack {
      theme(DummyTheme(id = ThemeIds.Default))
      theme(DummyTheme(id = "id_test"))
      transformer { spec, _ ->
        DummyTheme(id = "${spec.id}_2")
      }
    }

    themePack.toList() shouldBe listOf(
      ThemeIds.Default to DummyTheme(id = ThemeIds.Default),
      "id_test_2" to DummyTheme(id = "id_test_2"),
    )
  }

  "Next theme id should circle through themes" {
    val themePack = buildThemePack {
      theme(DummyTheme(id = ThemeIds.Default))
      theme(DummyTheme(id = "id_test_1"))
      theme(DummyTheme(id = "id_test_2"))
    }
    themePack.nextThemeId(ThemeIds.Default) shouldBe "id_test_1"
    themePack.nextThemeId("id_test_1") shouldBe "id_test_2"
    themePack.nextThemeId("id_test_2") shouldBe ThemeIds.Default
  }

  "Test id extension" {
    val builder = ThemePackBuilder()
    builder.defaultId shouldBe ThemeIds.Default
    builder.darkId shouldBe ThemeIds.Dark
  }
})
