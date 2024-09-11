package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Star game entity.
 */
class Star : Entity() {

    private val radius = 3f

    init {
        x = RNG.nextInt(STAGE_WIDTH).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT).toFloat()
        velX = -6f
    }

    fun update(playerVelocity: Float) {
        super.update()
        x -= playerVelocity
        if (right < 0) {
            left = STAGE_WIDTH.toFloat()
            centerY = RNG.nextInt(STAGE_HEIGHT).toFloat()
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        paint.color = Color.WHITE
        canvas.drawCircle(x, y, radius, paint)
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
    }
}