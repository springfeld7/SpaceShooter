package com.thomasspringfeldt.spaceshooter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.SystemClock.uptimeMillis
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

const val STAGE_WIDTH = 1280
const val STAGE_HEIGHT = 672
const val STAR_COUNT = 50
const val ENEMY_COUNT = 8
const val PREFS = "com.thomasspringfeldt.spaceshooter"
const val LONGEST_DIST = "longest_distance"
const val TARGET_FPS = 60f
const val PWRUP_TRIGGER_VALUE = 1500f
var RNG = Random(uptimeMillis())

/**
 * Game engine for the Space Shooter.
 * @author Thomas Springfeldt
 */
class Game(context: Context) : SurfaceView(context), Runnable, SurfaceHolder.Callback {

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
    private val projectiles = ArrayList<Projectile>()
    private val powerups = ArrayList<PowerUp>()
    private val entitiesToRemove = ArrayList<Entity>()

    private var pwrUpTrigger = 0f

    private var jukebox = Jukebox(context.assets)

    init {
        holder?.addCallback(this)
        holder?.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT)
        for(i in 0 until STAR_COUNT) stars.add(Star())
        for(i in 0 until ENEMY_COUNT-6) enemies.add(SimpleEnemy(this))
        for(i in 2 until ENEMY_COUNT-4) enemies.add(ZigZagEnemy(this))
        for(i in 4 until ENEMY_COUNT-2) enemies.add(BoosterEnemy(this))
        for(i in 6 until ENEMY_COUNT) enemies.add(SineEnemy(this))
        powerups.add(InvincibilityPowerUp(this, player))
        powerups.add(ScoreMultiplierPowerUp(this, player))
        powerups.add(AttackSpeedPowerUp(this, player))

        maxDistancedTraveled = prefs.getFloat(LONGEST_DIST, 0.0f)
        jukebox.play(SFX.start_game)
    }

    fun getProjectilesSize() : Int { return projectiles.size }
    fun addProjectile(projectile: Projectile) { projectiles.add(projectile) }
    fun addPowerUp(powerUp: PowerUp) { powerups.add(powerUp) }

    private fun removeEntity(entity: Entity) {
        entitiesToRemove.add(entity)
    }

    private fun addAndRemoveEntities() {
        projectiles.removeAll(entitiesToRemove.toSet())
        powerups.removeAll(entitiesToRemove.toSet())
        entitiesToRemove.clear()

        //entities.addAll(entitiesToAdd)
        //entitiesToAdd.clear()
        if (pwrUpTrigger > PWRUP_TRIGGER_VALUE) {
            spawnRandomPwrUp()
            pwrUpTrigger = 0f
        }
    }

    private fun spawnRandomPwrUp() {
        when (RNG.nextInt(3)) {
            0 -> { addPowerUp(InvincibilityPowerUp(this, player)) }
            1 -> { addPowerUp(ScoreMultiplierPowerUp(this, player)) }
            2 -> { addPowerUp(AttackSpeedPowerUp(this, player)) }
            else -> { }
        }
    }

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
        if (isGameOver) {
            return
        }
        isBoosting = fingerDown
        player.update(isBoosting, jukebox)
        pwrUpTrigger += player.velX

        for (star in stars) star.update(player.velX)
        for (enemy in enemies) enemy.update(player.velX)
        for (projectile in projectiles) {
            projectile.update(player.velX)
            if (projectile.isDead) {
                removeEntity(projectile)
            }
        }
        for (powerup in powerups) {
            powerup.update(player.velX)
            if (powerup.isDead) {
                removeEntity(powerup)
            }
        }
        checkCollisions()
        checkPwrUpCollisions()
        checkGameOver()
        addAndRemoveEntities()
    }

    /**
     * Renders game state.
     */
    private fun render() {
        val canvas = holder?.lockCanvas() ?: return
        canvas.drawColor(Color.BLUE)
        val paint = Paint()

        for (star in stars) star.render(canvas, paint)
        for (enemy in enemies) enemy.render(canvas, paint)
        for (projectile in projectiles) projectile.render(canvas, paint)
        for (powerup in powerups) powerup.render(canvas, paint)
        player.render(canvas, paint)
        renderHud(canvas, paint)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun checkCollisions() {
        checkEnemyCollisions()
        checkProjectileCollisions()
        checkPwrUpCollisions()
    }

    private fun checkEnemyCollisions() {
        for (enemy in enemies) {
            if (isColliding(enemy, player)) {
                jukebox.play(SFX.crash)
                enemy.onCollision(player)
                player.onCollision(enemy)
            }
        }
    }

    private fun checkProjectileCollisions() {
        for (projectile in projectiles) {
            for (enemy in enemies) {
                if (isColliding(projectile, enemy)) {
                    jukebox.play(SFX.crash)
                    projectile.onCollision(enemy)
                    enemy.onCollision(projectile)
                }
            }
        }
    }

    private fun checkPwrUpCollisions() {
        for (powerup in powerups) {
            if (isColliding(powerup, player)) {
                jukebox.play(SFX.powerup)
                powerup.onCollision(player)
            }
        }
    }

    private fun checkGameOver() {
       if (player.health <= 0) {
           jukebox.play(SFX.game_over)
           if (player.distanceTraveled > maxDistancedTraveled) {
               editor.putFloat(LONGEST_DIST, player.distanceTraveled)
               editor.apply()
           }
           isGameOver = true
       }
    }

    private fun renderHud(canvas: Canvas, paint: Paint) {
        val textSize = 48f
        val textPosition = 10f
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.LEFT

        if (!isGameOver) {
            val health = resources.getString(R.string.player_health, player.health)
            val distance = resources.getString(R.string.distance, player.distanceTraveled.toInt())
            canvas.drawText(health, textPosition, textSize, paint)
            canvas.drawText(distance, textPosition, textSize * 2, paint)
        } else {
            val centerX = STAGE_WIDTH / 2.0f
            val centerY = STAGE_HEIGHT / 2.0f
            paint.textAlign = Paint.Align.CENTER

            canvas.drawText(resources.getString(R.string.game_over), centerX, centerY, paint)
            canvas.drawText(resources.getString(R.string.restart), centerX, centerY + textSize, paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        powerups.clear()
        projectiles.clear()
        player.respawn()
        maxDistancedTraveled = prefs.getFloat(LONGEST_DIST, 0.0f)
        isGameOver = false
    }

    /**
     * Stops the game from processing.
     */
    fun onPause() { }

    /**
     * Resumes the processing of the game.
     */
    fun onResume() {
    }

    fun onDestroy() {
        jukebox.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            holder.surface.setFrameRate(TARGET_FPS, Surface.FRAME_RATE_COMPATIBILITY_DEFAULT)
        }
        isRunning = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) { }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
        gameThread.join()
    }
}
