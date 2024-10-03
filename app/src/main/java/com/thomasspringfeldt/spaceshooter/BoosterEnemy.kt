package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

const val BOOSTER_ENEMY_VELOCITY = 6f
const val BOOSTER_ENEMY_BOOST = 8

/**
 * The booster enemy of the game.
 * @author Thomas Springfeldt
 */
class BoosterEnemy(game: Game) : Enemy() {

    private val bitmap : Bitmap

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

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    override fun move(playerVelocity: Float) {
        if (x > STAGE_WIDTH / 2) {
            x -= playerVelocity + velX * BOOSTER_ENEMY_BOOST
        } else {
            x -= playerVelocity + velX / 2
        }

        if (right < 0) { respawn() }
    }

    override fun respawn() {
        left = STAGE_WIDTH.toFloat() + RNG.nextInt(STAGE_WIDTH)
        centerY = RNG.nextInt((height / 2).toInt(), STAGE_HEIGHT - (height / 2).toInt()).toFloat()
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        respawn()
    }
}
