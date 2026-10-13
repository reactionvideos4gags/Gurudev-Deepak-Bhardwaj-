package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.ChatMessage
import com.example.ui.MainViewModel
import com.example.ui.components.YouTubeRecommendationCard
import com.example.ui.theme.AmberSaffron
import com.example.ui.theme.GuruIndigo
import com.example.util.WhatsAppHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AskQuestionScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val chatMessages by viewModel.chatMessages.collectAsState()
    val questionInput by viewModel.questionInput.collectAsState()
    val isAnswering by viewModel.isAnsweringQuestion.collectAsState()
    val isRecordingVoice by viewModel.isRecordingVoice.collectAsState()
    val transcriptionStatus by viewModel.voiceTranscriptionStatus.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    // Android Speech Recognizer launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                viewModel.onSpeechResult(spoken)
            }
        }
    }

    // Microphone permission launcher for gemini-3.5-transcribe
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceRecording()
        } else {
            Toast.makeText(context, "Microphone permission needed to transcribe audio", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ask_question_screen")
    ) {
        // Quick Doubt Suggestions bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Quick Doubts (Class ${userProfile.studentClass} ${userProfile.board}):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val sampleDoubts = when (userProfile.studentClass) {
                        in 1..5 -> listOf(
                            "What are prime numbers?",
                            "How do plants make food?",
                            "Difference between area & perimeter"
                        )
                        in 6..8 -> listOf(
                            "Explain Photosynthesis equation",
                            "What is Newton's First Law?",
                            "How to add negative numbers?"
                        )
                        else -> listOf(
                            "Prove Pythagoras Theorem",
                            "How to balance chemical equations?",
                            "Explain Snell's law of refraction",
                            "Tricks to remember Trigonometry values"
                        )
                    }

                    sampleDoubts.forEach { doubt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GuruIndigo.copy(alpha = 0.08f))
                                .clickable { viewModel.askQuestion(doubt) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = doubt,
                                style = MaterialTheme.typography.labelSmall,
                                color = GuruIndigo
                            )
                        }
                    }
                }
            }
        }

        // Chat messages LazyColumn
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages, key = { it.id }) { message ->
                ChatBubble(message = message)
            }

            if (isAnswering) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = GuruIndigo,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gurudev Deepak Bhardwaj is answering your question...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Voice Transcription Status banner if recording
        AnimatedVisibility(visible = transcriptionStatus != null || isRecordingVoice) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AmberSaffron.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isRecordingVoice) Icons.Default.Mic else Icons.Default.MicOff,
                            contentDescription = null,
                            tint = AmberSaffron,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = transcriptionStatus ?: "Listening...",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (isRecordingVoice) {
                        Button(
                            onClick = { viewModel.stopVoiceRecordingAndTranscribe() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Done (Transcribe)", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Bottom Input Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Microphone audio transcription button
                IconButton(
                    onClick = {
                        if (isRecordingVoice) {
                            viewModel.stopVoiceRecordingAndTranscribe()
                        } else {
                            val permission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            )
                            if (permission == PackageManager.PERMISSION_GRANTED) {
                                viewModel.startVoiceRecording()
                            } else {
                                recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isRecordingVoice) Color(0xFFDC2626) else GuruIndigo.copy(alpha = 0.1f)
                        )
                        .testTag("microphone_transcribe_button")
                ) {
                    Icon(
                        imageVector = if (isRecordingVoice) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "Microphone audio transcription with gemini-3.5-transcribe",
                        tint = if (isRecordingVoice) Color.White else GuruIndigo
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = questionInput,
                    onValueChange = { viewModel.questionInput.value = it },
                    placeholder = { Text("Ask any question or tap mic to speak…") },
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ask_question_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { viewModel.askQuestion() },
                    enabled = questionInput.isNotBlank() && !isAnswering,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (questionInput.isNotBlank()) GuruIndigo else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .testTag("send_question_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Question",
                        tint = if (questionInput.isNotBlank()) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isGuru = message.isFromGurudev

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isGuru) Arrangement.Start else Arrangement.End
    ) {
        if (isGuru) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AmberSaffron),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Gurudev",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (isGuru) 2.dp else 14.dp,
                    bottomEnd = if (isGuru) 14.dp else 2.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isGuru) MaterialTheme.colorScheme.surface else GuruIndigo
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    if (isGuru) {
                        Text(
                            text = "Gurudev Deepak Bhardwaj",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AmberSaffron
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        color = if (isGuru) MaterialTheme.colorScheme.onSurface else Color.White
                    )
                }
            }

            // Recommended YouTube Video Advisory if present
            if (message.youtubeVideoQuery != null) {
                Spacer(modifier = Modifier.height(8.dp))
                YouTubeRecommendationCard(
                    videoTitle = "Recommended Video Lesson on this topic",
                    searchQuery = message.youtubeVideoQuery
                )
            }
        }
    }
}

