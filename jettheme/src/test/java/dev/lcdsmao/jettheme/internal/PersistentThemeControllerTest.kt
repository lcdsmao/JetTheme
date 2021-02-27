package dev.lcdsmao.jettheme.internal

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.lcdsmao.jettheme.DummyTheme
import dev.lcdsmao.jettheme.ThemeIds
import dev.lcdsmao.jettheme.buildThemePack
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope

@ExperimentalCoroutinesApi
class PersistentThemeControllerTest : StringSpec({
  val coroutineScope = TestCoroutineScope()
  val themePack = buildThemePack<DummyTheme> {
    theme(DummyTheme(ThemeIds.Default))
    theme(DummyTheme(ThemeIds.Dark))
    theme(DummyTheme("id_other"))
  }
  val themeIdBasedOnSystem = ThemeIds.Dark
  val dataStoreKey = stringPreferencesKey("Test")
  lateinit var themeDataStore: FakeThemeDataStore
  lateinit var controller: PersistentThemeController<DummyTheme>

  beforeEach {
    themeDataStore = FakeThemeDataStore()
    controller = PersistentThemeController(
      coroutineScope = coroutineScope,
      themePack = themePack,
      themeIdBasedOnSystem = themeIdBasedOnSystem,
      themeDataStore = themeDataStore,
      dataStoreKey = "Test",
    )
  }

  "Test theme flow" {
    launch {
      with(themeDataStore.flow) {
        emit(preferencesOf())
        emit(preferencesOf(dataStoreKey to "id_other"))
        emit(preferencesOf(dataStoreKey to "id_none"))
        emit(preferencesOf(dataStoreKey to ThemeIds.SystemSettings))
      }
    }
    controller.themeFlow.take(4).toList() shouldContainInOrder listOf(
      DummyTheme(id = themeIdBasedOnSystem),
      DummyTheme(id = "id_other"),
      DummyTheme(id = ThemeIds.Default),
      DummyTheme(id = themeIdBasedOnSystem),
    )
  }

  "Test theme id" {
    controller.themeId shouldBe ThemeIds.Default
    themeDataStore.flow.emit(preferencesOf(dataStoreKey to "id_other"))
    controller.themeId shouldBe "id_other"
  }

  "Set valid theme should success" {
    controller.setThemeId("id_other")
    controller.setThemeId(ThemeIds.Default)
    controller.setThemeId(ThemeIds.SystemSettings)

    themeDataStore.flow.replayCache shouldContainInOrder listOf(
      preferencesOf(dataStoreKey to "id_other"),
      preferencesOf(dataStoreKey to ThemeIds.Default),
      preferencesOf(dataStoreKey to ThemeIds.SystemSettings),
    )
  }

  "Set invalid id should fail" {
    shouldThrowAny {
      controller.setThemeId("id_none")
    }
  }
})

private class FakeThemeDataStore : ThemeDataStore {

  val flow: MutableSharedFlow<Preferences> =
    MutableSharedFlow(replay = 10, extraBufferCapacity = 10)

  override suspend fun setThemeId(key: Preferences.Key<String>, themeId: String) {
    val pref = flow.replayCache.firstOrNull() ?: preferencesOf()
    val mutPref = pref.toMutablePreferences()
    mutPref += (key to themeId)
    flow.emit(mutPref.toPreferences())
  }

  override fun themeIdFlow(key: Preferences.Key<String>): Flow<String?> {
    return flow.map { it[key] }
  }
}
