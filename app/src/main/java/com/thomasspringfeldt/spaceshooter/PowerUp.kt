package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

const val PWRUP_VELOCITY = 8f

/**
 * PowerUp base class.
 * @author Thomas Springfeldt
 */
abstract class PowerUp : Entity() {

    lateinit var bitmap: Bitmap
    var pwrUpDuration = 0
    var isActive = false
    var isDead = false
    var timer :Long = 0
    lateinit var player : Player

    abstract fun update(playerVelocity: Float)

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
        val original = BitmapFactory.decodeResource(game.resources, resId)
        val ratio = PLAYER_HEIGHT.toFloat() / original.height
        val newH = (ratio * original.height).toInt()
        val newW = (ratio * original.width).toInt()
        val scaled = Bitmap.createScaledBitmap(original, newW, newH, true)
        return scaled
    }
}