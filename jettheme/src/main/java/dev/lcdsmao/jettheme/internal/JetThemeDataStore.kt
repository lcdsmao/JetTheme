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

  fun themeSpecIdFlow(key: Preferences.Key<String>): Flow<String?> = dataStore.data
    .map { preferences -> preferences[key] }

  suspend fun setThemeSpecId(key: Preferences.Key<String>, themeSpecId: String) {
    dataStore.edit { preferences ->
      preferences[key] = themeSpecId
    }
  }

  companion object {
    private const val Prefix = "dev.lcdsmao.jettheme"

    val AppThemeKey = preferencesKey<String>("$Prefix.app.theme.key")

    private val instanceHolder = SingletonHolder { context: Context ->
      val dataStore = context.createDataStore(
        name = "$Prefix.datastore"
      )
      JetThemeDataStore(dataStore)
    }

    fun get(context: Context): JetThemeDataStore = instanceHolder.getInstance(context)
  }
}
