# Building Own Design System

You can use JetTheme to implement your own design system.

For example, we want to create a Simple Design System which has different color scheme from Material Design.
The color scheme contains two colors, the background color and text color.

1. Define the `ThemeSpec` of our Simple Design System:

```kotlin
data class SimpleThemeSpec(
  override val id: String,
  val colors: SimpleColors,
  val typography: Typography = Typography(),
  val shapes: Shapes = Shapes(),
) : ThemeSpec

class SimpleColors(
  background: Color,
  text: Color,
  isDark: Boolean,
) {
  var background by mutableStateOf(background)
    private set
  var text by mutableStateOf(text)
    private set
  var isDark by mutableStateOf(isDark)
    private set

  fun update(other: SimpleColors) {
    background = other.background
    text = other.text
    isDark = other.isDark
  }
}
```

2. Construct a `ThemePack` with the `SimpleThemeSpec`:

```kotlin
val AppTheme = buildThemePack {
  theme(
    SimpleThemeSpec(
      id = defaultId,
      colors = LightColorPalette,
    )
  )
  theme(
    SimpleThemeSpec(
      id = darkId,
      colors = DarkColorPalette,
    )
  )
}
```

3. Create a `SimpleTheme` object which can retrieve current theme colors:

```kotlin
private val AmbientSimpleColors = staticAmbientOf<SimpleColors>()

object SimpleTheme {
  @Composable
  val colors: SimpleColors
    get() = AmbientSimpleColors.current
}
```

4. Create a Simple Design System provider by the `ProvideAppTheme`:

```kotlin
@Composable
fun ProvideSimpleTheme(
  content: @Composable () -> Unit,
) {
  ProvideAppTheme<SimpleThemeSpec>(
    themePack = AppTheme,
  ) { theme ->
    val colorPalette = remember { theme.colors }
    colorPalette.update(theme.colors)
    Providers(AmbientSimpleColors provides colorPalette) {
      MaterialTheme(
        colors = debugColors(theme.colors.isDark),
        typography = theme.typography,
        shapes = theme.shapes,
        content = content,
      )
    }
  }
}

// Sets all colors to [debugColor] to discourage usage of [MaterialTheme.colors]
// in preference to [SimpleTheme.colors].
private fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Magenta
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)
```

5. Access current theme values from `SimpleTheme` and change themes using `ThemeController`:

```kotlin
@Composable
fun CustomDesignSystemApp() {
  ProvideSimpleTheme {
    Box {
      val (themeId, setThemeId) = ThemeControllerAmbient.current
      Text(
        "Custom Design System",
        modifier = Modifier.clickable(onClick = { setThemeId(AppTheme.nextThemeId(themeId)) }),
        color = SimpleTheme.colors.text
      )
    }
  }
}
```
