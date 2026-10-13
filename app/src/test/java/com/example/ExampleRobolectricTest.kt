package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CourseCatalog
import com.example.data.LearningActivity
import com.example.data.UserProfile
import com.example.util.WhatsAppHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches Gurudev Deepak Bhardwaj`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Gurudev Deepak Bhardwaj", appName)
    }

    @Test
    fun `course catalog returns chapters for CBSE and ICSE Class 10`() {
        val cbseChapters = CourseCatalog.getChaptersForClass(10, "CBSE")
        assertTrue(cbseChapters.isNotEmpty())
        val icseChapters = CourseCatalog.getChaptersForClass(10, "ICSE")
        assertTrue(icseChapters.isNotEmpty())
    }

    @Test
    fun `whatsApp report formatting produces complete study report`() {
        val user = UserProfile(
            isLoggedIn = true,
            mobileNumber = "9876543210",
            studentName = "Arjun Sharma",
            studentClass = 10,
            board = "CBSE"
        )
        val activities = listOf(
            LearningActivity(
                type = "PHOTO_PROBLEM",
                title = "Quadratic Equations",
                problemText = "Find roots of 2x^2 - 7x + 3 = 0",
                subject = "Mathematics",
                gradeLevel = "Class 10",
                board = "CBSE",
                stepSolution = "Step 1: Calculate Discriminant",
                formulaUsed = "x = (-b +- sqrt(D)) / 2a",
                guruTip = "Check roots by substitution",
                youtubeUrl = "https://youtube.com/watch?v=123",
                youtubeTitle = "Class 10 Quadratic Equations"
            )
        )
        val report = WhatsAppHelper.formatLearningReport(user, activities)
        assertTrue(report.contains("Gurudev Deepak Bhardwaj"))
        assertTrue(report.contains("Arjun Sharma"))
        assertTrue(report.contains("Class 10"))
        assertTrue(report.contains("9876543210"))
    }
}
