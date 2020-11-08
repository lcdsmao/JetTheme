package dev.lcdsmao.jettheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import dev.lcdsmao.jettheme.internal.JetThemeSpecDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JetThemeController internal constructor(
  private val coroutineScope: CoroutineScope,
  private val themeDataStore: JetThemeSpecDataStore,
  private val themeSpecMap: JetThemeSpecMap,
) {

  private val themeIdFlow: StateFlow<String?> = themeDataStore.themeIdFlow
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, null)

  internal val themeFlow: Flow<JetThemeSpec?> = themeDataStore.themeIdFlow
    .map { id ->
      if (id == null) themeSpecMap.default else themeSpecMap[id]
    }

  val themeId: String?
    get() = themeIdFlow.value

  fun setThemeId(themeId: String) {
    coroutineScope.launch {
      themeDataStore.setThemeId(themeId)
    }
  }

  operator fun component1(): String? = themeId

  operator fun component2(): (themeId: String) -> Unit = ::setThemeId
}

@Composable
fun JetThemeController.themeState(): State<JetThemeSpec?> {
  return themeFlow.collectAsState(initial = null)
}

// @Composable
// inline fun <reified T : JetThemeSpec> JetThemeController.themeState(): State<T?> {
//   return themeFlow
//     .map { T::class.cast(it) }
//     .collectAsState(initial = null)
// }
