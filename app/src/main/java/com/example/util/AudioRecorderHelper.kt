package com.example.util

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Base64
import java.io.File
import java.io.FileInputStream

class AudioRecorderHelper(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun startRecording(): Boolean {
        return try {
            outputFile = File(context.cacheDir, "temp_voice_${System.currentTimeMillis()}.m4a")
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(64000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile?.absolutePath)
                prepare()
                start()
            }
            true
        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            false
        }
    }

    fun stopRecordingAndGetBase64(): String? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null

            val file = outputFile ?: return null
            if (!file.exists() || file.length() == 0L) return null

            val bytes = FileInputStream(file).use { it.readBytes() }
            file.delete()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            null
        }
    }

    fun cancel() {
        try {
            recorder?.apply {
                stop()
                release()
            }
        } catch (_: Exception) {}
        recorder = null
        outputFile?.delete()
    }
}
