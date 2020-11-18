package dev.lcdsmao.jettheme.internal

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
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
  val themePack = buildThemePack {
    theme(DummyTheme(ThemeIds.Default))
    theme(DummyTheme(ThemeIds.Dark))
    theme(DummyTheme("id_other"))
  }
  val themeIdBasedOnSystem = ThemeIds.Dark
  val dataStoreKey = preferencesKey<String>("Test")
  lateinit var themeDataStore: FakeThemeDataStore
  lateinit var controller: PersistentThemeController

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
        emit(mapOf(dataStoreKey to null))
        emit(mapOf(dataStoreKey to "id_other"))
        emit(mapOf(dataStoreKey to "id_none"))
        emit(mapOf(dataStoreKey to ThemeIds.SystemSettings))
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
    themeDataStore.flow.emit(mapOf(dataStoreKey to "id_other"))
    controller.themeId shouldBe "id_other"
  }

  "Set valid theme should success" {
    controller.setThemeId("id_other")
    controller.setThemeId(ThemeIds.Default)
    controller.setThemeId(ThemeIds.SystemSettings)

    themeDataStore.flow.replayCache shouldContainInOrder listOf(
      mapOf(dataStoreKey to "id_other"),
      mapOf(dataStoreKey to ThemeIds.Default),
      mapOf(dataStoreKey to ThemeIds.SystemSettings),
    )
  }

  "Set invalid id should fail" {
    shouldThrowAny {
      controller.setThemeId("id_none")
    }
  }
})

private class FakeThemeDataStore : ThemeDataStore {

  val flow: MutableSharedFlow<Map<Preferences.Key<String>, String?>> =
    MutableSharedFlow(replay = 10, extraBufferCapacity = 10)

  override suspend fun setThemeId(key: Preferences.Key<String>, themeId: String) {
    val cache = flow.replayCache.firstOrNull().orEmpty()
    flow.emit(cache + (key to themeId))
  }

  override fun themeIdFlow(key: Preferences.Key<String>): Flow<String?> {
    return flow.map { it[key] }
  }
}
