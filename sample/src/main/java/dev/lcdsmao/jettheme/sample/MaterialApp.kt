package dev.lcdsmao.jettheme.sample

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.lcdsmao.jettheme.JetThemeAmbient
import dev.lcdsmao.jettheme.component1
import dev.lcdsmao.jettheme.component2
import dev.lcdsmao.jettheme.darkId
import dev.lcdsmao.jettheme.material.ProvideAppMaterialTheme
import dev.lcdsmao.jettheme.material.buildMaterialTheme
import dev.lcdsmao.jettheme.material.defaultMaterialSpec
import dev.lcdsmao.jettheme.material.materialSpec
import dev.lcdsmao.jettheme.nextThemeId

@Composable
fun MaterialApp() {
  ProvideAppMaterialTheme(
    theme = appTheme,
  ) {
    Scaffold(
      floatingActionButton = { ToggleThemeFloatButton() }
    ) {
      Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp)
      ) {
        Text(text = "Heading 1", style = MaterialTheme.typography.h1, maxLines = 1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Heading 2", style = MaterialTheme.typography.h2, maxLines = 1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Heading 3", style = MaterialTheme.typography.h3, maxLines = 1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Heading 4", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Heading 5", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "Heading 6", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.size(32.dp))

        Row {
          Box(modifier = Modifier.size(64.dp).background(MaterialTheme.colors.primary))
          Spacer(modifier = Modifier.size(16.dp))
          Box(modifier = Modifier.size(64.dp).background(MaterialTheme.colors.primaryVariant))
          Spacer(modifier = Modifier.size(16.dp))
          Box(modifier = Modifier.size(64.dp).background(MaterialTheme.colors.secondary))
        }
      }
    }
  }
}

@Composable
private fun ToggleThemeFloatButton() {
  val (themeId, setThemeId) = JetThemeAmbient.current
  FloatingActionButton(
    backgroundColor = MaterialTheme.colors.primary,
    // contentColor = MaterialTheme.colors.onPrimary,
    onClick = { setThemeId(appTheme.nextThemeId(themeId)) }
  ) {
    Icon(asset = Icons.Default.Refresh)
  }
}

private val shapes = Shapes(
  small = RoundedCornerShape(4.dp),
  medium = RoundedCornerShape(4.dp),
  large = RoundedCornerShape(0.dp)
)

private val typography = Typography()

private val darkColorPalette = darkColors(
  primary = Color(0xFFBB86FC),
  primaryVariant = Color(0xFF3700B3),
  secondary = Color(0xFF03DAC5),
)

private val lightColorPalette = lightColors(
  primary = Color(0xFFFEDBD0),
  primaryVariant = Color(0xFFFFF0E8),
  secondary = Color(0xFFCC4C33),
  onPrimary = Color(0xFF442C2E),
)

private val appTheme = buildMaterialTheme {
  defaultMaterialSpec(
    colors = lightColorPalette,
    typography = typography,
    shapes = shapes,
  )
  materialSpec(
    id = darkId,
    colors = darkColorPalette,
  )
}
