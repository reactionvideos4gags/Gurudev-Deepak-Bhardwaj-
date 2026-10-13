package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PracticeProblem
import com.example.data.SubjectModulesCatalog
import com.example.data.TopicModule
import com.example.ui.MainViewModel
import com.example.ui.components.FormulaCard
import com.example.ui.components.StepByStepCard
import com.example.ui.theme.AmberSaffron
import com.example.ui.theme.ForestEmerald
import com.example.ui.theme.GuruIndigo
import com.example.util.WhatsAppHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubjectModulesScreen(
    viewModel: MainViewModel,
    onAskDoubtAboutProblem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedClass by viewModel.selectedFilterClass.collectAsState()
    val allProgress by viewModel.allProgress.collectAsState()
    val activeQuizModule by viewModel.activeQuizModule.collectAsState()

    val modules = viewModel.getModulesForCurrentSelection()

    // Interactive Quiz Dialog
    if (activeQuizModule != null) {
        val module = activeQuizModule!!
        QuizDialog(
            module = module,
            viewModel = viewModel,
            onDismiss = { viewModel.closeQuiz() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("subject_modules_screen")
    ) {
        // Top Filter Bar (Subject + Grade)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Select Subject & Grade Level",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Relevant learning modules, formulas & practice problems",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Subject Selector Tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SubjectModulesCatalog.availableSubjects) { subject ->
                        val isSelected = selectedSubject == subject
                        val icon = when (subject) {
                            "Mathematics" -> "📐"
                            "Physics" -> "⚡"
                            "Chemistry" -> "🧪"
                            "Biology" -> "🧬"
                            else -> "📚"
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) GuruIndigo else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { viewModel.updateSubject(subject) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("subject_tab_$subject")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = icon, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = subject,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Grade Selector (Class 1 to 10)
                Text(
                    text = "Select Grade Level (Class 1 to 10):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (1..10).forEach { grade ->
                        val isSelected = selectedClass == grade
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) AmberSaffron else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable { viewModel.updateClass(grade) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("grade_chip_$grade")
                        ) {
                            Text(
                                text = "Class $grade",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Module Items List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (modules.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No modules found for $selectedSubject in Class $selectedClass. Select another subject or class!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(modules, key = { it.id }) { module ->
                    val progress = allProgress.firstOrNull { it.lessonId == module.id }
                    val isCompleted = progress?.isCompleted ?: false
                    val quizScore = progress?.quizScore
                    val quizTotal = progress?.quizTotal

                    ModuleCard(
                        module = module,
                        isCompleted = isCompleted,
                        quizScore = quizScore,
                        quizTotal = quizTotal,
                        onToggleCompleted = { viewModel.toggleLessonCompletion(module) },
                        onStartQuiz = { viewModel.startQuiz(module) },
                        onWatchVideo = {
                            WhatsAppHelper.openYouTubeSearch(context, module.youtubeSearchQuery)
                        },
                        onAskDoubtAboutProblem = onAskDoubtAboutProblem
                    )
                }
            }
        }
    }
}

@Composable
fun ModuleCard(
    module: TopicModule,
    isCompleted: Boolean,
    quizScore: Int?,
    quizTotal: Int?,
    onToggleCompleted: () -> Unit,
    onStartQuiz: () -> Unit,
    onWatchVideo: () -> Unit,
    onAskDoubtAboutProblem: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("module_card_${module.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Subject, Grade & Completion Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GuruIndigo.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = GuruIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${module.subject} • Class ${module.classLevel}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = GuruIndigo
                    )
                }

                // Completion Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isCompleted) ForestEmerald.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onToggleCompleted() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("toggle_complete_${module.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = null,
                            tint = if (isCompleted) ForestEmerald else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCompleted) "Completed" else "Mark Complete",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isCompleted) ForestEmerald else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.topicTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = module.chapterDescription,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Formulas Section
            if (module.formulas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                FormulaCard(formulaText = module.formulas.joinToString("\n• ", prefix = "• "))
            }

            // Quiz score display if already taken
            if (quizScore != null && quizTotal != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (quizScore.toDouble() / quizTotal >= 0.7) ForestEmerald.copy(alpha = 0.1f)
                            else Color(0xFFFEF2F2)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = if (quizScore.toDouble() / quizTotal >= 0.7) ForestEmerald else Color(0xFFDC2626),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Quiz Score: $quizScore / $quizTotal (${(quizScore.toDouble() / quizTotal * 100).toInt()}%)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (quizScore.toDouble() / quizTotal >= 0.7) ForestEmerald else Color(0xFF991B1B)
                    )
                }
            }

            // Practice Problems Section
            if (module.practiceProblems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Practice Problems (${module.practiceProblems.size}):",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                module.practiceProblems.forEach { problem ->
                    PracticeProblemItem(
                        problem = problem,
                        onAskDoubt = { onAskDoubtAboutProblem(problem.problemStatement) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons (Quiz + YouTube Video)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = AmberSaffron),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("take_quiz_button_${module.id}")
                ) {
                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Take Topic Quiz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onWatchVideo,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("module_video_button_${module.id}")
                ) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Watch Video", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFDC2626))
                }
            }
        }
    }
}

