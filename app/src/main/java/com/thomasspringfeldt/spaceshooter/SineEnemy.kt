package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.sin

const val SINE_ENEMY_VELOCITY = 8f

/**
 * Simple enemy moving in a sine wave pattern.
 * @author Thomas Springfeldt
 */
class SineEnemy(game: Game) : Enemy() {

    private val bitmap : Bitmap

    init {
        velX = SINE_ENEMY_VELOCITY
        val id = R.drawable.sine_enemy
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        respawn()
    }

    override fun update(playerVelocity: Float) {
        super.update()
        move(playerVelocity)
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    override fun move(playerVelocity: Float) {
        x -= playerVelocity + velX

        y += sin(x /256) * 8
        if (right < 0) {
            respawn()
        }
    }

    override fun respawn() {
        left = STAGE_WIDTH.toFloat() + RNG.nextInt(STAGE_WIDTH)
        centerY = RNG.nextInt((height / 2).toInt(), STAGE_HEIGHT - (height / 2).toInt()).toFloat()
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        respawn()
    }
}
