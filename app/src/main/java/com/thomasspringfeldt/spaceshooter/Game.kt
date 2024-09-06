package com.thomasspringfeldt.spaceshooter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context?) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private val TAG = "Game"
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false
    private val star = Star()

    init {
        holder?.addCallback(this)
    }

    /**
     * Game loop.
     */
    override fun run() {
        Log.d(TAG, "run()")
        while(isRunning) {
            update()
            render()
        }
    }

    /**
     * Updates game state.
     */
    private fun update() {
        star.update()
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLUE)
        val paint = Paint()
        star.render(canvas, paint)
        holder.unlockCanvasAndPost(canvas)

    }

    /**
     * Stops the game from processing.
     */
    fun onPause() {
        Log.d(TAG, "onPause")
    }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
        Log.d(TAG, "onResume")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surface created")
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surface changed")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        isRunning = false
        gameThread.join()
    }

}