package dev.lcdsmao.jettheme.internal

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class JetThemeDataStore(
  private val dataStore: DataStore<Preferences>,
) {

  fun themeIdFlow(key: Preferences.Key<String>): Flow<String?> = dataStore.data
    .map { preferences -> preferences[key] }

  suspend fun setThemeId(key: Preferences.Key<String>, themeId: String) {
    dataStore.edit { preferences ->
      preferences[key] = themeId
    }
  }

  companion object {
    val AppThemeKey = preferencesKey<String>("com.lcdsmao.dev.app.jettheme.key")

    private val instanceHolder = SingletonHolder { context: Context ->
      val dataStore = context.createDataStore(
        name = "dev.lcdsmao.themeambient"
      )
      JetThemeDataStore(dataStore)
    }

    fun get(context: Context): JetThemeDataStore = instanceHolder.getInstance(context)
  }
}
