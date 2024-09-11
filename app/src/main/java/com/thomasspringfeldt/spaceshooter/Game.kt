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

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
const val STAR_COUNT = 50
const val ENEMY_COUNT = 8
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

    private val player = Player(this)
    private val stars = ArrayList<Star>()
    private val enemies = ArrayList<Enemy>()

    init {
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) stars.add(Star())
        for(i in 0 until ENEMY_COUNT) enemies.add(Enemy(this))
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
        for(star in stars) star.update(player.velX)
        for(enemy in enemies) enemy.update(player.velX)
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLUE)
        val paint = Paint()

        for(star in stars) star.render(canvas, paint)
        for(enemy in enemies) enemy.render(canvas, paint)
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
        Log.d(TAG, "KOKKAsurface changed, width: $width, height: $height")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        isRunning = false
        gameThread.join()
    }

}