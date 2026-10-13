package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: LearningActivity): Long

    @Query("SELECT * FROM learning_activities ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<LearningActivity>>

    @Query("SELECT * FROM learning_activities WHERE wasSharedToWhatsApp = 0 ORDER BY timestamp DESC")
    fun getUnreportedActivities(): Flow<List<LearningActivity>>

    @Query("UPDATE learning_activities SET wasSharedToWhatsApp = 1")
    suspend fun markAllAsReported()

    @Delete
    suspend fun delete(activity: LearningActivity)

    @Query("DELETE FROM learning_activities")
    suspend fun clearAll()

    // Lesson Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: LessonProgress)

    @Query("SELECT * FROM lesson_progress")
    fun getAllProgress(): Flow<List<LessonProgress>>

    @Query("SELECT * FROM lesson_progress WHERE classLevel = :classLevel")
    fun getProgressForClass(classLevel: Int): Flow<List<LessonProgress>>

    @Query("UPDATE lesson_progress SET isCompleted = :isCompleted, completedTimestamp = :timestamp WHERE lessonId = :lessonId")
    suspend fun markLessonCompleted(lessonId: String, isCompleted: Boolean, timestamp: Long)

    @Query("UPDATE lesson_progress SET quizScore = :score, quizTotal = :total, needsAttention = :needsAttention WHERE lessonId = :lessonId")
    suspend fun recordQuizScore(lessonId: String, score: Int, total: Int, needsAttention: Boolean)

    // Frequently Asked Questions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoubt(doubt: StudentDoubt): Long

    @Query("SELECT * FROM frequently_asked_questions ORDER BY timesAsked DESC, lastAskedTimestamp DESC LIMIT 10")
    fun getFrequentlyAskedQuestions(): Flow<List<StudentDoubt>>
}
