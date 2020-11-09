package dev.lcdsmao.jettheme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Immutable
data class JetTheme internal constructor(
  private val themeSpecMap: Map<String, JetThemeSpec>,
) {

  @Stable
  val default: JetThemeSpec
    get() = requireNotNull(themeSpecMap[JetThemeSpecIds.Default])

  @Stable
  val size: Int
    get() = themeSpecMap.size

  @Stable
  operator fun get(specId: String): JetThemeSpec = themeSpecMap[specId] ?: default

  @Stable
  operator fun contains(specId: String): Boolean = specId in themeSpecMap

  @Stable
  fun toList(): List<Pair<String, JetThemeSpec>> = themeSpecMap.toList()

  @Stable
  override fun toString(): String = "JetTheme: $themeSpecMap"
}

fun JetTheme.nextThemeSpecId(id: String): String {
  val entries = toList()
  val index = entries.indexOfFirst { it.first == id }
  return when {
    index < 0 || index + 1 >= size -> entries.first().first
    else -> entries[index + 1].first
  }
}

@OptIn(ExperimentalContracts::class)
fun buildJetTheme(
  block: JetThemeBuilder.() -> Unit,
): JetTheme {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return JetThemeBuilder().apply(block).build()
}

private typealias JetThemeSpecTransformer = (
  id: String,
  spec: JetThemeSpec,
  defaultSpec: JetThemeSpec,
) -> JetThemeSpec

class JetThemeBuilder internal constructor() {

  private val specs = mutableListOf<JetThemeSpec>()

  private var transformer: JetThemeSpecTransformer? = null

  fun spec(themeSpec: JetThemeSpec) {
    specs += themeSpec
  }

  fun transformer(f: JetThemeSpecTransformer) {
    transformer = f
  }

  internal fun build(): JetTheme {
    val themeMap = specs.associateBy { it.id }
    check(JetThemeSpecIds.Default in themeMap) {
      "Must provide a default theme spec using with id ${JetThemeSpecIds.Default}."
    }
    check(themeMap.size == specs.size) {
      "Provided theme specs has duplication: ${specs.map { it.id }}."
    }
    val defaultSpec = requireNotNull(themeMap[JetThemeSpecIds.Default])
    val transformedMap = transformer?.let { f ->
      themeMap.mapValues { (id, spec) -> f(id, spec, defaultSpec) }
    } ?: themeMap
    return JetTheme(transformedMap)
  }
}

@Suppress("unused")
val JetThemeBuilder.defaultId
  get() = JetThemeSpecIds.Default

@Suppress("unused")
val JetThemeBuilder.darkId
  get() = JetThemeSpecIds.Dark
