package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.example.data.LearningActivity
import com.example.data.UserProfile
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WhatsAppHelper {

    fun isWhatsAppInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        val packages = listOf("com.whatsapp", "com.whatsapp.w4b")
        for (pkg in packages) {
            try {
                packageManager.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: Exception) {
                // Not installed
            }
        }
        return false
    }

    fun formatLearningReport(
        user: UserProfile,
        activities: List<LearningActivity>
    ): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        val solvedCount = activities.count { it.type == "PHOTO_PROBLEM" }
        val doubtCount = activities.count { it.type == "QUESTION_ANSWER" }
        val topics = activities.map { it.title }.distinct().take(5)

        val builder = StringBuilder()
        builder.append("📖 *Gurudev Deepak Bhardwaj AI Online Gurukul*\n")
        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        builder.append("🌟 *STUDENT LEARNING & SEARCH REPORT* 🌟\n\n")
        builder.append("👤 *Student Name:* ${user.studentName}\n")
        builder.append("🎓 *Grade:* Class ${user.studentClass} (${user.board} Syllabus)\n")
        builder.append("📱 *Registered Mobile:* +91 ${user.mobileNumber}\n")
        builder.append("📅 *Report Generated:* $dateStr\n\n")

        builder.append("📊 *Key Study Milestones:*\n")
        builder.append("• 📸 Problems Solved (Step-by-Step): $solvedCount\n")
        builder.append("• ❓ Doubts & Questions Clarified: $doubtCount\n")
        builder.append("• 📚 Total Learning Sessions: ${activities.size}\n\n")

        if (topics.isNotEmpty()) {
            builder.append("🎯 *Topics & Concepts Practiced:*\n")
            topics.forEachIndexed { index, topic ->
                builder.append("  ${index + 1}. $topic\n")
            }
            builder.append("\n")
        }

        // Include latest solved problem highlight
        val latestProblem = activities.firstOrNull { it.type == "PHOTO_PROBLEM" }
        if (latestProblem != null) {
            builder.append("✨ *Latest Problem Solved Highlight:*\n")
            builder.append("• *Problem:* ${latestProblem.problemText.take(120)}...\n")
            if (latestProblem.formulaUsed.isNotBlank()) {
                builder.append("• *Formula Used:* ${latestProblem.formulaUsed.take(100)}\n")
            }
            if (latestProblem.youtubeUrl.isNotBlank()) {
                builder.append("• *Recommended Video Lecture:* ${latestProblem.youtubeUrl}\n")
            }
            builder.append("\n")
        }

        builder.append("🙏 *Gurudev Deepak Bhardwaj's Message:*\n")
        builder.append("\"विद्यां ददाति विनयं विनयाद्याति पात्रताम्।\n")
        builder.append("Consistent daily practice leads to academic mastery. All courses under CBSE & ICSE remain 100% free for you!\"\n")
        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━")

        return builder.toString()
    }

    fun sendReportToWhatsApp(
        context: Context,
        mobileNumber: String,
        reportMessage: String
    ): Boolean {
        return try {
            val cleanNumber = mobileNumber.replace("[^0-9]".toRegex(), "")
            val internationalNumber = if (cleanNumber.length == 10) "91$cleanNumber" else cleanNumber
            val encodedMessage = URLEncoder.encode(reportMessage, "UTF-8")
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$internationalNumber&text=$encodedMessage")

            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Prefer whatsapp package if installed
            if (isWhatsAppInstalled(context)) {
                intent.setPackage("com.whatsapp")
            }

            context.startActivity(intent)
            true
        } catch (e: Exception) {
            // Fallback general view intent
            try {
                val fallbackUri = Uri.parse("https://wa.me/?text=${URLEncoder.encode(reportMessage, "UTF-8")}")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
                true
            } catch (err: Exception) {
                Toast.makeText(context, "Could not launch WhatsApp: ${err.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    fun shareSolutionToWhatsApp(
        context: Context,
        mobileNumber: String,
        activity: LearningActivity
    ) {
        val text = """
            ✨ *Solved by Gurudev Deepak Bhardwaj AI Online Tutor*
            🎓 Class: ${activity.gradeLevel} (${activity.board})
            📚 Subject: ${activity.subject}
            
            📝 *Problem:*
            ${activity.problemText}
            
            📐 *Formula & Concept:*
            ${activity.formulaUsed}
            
            🪜 *Solution Steps:*
            ${activity.stepSolution}
            
            💡 *Gurudev's Tip:*
            ${activity.guruTip}
            
            📺 *Watch Recommended Video Lesson:*
            ${activity.youtubeUrl}
        """.trimIndent()

        sendReportToWhatsApp(context, mobileNumber, text)
    }

    fun openYouTubeSearch(context: Context, query: String) {
        try {
            val encoded = URLEncoder.encode(query, "UTF-8")
            val uri = Uri.parse("https://www.youtube.com/results?search_query=$encoded")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open video link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
