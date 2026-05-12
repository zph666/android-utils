package com.sword.utils.music

/**
 * @author houqiang
 * @since 2023/5/22
 */
object MusicPoolManager {

    fun playSound(audioUrl: String?, stop: Boolean? = false) {
        if (stop == true) {
            stopAllSounds()
        }
        MusicAudioManager.playAudio(audioUrl)
    }

    fun showSound(rawId: Int, stop: Boolean? = false) {
        if (stop == true) {
            stopAllSounds()
        }
        MusicMediaManager.showSound(rawId)
    }

    fun showSoundLong(rawId: Int, stop: Boolean? = false) {
        if (stop == true) {
            stopAllSounds()
        }
        MusicAudioManager.playRawAudio(rawId)
    }
    fun stopAllSounds() {
        MusicMediaManager.stopAllSounds()
        MusicAudioManager.stopAudio()
    }

}