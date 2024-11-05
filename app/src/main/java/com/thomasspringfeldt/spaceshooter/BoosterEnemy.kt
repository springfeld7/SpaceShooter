package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Paint

const val BOOSTER_ENEMY_VELOCITY = 6f
const val BOOSTER_ENEMY_BOOST = 8

/**
 * The booster enemy of the game.
 * @author Thomas Springfeldt
 */
class BoosterEnemy(game: Game) : Enemy() {

    init {
        velX = BOOSTER_ENEMY_VELOCITY
        val id = R.drawable.booster_enemy
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
        if (x > STAGE_WIDTH / 2) {
            x -= playerVelocity + velX * BOOSTER_ENEMY_BOOST
        } else {
            x -= playerVelocity + velX / 2
        }

        if (right < 0) { respawn() }
    }
}
