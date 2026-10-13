package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ChapterCourse
import com.example.data.CourseCatalog
import com.example.data.LearningActivity
import com.example.data.LessonProgress
import com.example.data.PracticeProblem
import com.example.data.StudentDoubt
import com.example.data.SubjectModulesCatalog
import com.example.data.TopicModule
import com.example.data.UserProfile
import com.example.data.UserRepository
import com.example.network.GeminiTutorService
import com.example.network.SolvedProblemResult
import com.example.util.AudioRecorderHelper
import com.example.util.WhatsAppHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString() + "_" + Random.nextInt(1000),
    val text: String,
    val isFromGurudev: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val youtubeVideoQuery: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo = UserRepository(application)
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.learningDao()
    private val geminiService = GeminiTutorService()
    val audioRecorder = AudioRecorderHelper(application)

    // User profile state
    val userProfile: StateFlow<UserProfile> = userRepo.userProfile

    // Learning activities from Room
    val activities: StateFlow<List<LearningActivity>> = dao.getAllActivities()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Progress tracking from Room
    val allProgress: StateFlow<List<LessonProgress>> = dao.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Frequently Asked Questions / Doubts from Room
    val frequentDoubts: StateFlow<List<StudentDoubt>> = dao.getFrequentlyAskedQuestions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // OTP Auth State
    val mobileNumberInput = MutableStateFlow("")
    val studentNameInput = MutableStateFlow("Arjun Sharma")
    val otpInput = MutableStateFlow("")
    val generatedOtp = MutableStateFlow<String?>(null)
    val isOtpSent = MutableStateFlow(false)
    val otpError = MutableStateFlow<String?>(null)
    val isWhatsAppAvailable = MutableStateFlow(WhatsAppHelper.isWhatsAppInstalled(application))

    // Active Tab Navigation
    // 0 = Photo Solver, 1 = Ask Doubt, 2 = Modules & Practice, 3 = Progress Dashboard, 4 = WhatsApp Report
    val selectedTab = MutableStateFlow(0)

    // Problem Solver State
    val selectedBitmap = MutableStateFlow<Bitmap?>(null)
    val problemNoteInput = MutableStateFlow("")
    val isSolvingProblem = MutableStateFlow(false)
    val latestSolvedResult = MutableStateFlow<SolvedProblemResult?>(null)
    val solverError = MutableStateFlow<String?>(null)

    // Ask Question (Chat) State
    val chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Namaste Priye Vidhyarthi! I am Gurudev Deepak Bhardwaj. Ask me any question from Class 1 to 10 CBSE & ICSE syllabus or tap the mic to speak. All lessons are free!",
                isFromGurudev = true
            )
        )
    )
    val questionInput = MutableStateFlow("")
    val isAnsweringQuestion = MutableStateFlow(false)
    val isRecordingVoice = MutableStateFlow(false)
    val voiceTranscriptionStatus = MutableStateFlow<String?>(null)

    // Subject & Grade Explorer Filter
    val selectedSubject = MutableStateFlow("Mathematics")
    val selectedFilterClass = MutableStateFlow(10)
    val selectedFilterBoard = MutableStateFlow("CBSE")

    // Active Interactive Practice Problem & Quiz Modal
    val activePracticeProblem = MutableStateFlow<PracticeProblem?>(null)
    val activeQuizModule = MutableStateFlow<TopicModule?>(null)
    val quizSelectedOption = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quizSubmittedScore = MutableStateFlow<Pair<Int, Int>?>(null)

    init {
        selectedFilterClass.value = userProfile.value.studentClass
        selectedFilterBoard.value = userProfile.value.board
        if (userProfile.value.mobileNumber.isNotBlank()) {
            mobileNumberInput.value = userProfile.value.mobileNumber
        }
    }

    fun refreshWhatsAppStatus() {
        isWhatsAppAvailable.value = WhatsAppHelper.isWhatsAppInstalled(getApplication())
    }

    // OTP Flow
    fun sendOtp() {
        val cleanNumber = mobileNumberInput.value.trim().replace("[^0-9]".toRegex(), "")
        if (cleanNumber.length != 10) {
            otpError.value = "Please enter a valid 10-digit mobile number"
            return
        }
        val otp = (100000 + Random.nextInt(900000)).toString()
        generatedOtp.value = otp
        isOtpSent.value = true
        otpError.value = null
        otpInput.value = otp
    }

    fun verifyOtp(enteredOtp: String = otpInput.value) {
        if (enteredOtp.trim() == generatedOtp.value) {
            val updated = UserProfile(
                isLoggedIn = true,
                mobileNumber = mobileNumberInput.value.trim(),
                studentName = studentNameInput.value.trim().ifBlank { "Arjun Sharma" },
                studentClass = selectedFilterClass.value,
                board = selectedFilterBoard.value,
                isParentWhatsAppLinked = true
            )
            userRepo.saveProfile(updated)
            otpError.value = null
        } else {
            otpError.value = "Invalid OTP code. Please enter the 6-digit code received."
        }
    }

    fun updateClass(newClass: Int) {
        selectedFilterClass.value = newClass
        userRepo.updateClassAndBoard(newClass, selectedFilterBoard.value)
    }

    fun updateBoard(newBoard: String) {
        selectedFilterBoard.value = newBoard
        userRepo.updateClassAndBoard(selectedFilterClass.value, newBoard)
    }

    fun updateSubject(subject: String) {
        selectedSubject.value = subject
    }

    fun updateStudentName(name: String) {
        studentNameInput.value = name
        val current = userProfile.value
        userRepo.saveProfile(current.copy(studentName = name))
    }

    fun setSelectedBitmap(bitmap: Bitmap?) {
        selectedBitmap.value = bitmap
    }

    // Problem Solver
    fun solveProblem() {
        val bitmap = selectedBitmap.value
        val note = problemNoteInput.value
        if (bitmap == null && note.isBlank()) {
            solverError.value = "Please upload/capture a problem photograph or type the problem statement."
            return
        }

        solverError.value = null
        isSolvingProblem.value = true

        viewModelScope.launch {
            val currentProfile = userProfile.value
            val result = geminiService.solveProblemFromPhoto(
                bitmap = bitmap,
                additionalPrompt = note,
                studentClass = currentProfile.studentClass,
                board = currentProfile.board
            )

            isSolvingProblem.value = false
            result.onSuccess { solved ->
                latestSolvedResult.value = solved
                val activity = LearningActivity(
                    type = "PHOTO_PROBLEM",
                    title = solved.title,
                    problemText = solved.identifiedProblem,
                    subject = "Class ${currentProfile.studentClass} ${currentProfile.board}",
                    gradeLevel = "Class ${currentProfile.studentClass}",
                    board = currentProfile.board,
                    stepSolution = solved.steps.joinToString("\n"),
                    formulaUsed = solved.formulaAndConcepts,
                    guruTip = solved.gurudevTip,
                    youtubeUrl = "https://www.youtube.com/results?search_query=${solved.youtubeQuery.replace(" ", "+")}",
                    youtubeTitle = solved.youtubeVideoTitle
                )
                dao.insert(activity)
            }.onFailure { err ->
                solverError.value = "Problem solving failed: ${err.message}"
            }
        }
    }

    // Ask Question with Doubt Tracking
    fun askQuestion(query: String = questionInput.value) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(text = query, isFromGurudev = false)
        chatMessages.value = chatMessages.value + userMsg
        questionInput.value = ""
        isAnsweringQuestion.value = true

        // Record student doubt into FAQ table
        viewModelScope.launch {
            val currentProfile = userProfile.value
            dao.insertDoubt(
                StudentDoubt(
                    questionText = query,
                    subject = selectedSubject.value,
                    classLevel = currentProfile.studentClass,
                    board = currentProfile.board
                )
            )
        }

        viewModelScope.launch {
            val currentProfile = userProfile.value
            val result = geminiService.askGurudevQuestion(
                question = query,
                studentClass = currentProfile.studentClass,
                board = currentProfile.board
            )

            isAnsweringQuestion.value = false
            result.onSuccess { (answer, videoQuery) ->
                val guruMsg = ChatMessage(
                    text = answer,
                    isFromGurudev = true,
                    youtubeVideoQuery = videoQuery
                )
                chatMessages.value = chatMessages.value + guruMsg

                val activity = LearningActivity(
                    type = "QUESTION_ANSWER",
                    title = query.take(40),
                    problemText = query,
                    subject = selectedSubject.value,
                    gradeLevel = "Class ${currentProfile.studentClass}",
                    board = currentProfile.board,
                    stepSolution = answer,
                    formulaUsed = "Core CBSE/ICSE Concept",
                    guruTip = "Gurudev's Guidance: Review related textbook chapter exercises.",
                    youtubeUrl = "https://www.youtube.com/results?search_query=${videoQuery.replace(" ", "+")}",
                    youtubeTitle = "Video Lesson: $videoQuery"
                )
                dao.insert(activity)
            }.onFailure {
                val errorMsg = ChatMessage(
                    text = "Priye Vidhyarthi, let me review your doubt again. Please ensure you are connected to the internet.",
                    isFromGurudev = true
                )
                chatMessages.value = chatMessages.value + errorMsg
            }
        }
    }

    // Voice recording & transcription
    fun startVoiceRecording() {
        val success = audioRecorder.startRecording()
        if (success) {
            isRecordingVoice.value = true
            voiceTranscriptionStatus.value = "Listening to your question..."
        } else {
            voiceTranscriptionStatus.value = "Microphone access needed."
        }
    }

    fun stopVoiceRecordingAndTranscribe() {
        isRecordingVoice.value = false
        voiceTranscriptionStatus.value = "Transcribing with gemini-3.5-transcribe..."

        viewModelScope.launch {
            val base64Audio = audioRecorder.stopRecordingAndGetBase64()
            if (base64Audio != null) {
                val result = geminiService.transcribeAudio(base64Audio)
                result.onSuccess { transcribedText ->
                    voiceTranscriptionStatus.value = null
                    if (transcribedText.isNotBlank()) {
                        questionInput.value = transcribedText
                        askQuestion(transcribedText)
                    }
                }.onFailure {
                    voiceTranscriptionStatus.value = "Could not transcribe audio. Please try again or type."
                }
            } else {
                voiceTranscriptionStatus.value = null
            }
        }
    }

    fun cancelVoiceRecording() {
        audioRecorder.cancel()
        isRecordingVoice.value = false
        voiceTranscriptionStatus.value = null
    }

    fun onSpeechResult(text: String) {
        if (text.isNotBlank()) {
            questionInput.value = text
            askQuestion(text)
        }
    }

    // Progress Tracking Actions
    fun toggleLessonCompletion(module: TopicModule) {
        viewModelScope.launch {
            val existing = allProgress.value.firstOrNull { it.lessonId == module.id }
            val newCompleted = !(existing?.isCompleted ?: false)
            val updated = LessonProgress(
                lessonId = module.id,
                classLevel = module.classLevel,
                subject = module.subject,
                topicTitle = module.topicTitle,
                board = module.board,
                isCompleted = newCompleted,
                quizScore = existing?.quizScore,
                quizTotal = existing?.quizTotal,
                needsAttention = existing?.needsAttention ?: false,
                completedTimestamp = if (newCompleted) System.currentTimeMillis() else null
            )
            dao.insertOrUpdateProgress(updated)
        }
    }

    fun startQuiz(module: TopicModule) {
        activeQuizModule.value = module
        quizSelectedOption.value = emptyMap()
        quizSubmittedScore.value = null
    }

    fun answerQuizQuestion(questionIndex: Int, optionIndex: Int) {
        val current = quizSelectedOption.value.toMutableMap()
        current[questionIndex] = optionIndex
        quizSelectedOption.value = current
    }

    fun submitQuiz(module: TopicModule) {
        val answers = quizSelectedOption.value
        var score = 0
        module.quiz.forEachIndexed { index, q ->
            if (answers[index] == q.correctIndex) {
                score++
            }
        }
        val total = module.quiz.size
        quizSubmittedScore.value = Pair(score, total)

        // If score < 70%, mark as needs attention
        val percentage = if (total > 0) (score.toDouble() / total) * 100 else 100.0
        val needsAttention = percentage < 70.0

        viewModelScope.launch {
            val existing = allProgress.value.firstOrNull { it.lessonId == module.id }
            val progress = LessonProgress(
                lessonId = module.id,
                classLevel = module.classLevel,
                subject = module.subject,
                topicTitle = module.topicTitle,
                board = module.board,
                isCompleted = existing?.isCompleted ?: (score == total),
                quizScore = score,
                quizTotal = total,
                needsAttention = needsAttention,
                completedTimestamp = existing?.completedTimestamp
            )
            dao.insertOrUpdateProgress(progress)
        }
    }

    fun closeQuiz() {
        activeQuizModule.value = null
        quizSelectedOption.value = emptyMap()
        quizSubmittedScore.value = null
    }

    // Learning Modules by Selection
    fun getModulesForCurrentSelection(): List<TopicModule> {
        return SubjectModulesCatalog.getModules(selectedFilterClass.value, selectedSubject.value)
    }

    fun getFilteredCourses(): List<ChapterCourse> {
        return CourseCatalog.getChaptersForClass(selectedFilterClass.value, selectedFilterBoard.value)
    }

    fun logout() {
        userRepo.logout()
        mobileNumberInput.value = ""
        isOtpSent.value = false
        generatedOtp.value = null
    }
}
