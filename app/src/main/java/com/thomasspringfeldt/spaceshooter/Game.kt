package com.thomasspringfeldt.spaceshooter

import android.content.Context
import android.view.SurfaceView

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context?) : SurfaceView(context), Runnable {

    /**
     * Game loop.
     */
    override fun run() {
        TODO("Not yet implemented")
    }

    /**
     * Stops the game from processing.
     */
    fun pause() {

    }

    /**
     * Resumes the processing of the game.
     */
    fun resume() {
        //start the new thread
        //set isRunning = true
        //start the thread
    }

}