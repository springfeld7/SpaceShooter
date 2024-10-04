package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

/**
 * Base class for enemies.
 * @author Thomas Springfeldt
 */
abstract class Enemy : Entity() {
    abstract fun update(playerVelocity: Float)
    abstract fun move(playerVelocity: Float)

    fun respawn() {
        left = STAGE_WIDTH.toFloat() + RNG.nextInt(STAGE_WIDTH * 2)
        centerY = RNG.nextInt((height / 2).toInt(), STAGE_HEIGHT - (height / 2).toInt()).toFloat()
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        respawn()
    }

    fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
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
    //fun flipHorizontally(src: Bitmap) = flip(src, vertically = false)
}