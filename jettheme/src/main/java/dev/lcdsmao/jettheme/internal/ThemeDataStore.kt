package dev.lcdsmao.jettheme.internal

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal interface ThemeDataStore {

  fun themeIdFlow(key: Preferences.Key<String>): Flow<String?>

  suspend fun setThemeId(key: Preferences.Key<String>, themeId: String)

  companion object {

    internal const val Prefix = "dev.lcdsmao.jettheme"

    internal val AppThemeKey = stringPreferencesKey("$Prefix.app.theme.key")
  }
}

@Suppress("FunctionName")
internal fun ThemeDataStore(context: Context) = ThemeDataStoreImpl(context.dataStore)

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
}

private val Context.dataStore by preferencesDataStore(
  name = "${ThemeDataStore.Prefix}.datastore"
)
