package com.thomasspringfeldt.spaceshooter

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.io.IOException

object SFX{
    var crash = 0
    var boost = 0
    var death = 0
    var powerup = 0
    var shot_player = 0
    var shot_enemy = 0
    var start_game = 0
    var game_over = 0
}
const val MAX_STREAMS = 3

class Jukebox(private val assetManager: AssetManager) {
    private val tag = "Jukebox"
    private val soundPool: SoundPool
    init {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(MAX_STREAMS)
            .build()
        Log.d(tag, "soundpool created!")
        SFX.crash = loadSound("crash.wav")
        SFX.boost = loadSound("boost.wav")
        SFX.death = loadSound("death.wav")
        SFX.powerup = loadSound("powerup.wav")
        SFX.shot_player = loadSound("shot_player.wav")
        SFX.shot_enemy = loadSound("shot_enemy.wav")
        SFX.start_game = loadSound("start_game.wav")
        SFX.game_over = loadSound("game_over.wav")
    }

    private fun loadSound(fileName: String): Int{
        try {
            val descriptor: AssetFileDescriptor = assetManager.openFd(fileName)
            return soundPool.load(descriptor, 1)
        }catch(e: IOException){
            Log.d(tag, "Unable to load $fileName! Check the filename, and make sure it's in the assets-folder.")
        }
        return 0
    }

    fun play(soundID: Int) {
        val leftVolume = 1f
        val rightVolume = 1f
        val priority = 0
        val loop = 0
        val playbackRate = 1.0f
        if (soundID > 0) {
            soundPool.play(soundID, leftVolume, rightVolume, priority, loop, playbackRate)
        }
    }

    fun release() {
        soundPool.release()
        //the soundpool can no longer be used! you have to create a new soundpool.
    }
}