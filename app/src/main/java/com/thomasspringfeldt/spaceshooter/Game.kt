package com.thomasspringfeldt.spaceshooter

import android.content.Context
import android.util.Log
import android.view.SurfaceView

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context?) : SurfaceView(context), Runnable {

    private val TAG = "Game"
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false

    /**
     * Game loop.
     */
    override fun run() {
        while(isRunning) {
            update()
            render()
        }
    }

    /**
     * Updates game state.
     */
    private fun update() {
        Log.d(TAG, "Updating")
    }

    /**
     * Renders game state.
     */
    private fun render() {
        Log.d(TAG, "Rendering")
    }

    /**
     * Stops the game from processing.
     */
    fun onPause() {
        Log.d(TAG, "onPause")
        isRunning = false
        gameThread.join()
    }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
        Log.d(TAG, "onResume")
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

}