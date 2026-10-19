package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.ScriptDetector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Garshuni", appName)
  }

  @Test
  fun `script detector recognizes Birhatiyah and Arab Gundul`() {
    val suryaniSample = "برهتية كرير تتلية طوران"
    val detectedSuryani = ScriptDetector.detectScriptType(suryaniSample)
    assertEquals(ScriptDetector.TYPE_SURYANI_BIRHATIYAH, detectedSuryani)

    val arabGundulSample = "فصل اركان الاسلام خمسة"
    val detectedGundul = ScriptDetector.detectScriptType(arabGundulSample)
    assertEquals(ScriptDetector.TYPE_ARAB_GUNDUL, detectedGundul)
  }
}
