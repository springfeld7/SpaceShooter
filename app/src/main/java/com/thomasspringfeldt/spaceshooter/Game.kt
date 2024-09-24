package com.thomasspringfeldt.spaceshooter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.util.Log
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.round
import kotlin.random.Random

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
const val STAR_COUNT = 50
const val ENEMY_COUNT = 8
const val PREFS = "com.thomasspringfeldt.spaceshooter"
const val LONGEST_DIST = "longest_distance"
const val TARGET_FPS = 60f
var RNG = Random(uptimeMillis())

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

    private val tag = "Game"
    private lateinit var gameThread : Thread
    @Volatile private var isRunning : Boolean = false
    @Volatile private var fingerDown = false
    private var isBoosting = false
    private var isGameOver = false

    private var maxDistancedTraveled = 0.0f
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private val player = Player(this)
    private val stars = ArrayList<Star>()
    private val enemies = ArrayList<Enemy>()

    private var jukebox = Jukebox(context.assets)


    init {
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) stars.add(Star())
        for(i in 0 until ENEMY_COUNT) enemies.add(Enemy(this))
        maxDistancedTraveled = prefs.getFloat(LONGEST_DIST, 0.0f)
        jukebox.play(SFX.start_game)
    }

    /**
     * Game loop.
     */
    override fun run() {
        Log.d(tag, "run()")
        while(isRunning) {
            update()
            render()
        }
    }

    /**
     * Updates game state.
     */
    private fun update() {
        if (isGameOver) {
            return
        }
        isBoosting = fingerDown
        player.update(isBoosting)
        for(star in stars) star.update(player.velX)
        for(enemy in enemies) enemy.update(player.velX)
        checkCollisions()
        checkGameOver()
    }

    private fun checkCollisions() {
        for (enemy in enemies) {
            if (isColliding(enemy, player)) {
                enemy.onCollision(player)
                player.onCollision(enemy)
                jukebox.play(SFX.crash)
            }
        }
    }

    private fun checkGameOver() {
       if (player.getHealth() <= 0) {
           jukebox.play(SFX.game_over)
           if (player.getDistanceTraveled() > maxDistancedTraveled) {
               editor.putFloat(LONGEST_DIST, player.getDistanceTraveled())
               editor.apply()
           }
           isGameOver = true
       }
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
        renderHud(canvas, paint)

        holder.unlockCanvasAndPost(canvas)

    }

    private fun renderHud(canvas: Canvas, paint: Paint) {
        val textSize = 48f
        val textPosition = 10f
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.LEFT
        if (!isGameOver) {
            canvas.drawText("Health: ${player.getHealth()}", textPosition, textSize, paint)
            canvas.drawText("Distance: ${round(player.getDistanceTraveled())}", textPosition, textSize * 2, paint)
        } else {
            val centerX = STAGE_WIDTH / 2.0f
            val centerY = STAGE_HEIGHT / 2.0f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("GAME OVER", centerX, centerY, paint)
            canvas.drawText("Press to restart", centerX, centerY + textSize, paint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> fingerDown = true
            MotionEvent.ACTION_UP -> {
                fingerDown = false
                if (isGameOver) {
                    restart()
                }
            }
        }
        return true
    }

    private fun restart() {
        for(enemy in enemies) enemy.respawn()
        player.respawn()
        maxDistancedTraveled = prefs.getFloat(LONGEST_DIST, 0.0f)
        isGameOver = false
    }

    /**
     * Stops the game from processing.
     */
    fun onPause() {
        Log.d(tag, "onPause")
    }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
        Log.d(tag, "onResume")
    }


    fun onDestroy() {
        jukebox.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(tag, "surface created")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            holder.surface.setFrameRate(TARGET_FPS, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT)
        }
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(tag, "KOKKAsurface changed, width: $width, height: $height")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(tag, "surface destroyed")
        isRunning = false
        gameThread.join()
    }
}