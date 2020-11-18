package dev.lcdsmao.jettheme.internal

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface ThemeDataStore {

  fun themeIdFlow(key: Preferences.Key<String>): Flow<String?>

  suspend fun setThemeId(key: Preferences.Key<String>, themeId: String)

  companion object {

    internal const val Prefix = "dev.lcdsmao.jettheme"

    internal val AppThemeKey = preferencesKey<String>("$Prefix.app.theme.key")
  }
}

@Suppress("FunctionName")
internal fun ThemeDataStore(context: Context) = ThemeDataStoreImpl.get(context)

internal class ThemeDataStoreImpl(
  private val dataStore: DataStore<Preferences>,
) : ThemeDataStore {

  override fun themeIdFlow(key: Preferences.Key<String>): Flow<String?> = dataStore.data
    .map { preferences -> preferences[key] }

  override suspend fun setThemeId(key: Preferences.Key<String>, themeId: String) {
    dataStore.edit { preferences ->
      preferences[key] = themeId
    }
  }

  companion object {
    private val instanceHolder = SingletonHolder { context: Context ->
      val dataStore = context.createDataStore(
        name = "${ThemeDataStore.Prefix}.datastore"
      )
      ThemeDataStoreImpl(dataStore)
    }

    fun get(context: Context): ThemeDataStoreImpl = instanceHolder.getInstance(context)
  }
}
