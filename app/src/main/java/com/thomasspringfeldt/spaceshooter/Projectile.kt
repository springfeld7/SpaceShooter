package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Base class for projectiles.
 * @author Thomas Springfeldt
 */
abstract class Projectile(velocity: Float) : Entity() {

    lateinit var bitmap: Bitmap
    var isDead = false

    init {
        velX = velocity
    }

    open fun update(playerVelocity: Float) {
        x += velX
        if (left >= STAGE_WIDTH) { isDead = true }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        if (!isDead) { canvas.drawBitmap(bitmap, x, y, paint) }
    }

    fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
        val original = BitmapFactory.decodeResource(game.resources, resId)
        val ratio = PLAYER_HEIGHT.toFloat() / original.height
        val newH = (ratio * original.height).toInt()
        val newW = (ratio * original.width).toInt()
        return Bitmap.createScaledBitmap(original, newW, newH, true)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        left = STAGE_WIDTH.toFloat()
    }
}