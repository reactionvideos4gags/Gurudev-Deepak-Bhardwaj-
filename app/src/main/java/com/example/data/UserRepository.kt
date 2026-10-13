package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserProfile(
    val isLoggedIn: Boolean = false,
    val mobileNumber: String = "",
    val studentName: String = "",
    val studentClass: Int = 10, // 1 to 10
    val board: String = "CBSE", // "CBSE" or "ICSE"
    val isParentWhatsAppLinked: Boolean = true
)

class UserRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("gurudev_user_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private fun loadProfile(): UserProfile {
        return UserProfile(
            isLoggedIn = prefs.getBoolean("is_logged_in", false),
            mobileNumber = prefs.getString("mobile_number", "") ?: "",
            studentName = prefs.getString("student_name", "Arjun Sharma") ?: "Arjun Sharma",
            studentClass = prefs.getInt("student_class", 10),
            board = prefs.getString("board", "CBSE") ?: "CBSE",
            isParentWhatsAppLinked = prefs.getBoolean("is_parent_wa_linked", true)
        )
    }

    fun saveProfile(profile: UserProfile) {
        prefs.edit()
            .putBoolean("is_logged_in", profile.isLoggedIn)
            .putString("mobile_number", profile.mobileNumber)
            .putString("student_name", profile.studentName)
            .putInt("student_class", profile.studentClass)
            .putString("board", profile.board)
            .putBoolean("is_parent_wa_linked", profile.isParentWhatsAppLinked)
            .apply()
        _userProfile.value = profile
    }

    fun updateClassAndBoard(newClass: Int, newBoard: String) {
        val current = _userProfile.value
        val updated = current.copy(studentClass = newClass, board = newBoard)
        saveProfile(updated)
    }

    fun logout() {
        prefs.edit().clear().apply()
        _userProfile.value = UserProfile(isLoggedIn = false)
    }
}
