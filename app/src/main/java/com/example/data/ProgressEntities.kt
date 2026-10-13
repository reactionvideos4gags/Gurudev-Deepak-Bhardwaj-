package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgress(
    @PrimaryKey
    val lessonId: String,
    val classLevel: Int,
    val subject: String,
    val topicTitle: String,
    val board: String,
    val isCompleted: Boolean = false,
    val quizScore: Int? = null,
    val quizTotal: Int? = null,
    val needsAttention: Boolean = false,
    val completedTimestamp: Long? = null
)

@Entity(tableName = "frequently_asked_questions")
data class StudentDoubt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionText: String,
    val subject: String,
    val classLevel: Int,
    val board: String,
    val timesAsked: Int = 1,
    val lastAskedTimestamp: Long = System.currentTimeMillis()
)

data class PracticeProblem(
    val id: String,
    val title: String,
    val problemStatement: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val formulaUsed: String,
    val stepByStepSolution: List<String>,
    val finalAnswer: String,
    val hint: String,
    val youtubeVideoQuery: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class TopicModule(
    val id: String,
    val classLevel: Int,
    val subject: String, // "Mathematics", "Physics", "Chemistry", "Biology"
    val board: String,
    val topicTitle: String,
    val chapterDescription: String,
    val formulas: List<String>,
    val practiceProblems: List<PracticeProblem>,
    val quiz: List<QuizQuestion>,
    val youtubeVideoTitle: String,
    val youtubeSearchQuery: String
)
