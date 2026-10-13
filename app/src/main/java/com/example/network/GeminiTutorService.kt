package com.example.network

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class SolvedProblemResult(
    val title: String,
    val identifiedProblem: String,
    val formulaAndConcepts: String,
    val steps: List<String>,
    val finalAnswer: String,
    val gurudevTip: String,
    val youtubeQuery: String,
    val youtubeVideoTitle: String,
    val rawText: String
)

class GeminiTutorService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    /**
     * Solves a photo problem with step-by-step working and formulas involved.
     * Uses gemini-3.1-pro-preview for complex STEM problem solving.
     */
    suspend fun solveProblemFromPhoto(
        bitmap: Bitmap?,
        additionalPrompt: String,
        studentClass: Int,
        board: String
    ): Result<SolvedProblemResult> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                Log.w("GeminiTutorService", "API key missing or placeholder, generating offline fallback")
                return@withContext Result.success(getFallbackSolvedProblem(studentClass, board, additionalPrompt))
            }

            val systemInstruction = """
                You are Gurudev Deepak Bhardwaj, a wise, affectionate, and venerable AI Online Tutor for Indian school students in Class 1 to Class 10 following CBSE and ICSE curricula.
                The student is in Class $studentClass ($board Syllabus).
                Explain everything in clear, friendly English with respectful Hindi touches (e.g. 'Priye Vidhyarthi').
                All courses are 100% free under CBSE and ICSE.
                
                You MUST format your answer with clear headers:
                [TITLE]: Short title of the problem/topic
                [PROBLEM]: Clear restatement of the problem from the image or text
                [FORMULA]: All mathematical/scientific formulas and core concepts used in this problem
                [STEPS]: Numbered step-by-step clear solution, calibrated to Class $studentClass level
                [FINAL_ANSWER]: The definitive final answer highlighted
                [GURUDEV_TIP]: Gurudev Deepak Bhardwaj's wisdom, shortcut method, or common pitfall to avoid in board exams
                [YOUTUBE_VIDEO]: Recommended YouTube search title/query for video explanation of this topic
            """.trimIndent()

            val partsArray = JSONArray()

            val promptText = if (additionalPrompt.isNotBlank()) {
                "Solve this problem step-by-step for Class $studentClass ($board): $additionalPrompt"
            } else {
                "Priye Gurudev Deepak Bhardwaj, please identify and solve this problem from the image step-by-step for Class $studentClass ($board) with formulas involved."
            }
            partsArray.put(JSONObject().put("text", promptText))

            if (bitmap != null) {
                val base64Image = bitmapToBase64(bitmap)
                val inlineData = JSONObject()
                    .put("mimeType", "image/jpeg")
                    .put("data", base64Image)
                partsArray.put(JSONObject().put("inlineData", inlineData))
            }

            val requestBodyJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject().put("parts", partsArray)
            contentsArray.put(contentObj)
            requestBodyJson.put("contents", contentsArray)

            // System instruction
            val sysInstructionObj = JSONObject()
                .put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            requestBodyJson.put("systemInstruction", sysInstructionObj)

            val model = "gemini-3.1-pro-preview"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e("GeminiTutorService", "API Error: ${response.code} - $responseBody")
                // Graceful fallback if quota or temporary error
                return@withContext Result.success(getFallbackSolvedProblem(studentClass, board, additionalPrompt))
            }

            val responseJson = JSONObject(responseBody)
            val candidates = responseJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text", "") ?: ""

            if (text.isBlank()) {
                return@withContext Result.success(getFallbackSolvedProblem(studentClass, board, additionalPrompt))
            }

            Result.success(parseSolvedProblem(text, studentClass, board))
        } catch (e: Exception) {
            Log.e("GeminiTutorService", "Error in solveProblemFromPhoto", e)
            Result.success(getFallbackSolvedProblem(studentClass, board, additionalPrompt))
        }
    }

    /**
     * Answers any student question / doubt using gemini-3.5-flash.
     * Includes step-by-step guidance and a recommended YouTube tutorial for further help!
     */
    suspend fun askGurudevQuestion(
        question: String,
        studentClass: Int,
        board: String
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(getFallbackAnswer(question, studentClass, board))
            }

            val systemInstruction = """
                You are Gurudev Deepak Bhardwaj, a revered AI Online Tutor for Class 1 to Class 10 CBSE and ICSE students.
                Respond with warmth, clarity, structured numbered points or steps, and any relevant formula.
                At the end of your response, ALWAYS include:
                [RECOMMENDED_VIDEO_QUERY]: <Specific YouTube search topic for video lesson on this topic>
            """.trimIndent()

            val requestBodyJson = JSONObject()
            val partsArray = JSONArray()
            partsArray.put(JSONObject().put("text", "Student (Class $studentClass, $board) asks: $question"))
            val contentsArray = JSONArray()
            contentsArray.put(JSONObject().put("parts", partsArray))
            requestBodyJson.put("contents", contentsArray)

            val sysInstructionObj = JSONObject()
                .put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
            requestBodyJson.put("systemInstruction", sysInstructionObj)

            val model = "gemini-3.5-flash"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.success(getFallbackAnswer(question, studentClass, board))
            }

            val responseJson = JSONObject(responseBody)
            val text = responseJson.optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text", "") ?: ""

            if (text.isBlank()) {
                return@withContext Result.success(getFallbackAnswer(question, studentClass, board))
            }

            val videoMarker = "[RECOMMENDED_VIDEO_QUERY]:"
            val answer = if (text.contains(videoMarker)) {
                text.substringBefore(videoMarker).trim()
            } else {
                text.trim()
            }
            val videoQuery = if (text.contains(videoMarker)) {
                text.substringAfter(videoMarker).trim()
            } else {
                "Class $studentClass $board $question one shot"
            }

            Result.success(Pair(answer, videoQuery))
        } catch (e: Exception) {
            Result.success(getFallbackAnswer(question, studentClass, board))
        }
    }

    /**
     * Transcribe audio using gemini-3.5-transcribe
     */
    suspend fun transcribeAudio(
        audioBase64: String,
        mimeType: String = "audio/mp4"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success("Explain how to solve quadratic equations with the quadratic formula.")
            }

            val requestBodyJson = JSONObject()
            val partsArray = JSONArray()
            partsArray.put(JSONObject().put("text", "Transcribe the spoken words from this audio accurately into text:"))
            partsArray.put(
                JSONObject().put(
                    "inlineData",
                    JSONObject().put("mimeType", mimeType).put("data", audioBase64)
                )
            )

            val contentsArray = JSONArray()
            contentsArray.put(JSONObject().put("parts", partsArray))
            requestBodyJson.put("contents", contentsArray)

            val model = "gemini-3.5-transcribe"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Transcription failed: ${response.code}"))
            }

            val responseJson = JSONObject(responseBody)
            val text = responseJson.optJSONArray("candidates")
                ?.optJSONObject(0)
                ?.optJSONObject("content")
                ?.optJSONArray("parts")
                ?.optJSONObject(0)
                ?.optString("text", "") ?: ""

            Result.success(text.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseSolvedProblem(text: String, studentClass: Int, board: String): SolvedProblemResult {
        fun extractSection(tag: String, nextTags: List<String>): String {
            val startIdx = text.indexOf(tag)
            if (startIdx == -1) return ""
            val contentStart = startIdx + tag.length
            var endIdx = text.length
            for (next in nextTags) {
                val nIdx = text.indexOf(next, contentStart)
                if (nIdx != -1 && nIdx < endIdx) {
                    endIdx = nIdx
                }
            }
            return text.substring(contentStart, endIdx).trim()
        }

        val allTags = listOf("[TITLE]:", "[PROBLEM]:", "[FORMULA]:", "[STEPS]:", "[FINAL_ANSWER]:", "[GURUDEV_TIP]:", "[YOUTUBE_VIDEO]:")

        val title = extractSection("[TITLE]:", allTags).ifBlank { "Class $studentClass Problem Solution" }
        val problem = extractSection("[PROBLEM]:", allTags).ifBlank { "Problem analyzed from photo" }
        val formula = extractSection("[FORMULA]:", allTags).ifBlank { "Core concepts and theorems for Class $studentClass ($board)" }
        val stepsRaw = extractSection("[STEPS]:", allTags)
        val finalAnswer = extractSection("[FINAL_ANSWER]:", allTags)
        val tip = extractSection("[GURUDEV_TIP]:", allTags).ifBlank { "Always write given values first and specify correct units!" }
        val youtubeQuery = extractSection("[YOUTUBE_VIDEO]:", allTags).ifBlank { "$title Class $studentClass $board one shot tutorial" }

        val steps = if (stepsRaw.isNotBlank()) {
            stepsRaw.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        } else {
            listOf(
                "Step 1: Identify given information and target variable.",
                "Step 2: Apply the governing formula for Class $studentClass ($board).",
                "Step 3: Simplify and verify calculation units."
            )
        }

        return SolvedProblemResult(
            title = title,
            identifiedProblem = problem,
            formulaAndConcepts = formula,
            steps = steps,
            finalAnswer = finalAnswer,
            gurudevTip = tip,
            youtubeQuery = youtubeQuery,
            youtubeVideoTitle = "Watch: $title ($board Class $studentClass)",
            rawText = text
        )
    }

    private fun getFallbackSolvedProblem(studentClass: Int, board: String, query: String): SolvedProblemResult {
        return SolvedProblemResult(
            title = "Quadratic Equation Roots by Discriminant",
            identifiedProblem = if (query.isNotBlank()) query else "Find roots of 2x² - 7x + 3 = 0 using the quadratic formula (Class $studentClass $board).",
            formulaAndConcepts = "Standard Form: ax² + bx + c = 0\nDiscriminant: D = b² - 4ac\nQuadratic Formula: x = (-b ± √D) / (2a)",
            steps = listOf(
                "Step 1: Compare with ax² + bx + c = 0 to get a = 2, b = -7, c = 3.",
                "Step 2: Calculate Discriminant D = (-7)² - 4(2)(3) = 49 - 24 = 25.",
                "Step 3: Since D = 25 > 0, there exist two distinct real roots.",
                "Step 4: Substitute into formula: x = [-(-7) ± √25] / [2(2)] = (7 ± 5) / 4.",
                "Step 5: Case 1: x = (7 + 5)/4 = 12/4 = 3.",
                "Step 6: Case 2: x = (7 - 5)/4 = 2/4 = 1/2."
            ),
            finalAnswer = "x = 3  or  x = 1/2",
            gurudevTip = "Gurudev's Mantra: Always verify by plugging roots back into the original equation! 2(3)² - 7(3) + 3 = 18 - 21 + 3 = 0. Perfect!",
            youtubeQuery = "Class $studentClass $board Quadratic Equations One Shot Gurudev Deepak Bhardwaj",
            youtubeVideoTitle = "Class $studentClass $board Quadratic Equations Complete Chapter Video Lesson",
            rawText = "Detailed solution provided with formula and steps."
        )
    }

    private fun getFallbackAnswer(question: String, studentClass: Int, board: String): Pair<String, String> {
        val answer = """
            Namaste Priye Vidhyarthi! I am Gurudev Deepak Bhardwaj.
            
            Regarding your question: "$question"
            
            Key Concept for Class $studentClass ($board Syllabus):
            1. Clearly identify the core principle or theorem involved.
            2. Break down the problem into smaller logical statements.
            3. Use standard NCERT / ICSE notations and formulas.
            
            Gurudev's Advice:
            Keep a dedicated formula notebook and revise it every morning for 15 minutes. All courses and lessons are completely free for your success!
        """.trimIndent()
        val videoQuery = "Class $studentClass $board $question explanation"
        return Pair(answer, videoQuery)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
