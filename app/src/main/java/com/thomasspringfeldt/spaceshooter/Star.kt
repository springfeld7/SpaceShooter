package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

const val SMALL_STAR_RADIUS = 1
const val MEDIUM_STAR_RADIUS = 3
const val LARGE_STAR_RADIUS = 5
const val STAR_VELOCITY = 6f

/**
 * Star for the game screen background.
 */
class Star : Entity() {

    private var radius: Float
    private val starSizes: IntArray =
        intArrayOf(SMALL_STAR_RADIUS, MEDIUM_STAR_RADIUS, LARGE_STAR_RADIUS)

    init {
        x = RNG.nextInt(STAGE_WIDTH).toFloat()
        y = RNG.nextInt(STAGE_HEIGHT).toFloat()
        radius = getRandomStarSize()
        velX = STAR_VELOCITY
    }

    fun update(playerVelocity: Float) {
        super.update()
        x -= playerVelocity + radius
        if (right < 0) {
            left = STAGE_WIDTH.toFloat()
            centerY = RNG.nextInt(STAGE_HEIGHT).toFloat()
            radius = getRandomStarSize()
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        paint.color = Color.WHITE
        canvas.drawCircle(x, y, radius, paint)
    }

    private fun getRandomStarSize() : Float {

        return starSizes[RNG.nextInt(starSizes.size)].toFloat()
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
    }
}