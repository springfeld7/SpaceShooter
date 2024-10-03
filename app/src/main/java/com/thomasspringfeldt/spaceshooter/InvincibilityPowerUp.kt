package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

const val INVINC_PWRUP_DURATION = 8000

/**
 * Invincibility power up.
 * @Thomas Springfeldt
 */
class InvincibilityPowerUp(game: Game, player: Player) : PowerUp() {

    private val bitmap : Bitmap
    private val player : Player
    var timer :Long = 0

    init {
        velX = PWRUP_VELOCITY
        setDuration(INVINC_PWRUP_DURATION)
        val id = R.drawable.pwrup_invinc
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        this.player = player
        left = STAGE_WIDTH.toFloat() + RNG.nextInt(STAGE_WIDTH)
        centerY = RNG.nextInt((height / 2).toInt(), STAGE_HEIGHT - (height / 2).toInt()).toFloat()
    }

    override fun update(playerVelocity: Float) {
        super.update()
        if (!IsEmpty()) {
            x -= playerVelocity + velX
        } else {

        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        left = -width
        timer = System.currentTimeMillis()
        player.flipInvincible()
    }
}