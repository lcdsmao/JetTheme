# JetTheme

JetTheme is a flexible theme provider for Jetpack Compose.

- Change theme specification values (e.g. colors) and recompose the UI dynamically.
- Save theme settings to local storage.
- Support building with a custom design system.

## Installation

```gradle
dependencies {
  implementation "TODO"
}
```

## Basic Usage

Material Design out of the box.

1.Define your app theme using `buildMaterialTheme`:

```kotlin
val AppTheme = buildMaterialTheme {
  defaultMaterialSpec(
    colors = lightColors(...),
    typography = Typography(...),
    shapes = Shapes(...),
  )
  materialSpec(
    id = darkId,
    colors = darkColors(...),
  )
  materialSpec(
    id = "other_theme_spec",
    colors = otherColors(...),
  )
}
```

2. Wrap your app using `ProvideAppMaterialTheme`:

```kotlin
@Composable
fun App() {
  ProvideAppMaterialTheme(
    theme = appTheme,
  ) {
    // children
  }
}
```

3. Change theme using `JetThemeAmbient`:

```kotlin
val themeController = JetThemeAmbient.current
themeController.setDarkThemeSpec()
themeController.setThemeSpecId("other_theme_spec")
```

4. Access current theme values using `MaterialTheme` (from `androidx.compose.material`):

```kotlin
Surface(
  color = MaterialTheme.colors.primary,
)
```

## Advanced Usage

You can use JetTheme to implement your custom design system.

TODO

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.
