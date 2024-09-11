package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


/**
 * The enemy of the game.
 * @author Thomas Springfeldt
 */
class Enemy(game: Game) : Entity() {

    private val bitmap : Bitmap

    init {
        var id = R.drawable.ship_1
        when(RNG.nextInt(6)) {
            0 -> id = R.drawable.ship_1
            1 -> id = R.drawable.ship_2
            2 -> id = R.drawable.ship_3
            3 -> id = R.drawable.ship_4
            4 -> id = R.drawable.ship_5
            5 -> id = R.drawable.ship_6
        }
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        respawn()
    }

    fun update(playerVelocity: Float) {
        super.update()
        x -= playerVelocity
        if (right < 0) {
            respawn()
        }
    }


    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    private fun respawn() {
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

    fun flip(src: Bitmap, vertically: Boolean) : Bitmap {
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

    fun flipVertically(src: Bitmap) = flip(src, vertically = true)
    fun flipHorizontally(src: Bitmap) = flip(src, vertically = false)
}