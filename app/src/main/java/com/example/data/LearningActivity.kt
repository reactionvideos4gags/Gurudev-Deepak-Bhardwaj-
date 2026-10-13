package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_activities")
data class LearningActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "PHOTO_PROBLEM", "QUESTION_ANSWER", "TOPIC_STUDIED"
    val title: String,
    val problemText: String,
    val subject: String,
    val gradeLevel: String, // "Class 1" .. "Class 10"
    val board: String, // "CBSE" or "ICSE"
    val stepSolution: String,
    val formulaUsed: String,
    val guruTip: String,
    val youtubeUrl: String,
    val youtubeTitle: String,
    val wasSharedToWhatsApp: Boolean = false
)
