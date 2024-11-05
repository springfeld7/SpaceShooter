package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.sin

const val SINE_ENEMY_VELOCITY = 8f

/**
 * Simple enemy moving in a sine wave pattern.
 * @author Thomas Springfeldt
 */
class SineEnemy(game: Game) : Enemy() {

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

    override fun move(playerVelocity: Float) {
        x -= playerVelocity + velX

        y += sin(x /256) * 8
        if (right < 0) {
            respawn()
        }
    }
}
