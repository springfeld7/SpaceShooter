package com.thomasspringfeldt.spaceshooter

import android.util.Log

const val BLUE_BULLET_VELOCITY = 10f

/**
 * Simple bullet.
 * @author Thomas Springfeldt
 */
class BlueBullet(game: Game, player: Player) : Projectile(BLUE_BULLET_VELOCITY) {

    init {
        val id = R.drawable.bullet_small_blue
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        left = player.right
        centerY = player.centerY
    }
}