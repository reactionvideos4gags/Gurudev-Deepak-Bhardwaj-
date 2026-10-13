package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.ClassPickerDialog
import com.example.ui.components.GurudevHeader
import com.example.ui.screens.AskQuestionScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ProgressDashboardScreen
import com.example.ui.screens.ReportScreen
import com.example.ui.screens.SolveProblemScreen
import com.example.ui.screens.SubjectModulesScreen
import com.example.ui.theme.GuruIndigo
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                GurudevApp(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshWhatsAppStatus()
    }
}

@Composable
fun GurudevApp(viewModel: MainViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isWhatsAppAvailable by viewModel.isWhatsAppAvailable.collectAsState()

    var showClassPicker by remember { mutableStateOf(false) }

    if (!userProfile.isLoggedIn) {
        AuthScreen(viewModel = viewModel)
    } else {
        // BackHandler to return to home screen (tab 0)
        BackHandler(enabled = selectedTab != 0) {
            viewModel.selectedTab.value = 0
        }

        if (showClassPicker) {
            ClassPickerDialog(
                currentClass = userProfile.studentClass,
                currentBoard = userProfile.board,
                onDismiss = { showClassPicker = false },
                onConfirm = { newClass, newBoard ->
                    viewModel.updateClass(newClass)
                    viewModel.updateBoard(newBoard)
                    showClassPicker = false
                }
            )
        }

        Scaffold(
            topBar = {
                GurudevHeader(
                    studentClass = userProfile.studentClass,
                    board = userProfile.board,
                    studentName = userProfile.studentName,
                    isWhatsAppReady = isWhatsAppAvailable,
                    onChangeClassClick = { showClassPicker = true }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { viewModel.selectedTab.value = 0 },
                        icon = { Icon(Icons.Default.CameraAlt, contentDescription = "Photo Solve") },
                        label = { Text("Photo Solve", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuruIndigo,
                            selectedTextColor = GuruIndigo,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_photo_solve")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { viewModel.selectedTab.value = 1 },
                        icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = "Ask Doubt") },
                        label = { Text("Ask Doubt", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuruIndigo,
                            selectedTextColor = GuruIndigo,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_ask_doubt")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { viewModel.selectedTab.value = 2 },
                        icon = { Icon(Icons.Default.Science, contentDescription = "Modules") },
                        label = { Text("Modules", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuruIndigo,
                            selectedTextColor = GuruIndigo,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_subject_modules")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { viewModel.selectedTab.value = 3 },
                        icon = { Icon(Icons.Default.Insights, contentDescription = "Progress") },
                        label = { Text("Progress", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuruIndigo,
                            selectedTextColor = GuruIndigo,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_progress_dashboard")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { viewModel.selectedTab.value = 4 },
                        icon = { Icon(Icons.Default.Assessment, contentDescription = "WhatsApp") },
                        label = { Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GuruIndigo,
                            selectedTextColor = GuruIndigo,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_wa_report")
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> SolveProblemScreen(viewModel = viewModel)
                    1 -> AskQuestionScreen(viewModel = viewModel)
                    2 -> SubjectModulesScreen(
                        viewModel = viewModel,
                        onAskDoubtAboutProblem = { problemStatement ->
                            viewModel.questionInput.value = "How do I solve this problem: $problemStatement"
                            viewModel.selectedTab.value = 1
                        }
                    )
                    3 -> ProgressDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToSubjectModules = { viewModel.selectedTab.value = 2 },
                        onAskGurudevDoubt = { doubtText ->
                            viewModel.questionInput.value = doubtText
                            viewModel.selectedTab.value = 1
                        }
                    )
                    4 -> ReportScreen(viewModel = viewModel)
                }
            }
        }
    }
}
