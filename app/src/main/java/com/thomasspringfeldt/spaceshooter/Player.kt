package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.math.MathUtils.clamp
import kotlin.math.absoluteValue

const val PLAYER_HEIGHT = 70
const val PLAYER_POSX = 30
const val GRAVITY = 0.25f
const val DRAG = 0.97f
const val ACCELERATION = 0.8f
const val BOOST_FORCE = -0.8f
const val MAX_VELOCITY = 15f
const val VELOCITY_EPSILON = 0.01f
const val PLAYER_DEFAULT_HEALTH = 3
const val INVINCIBILITY_WINDOW = 1500
const val BLINK_LENGTH = 150

/**
 * The protagonist of the game.
 * @author Thomas Springfeldt
 */
class Player(game: Game) : Entity() {

    private val bitmap = createScaledBitmap(game, R.drawable.player)
    private var health = PLAYER_DEFAULT_HEALTH
    private var invincible = false
    private var invincibilityTimer : Long = 0
    private var blink = false
    private var blinkTimer : Long = 0
    private var distanceTraveled = 0f

    init {
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        respawn()
    }

    fun update(isBoosting: Boolean, jukebox: Jukebox) {
        super.update()

        applyDrag()
        applyGravity()
        if (isBoosting) {
            applyBoost()
            jukebox.play(SFX.boost)
        }

        y += velY
        distanceTraveled += velX

        //make sure the player can't leave screen
        if (bottom > STAGE_HEIGHT) {
            bottom = STAGE_HEIGHT.toFloat()
            velY = 0f
        }
        if (top < 0) {
            top = 0f
            velY = 0f
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        if (!invincible) {
            canvas.drawBitmap(bitmap, x, y, paint)
        } else {
            if (blink) else canvas.drawBitmap(bitmap, x, y, paint)
            if (System.currentTimeMillis() - blinkTimer >= 150) {
                flipBlink()
                blinkTimer = System.currentTimeMillis()
            }
        }
    }

    private fun applyBoost() {
        velX += ACCELERATION
        velY += BOOST_FORCE
        velX = clamp(velX, 0f, MAX_VELOCITY)
    }

    private fun applyGravity() {
        velY += GRAVITY
        velY = clamp(velY, -MAX_VELOCITY, MAX_VELOCITY)
    }

    /**
     * Slows down player speed.
     */
    private fun applyDrag() {
        velX *= DRAG
        velY *= DRAG
        if (velX.absoluteValue < VELOCITY_EPSILON) velX = 0f
        if (velY.absoluteValue < VELOCITY_EPSILON) velY = 0f
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        invincible = true
        invincibilityTimer = System.currentTimeMillis()
        blink = true
        blinkTimer = System.currentTimeMillis()
        health--
    }

    fun respawn() {
        x = PLAYER_POSX.toFloat()
        health = PLAYER_DEFAULT_HEALTH
        centerY = STAGE_HEIGHT / 2.0f
        velX = 0f
        velY = 0f
        distanceTraveled = 0f
    }

    fun getHealth() : Int { return health }

    fun isInvincible() : Boolean { return invincible }

    fun flipInvincible() { invincible = !invincible }

    fun getInvincibilityTimer() : Long { return invincibilityTimer }

    fun getBlinkTimer() : Long { return blinkTimer }

    fun flipBlink() { blink = !blink }

    fun getDistanceTraveled() : Float { return distanceTraveled }

    private fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
        val original = BitmapFactory.decodeResource(game.resources, resId)
        val ratio = PLAYER_HEIGHT.toFloat() / original.height
        val newH = (ratio * original.height).toInt()
        val newW = (ratio * original.width).toInt()
        return Bitmap.createScaledBitmap(original, newW, newH, true)
    }
}