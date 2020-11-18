package dev.lcdsmao.jettheme

import androidx.compose.ui.test.junit4.ComposeTestRule

fun ComposeTestRule.sleepAndWait(timeMills: Int = 500) {
  Thread.sleep(timeMills.toLong())
  waitForIdle()
}
