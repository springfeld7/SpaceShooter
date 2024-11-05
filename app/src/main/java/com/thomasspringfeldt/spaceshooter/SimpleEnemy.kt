package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Paint

const val SIMPLE_ENEMY_VELOCITY = 5f

/**
 * The simplest enemy of the game.
 * @author Thomas Springfeldt
 */
class SimpleEnemy(game: Game) : Enemy() {

    init {
        velX = SIMPLE_ENEMY_VELOCITY
        val id = R.drawable.simple_enemy
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
        if (right < 0) {
            respawn()
        }
    }
}
