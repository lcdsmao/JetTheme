<p>
  <a href="https://github.com/lcdsmao/JetTheme">
    <img src="https://raw.githubusercontent.com/lcdsmao/JetTheme/main/art/logo.svg" width="128px">
  </a>
</p>

# JetTheme

<p>
  <a href="https://github.com/lcdsmao/JetTheme/actions">
    <img src="https://github.com/lcdsmao/JetTheme/workflows/CI/badge.svg"/>
  </a>
  <a href="https://bintray.com/lcdsmao/maven/jettheme/_latestVersion">
    <img src="https://api.bintray.com/packages/lcdsmao/maven/jettheme/images/download.svg"/>
  </a>
</p>

JetTheme is a flexible theme provider for Jetpack Compose.

- Change the theme and recompose the UI dynamically.
- Save theme preference to local storage.
- Build your own design system.

## Download

```gradle
dependencies {
  // Use this if you want material design support (recommended)
  implementation "dev.lcdsmao.jettheme:jettheme-material:$latestVersion"
  // Use this if you want to build custom design system
  implementation "dev.lcdsmao.jettheme:jettheme:$latestVersion"
}
```

## Quick Start

### Provide Themes

Define your material themes themes using `buildMaterialThemePack`.

```kotlin
val AppTheme = buildMaterialThemePack {
  defaultMaterialTheme(
    colors = lightColors(...),
    typography = Typography(...),
    shapes = Shapes(...),
  )
  materialTheme(
    id = darkId,
    colors = darkColors(...),
  )
  materialTheme(
    id = "other_theme",
    colors = otherColors(...),
  )
}
```

For child components can correctly access defined `AppTheme` via `MaterialTheme`,
wrap your child components in a `ProvideAppMaterialTheme`.

```kotlin
@Composable
fun App() {
  ProvideAppMaterialTheme(AppTheme) {
    // children
  }
}
```

### Change Themes

You can retrieve current component tree's `ThemeController` from `ThemeControllerAmbient`.

```kotlin
val themeController = ThemeControllerAmbient.current
```

To change current theme you can use the theme id strings.

```kotlin
themeController.setThemeId(ThemeIds.Default)
themeController.setThemeId("other_theme_id")
```

### Access Current Theme Values

You can access current theme values via `MaterialTheme` object (from `androidx.compose.material`):

```kotlin
Surface(color = MaterialTheme.colors.primary) {
  // children
}
```

Check out JetTheme's [full documentation here.](https://lcdsmao.github.io/jettheme/)

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.