@Composable
fun PracticeProblemItem(
    problem: PracticeProblem,
    onAskDoubt: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GuruIndigo.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = problem.difficulty,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = GuruIndigo
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = problem.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { isExpanded = !isExpanded }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand"
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = problem.problemStatement,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    // Hint
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(AmberSaffron.copy(alpha = 0.1f))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = AmberSaffron, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Hint: ${problem.hint}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    StepByStepCard(
                        steps = problem.stepByStepSolution,
                        finalAnswer = problem.finalAnswer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = onAskDoubt) {
                        Icon(Icons.Default.Help, contentDescription = null, modifier = Modifier.size(14.dp), tint = GuruIndigo)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ask Gurudev about this problem", fontSize = 11.5.sp, color = GuruIndigo)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizDialog(
    module: TopicModule,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val selectedOptions by viewModel.quizSelectedOption.collectAsState()
    val submittedScore by viewModel.quizSubmittedScore.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Quiz: ${module.topicTitle}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = GuruIndigo
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (submittedScore != null) {
                    val (score, total) = submittedScore!!
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (score.toDouble() / total >= 0.7) ForestEmerald.copy(alpha = 0.15f)
                                    else Color(0xFFFEF2F2)
                                )
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (score.toDouble() / total >= 0.7) "🎉 Great Mastery!" else "📚 Needs More Practice!",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (score.toDouble() / total >= 0.7) ForestEmerald else Color(0xFFDC2626)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your Score: $score / $total",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                items(module.quiz.indices.toList()) { qIdx ->
                    val q = module.quiz[qIdx]
                    val chosen = selectedOptions[qIdx]
                    val isGraded = submittedScore != null

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Q${qIdx + 1}: ${q.question}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        q.options.forEachIndexed { optIdx, optionText ->
                            val isSelected = chosen == optIdx
                            val isCorrectAnswer = q.correctIndex == optIdx

                            val bgColor = when {
                                !isGraded && isSelected -> GuruIndigo.copy(alpha = 0.1f)
                                isGraded && isCorrectAnswer -> ForestEmerald.copy(alpha = 0.15f)
                                isGraded && isSelected && !isCorrectAnswer -> Color(0xFFFCA5A5)
                                else -> Color.Transparent
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(bgColor)
                                    .clickable(enabled = !isGraded) {
                                        viewModel.answerQuizQuestion(qIdx, optIdx)
                                    }
                                    .padding(vertical = 4.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = if (!isGraded) {
                                        { viewModel.answerQuizQuestion(qIdx, optIdx) }
                                    } else null
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (isGraded) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💡 Explanation: ${q.explanation}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (submittedScore == null) {
                Button(
                    onClick = { viewModel.submitQuiz(module) },
                    colors = ButtonDefaults.buttonColors(containerColor = GuruIndigo)
                ) {
                    Text("Submit Quiz")
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestEmerald)
                ) {
                    Text("Done")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
