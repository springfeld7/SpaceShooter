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
        x = 600f
        y = 300f
        velX = -6f
    }

    override fun update() {
        super.update()
        x += velX
        if (right < 0) {
            left = 600f
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