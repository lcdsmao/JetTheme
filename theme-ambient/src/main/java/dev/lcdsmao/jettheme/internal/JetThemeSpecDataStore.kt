package dev.lcdsmao.jettheme.internal

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class JetThemeSpecDataStore(
  private val dataStore: DataStore<Preferences>,
) {

  val themeIdFlow: Flow<String?> = dataStore.data
    .map { preferences -> preferences[ThemeIdKey] }

  suspend fun setThemeId(themeId: String) {
    dataStore.edit { preferences ->
      preferences[ThemeIdKey] = themeId
    }
  }

  companion object {
    private val ThemeIdKey = preferencesKey<String>("theme-id")

    private val instanceHolder = SingletonHolder { context: Context ->
      val dataStore = context.createDataStore(
        name = "dev.lcdsmao.themeambient"
      )
      JetThemeSpecDataStore(dataStore)
    }

    fun get(context: Context): JetThemeSpecDataStore = instanceHolder.getInstance(context)
  }
}
