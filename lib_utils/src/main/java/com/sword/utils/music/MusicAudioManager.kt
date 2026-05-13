package com.sword.utils.music

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import com.sword.utils.spf.SPFContext
import java.io.File

/**
 * 长音频管理工具
 */
@SuppressLint("StaticFieldLeak")
object MusicAudioManager {

    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath = ""
    var isMusicVoice = true

    fun playAudio(audioUrl: String?, isRound: Boolean = false) {
        if (audioUrl.isNullOrBlank()) return

        val source = audioUrl.trim()
        when {
            source.startsWith("content://") -> {
                playAudioInternal(source, "uri", isRound)
                return
            }

            source.startsWith("/") || source.startsWith("file://") -> {
                val path = source.removePrefix("file://")
                playAudioInternal(path, "file", isRound)
                return
            }

            source.startsWith("http://") || source.startsWith("https://") -> {
                var url = source
                val head = "https:/project.xiability.cn/"
                val newAudioUrl = "https://project.xiability.cn/"
                if (url.contains(head)) {
                    url = url.replace(head, newAudioUrl)
                }
                playAudioInternal(url, "url", isRound)
                return
            }
        }

        if (audioUrl != null) {
            audioFilePath = audioUrl
            val head = "https:/project.xiability.cn/"
            val newAudioUrl = "https://project.xiability.cn/"
            if (audioUrl.contains(head)) {
                audioFilePath = audioUrl.replace(head, newAudioUrl)
            }
        }
        playAudioInternal(audioFilePath, "path", isRound)
    }

    fun playAudioUri(uri: Uri?, isRound: Boolean = false) {
        if (uri == null) return
        playAudioInternal(uri.toString(), "uri", isRound)
    }

    fun playRawAudio(resourceId: Int, isRound: Boolean = false) {
        playAudioInternal(resourceId.toString(), "raw", isRound)
    }

    private fun playAudioInternal(source: String, type: String, isRound: Boolean = false) {
        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()
        try {
            if (type == "raw") {
                // 如果是 raw 资源文件
                val resourceId = source.toInt()
                val resources = SPFContext.application.resources
                val audioResource = resources.openRawResourceFd(resourceId)
                mediaPlayer!!.setDataSource(
                    audioResource.fileDescriptor,
                    audioResource.startOffset,
                    audioResource.length
                )
            } else if (type == "file") {
//                "播放本地音频 ${source}".log("AudioPlayer")
                val file = File(source)
//                "播放本地音频 ${file.absolutePath}".log("AudioPlayer")
                mediaPlayer!!.setDataSource(file.absolutePath)
            } else if (type == "uri") {
//                "播放Uri音频 $source".log("AudioPlayer")
                mediaPlayer!!.setDataSource(SPFContext.application, Uri.parse(source))
            } else {
//                "播放网络音频".log("AudioPlayer")
                mediaPlayer!!.setDataSource(source)
            }

            if (isMusicVoice) {
                mediaPlayer!!.setVolume(1f, 1f)
            } else {
                mediaPlayer!!.setVolume(0f, 0f)
            }

            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnPreparedListener {
                mediaPlayer!!.start()
            }
            mediaPlayer!!.setOnCompletionListener {
                if (isRound) {
                    mediaPlayer!!.start()
                }
            }
            mediaPlayer!!.setOnErrorListener { _, _, _ ->
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopAudio() {
        if (mediaPlayer != null) {
//            ("stopAudio: 暂停").log("AudioPlayer")
            mediaPlayer!!.pause()
        }
    }

}
