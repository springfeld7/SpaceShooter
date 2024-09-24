package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
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
        val id = R.drawable.ship_2
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

    private fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
        val original = BitmapFactory.decodeResource(game.resources, resId)
        val ratio = PLAYER_HEIGHT.toFloat() / original.height
        val newH = (ratio * original.height).toInt()
        val newW = (ratio * original.width).toInt()
        val scaled = Bitmap.createScaledBitmap(original, newW, newH, true)
        return flipVertically(scaled)
    }

    private fun flip(src: Bitmap, vertically: Boolean) : Bitmap {
        val matrix = Matrix()
        val cx = src.width / 2f
        val cy = src.height / 2f
        if (vertically) {
            matrix.postScale(-1f, 1f, cx, cy)
        } else {
            matrix.postScale(1f, -1f, cx, cy)
        }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun flipVertically(src: Bitmap) = flip(src, vertically = true)
    private fun flipHorizontally(src: Bitmap) = flip(src, vertically = false)
}