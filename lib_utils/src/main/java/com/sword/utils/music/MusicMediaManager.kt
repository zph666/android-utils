package com.sword.utils.music

import android.media.MediaPlayer
import com.sword.utils.spf.SPFContext

/**
 * @author houqiang
 * @since 2023/5/22
 */
object MusicMediaManager {

    var mediaPlayerCache: MediaPlayer? = null
    var isMusicVoice = true
    fun showSound(rawId: Int, stop: Boolean = false) {
        if (stop) {
            stopAllSounds()
        }
        val context = SPFContext.application
        val mediaPlayer = MediaPlayer.create(context, rawId)
        mediaPlayerCache = mediaPlayer
        // 设置音量
        val volume = if (isMusicVoice) 1f else 0f
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
        mediaPlayer.start()
    }

    fun stopAllSounds() {
        mediaPlayerCache?.release()
    }

}