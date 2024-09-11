package com.thomasspringfeldt.spaceshooter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val GAME_WIDTH = 1280
const val STAGE_HEIGHT = 672
const val STAR_COUNT = 50
var RNG = Random(uptimeMillis())

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context?) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private val TAG = "Game"
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false
    @Volatile private var fingerDown = false
    var isBoosting = false

    val player = Player(this)
    private val stars = ArrayList<Star>()

    init {
        holder?.addCallback(this)
        holder?.setFixedSize(GAME_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) {
            stars.add(Star())
        }
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
        isBoosting = fingerDown
        player.update(isBoosting)
        for(star in stars) { star.update(player.velX) }
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLUE)
        val paint = Paint()

        for(star in stars) { star.render(canvas, paint) }
        player.render(canvas, paint)

        holder.unlockCanvasAndPost(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> fingerDown = true
                MotionEvent.ACTION_UP -> fingerDown = false
        }
        return true
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
        Log.d(TAG, "surface changed, width: $width, height: $height")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        isRunning = false
        gameThread.join()
    }

}