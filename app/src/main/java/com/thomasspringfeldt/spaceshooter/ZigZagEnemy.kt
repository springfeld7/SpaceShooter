package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

const val ZIGZAG_ENEMY_VELOCITY = 4f


/**
 * The zigzagging enemy of the game.
 * @author Thomas Springfeldt
 */
class ZigZagEnemy(game: Game) : Enemy() {

    private val bitmap : Bitmap
    private var zig : Float
    private var zigCounter = 0f
    private var moveUp = true

    init {
        velX = ZIGZAG_ENEMY_VELOCITY
        velY = ZIGZAG_ENEMY_VELOCITY
        zig = RNG.nextInt(STAGE_WIDTH / 8, STAGE_WIDTH / 3).toFloat()
        val id = R.drawable.zigzag_enemy
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
        if (moveUp ) {
            x -= playerVelocity + velX
            y -= velY
        } else {
            x -= playerVelocity + velX
            y -= -velY
        }
        zigCounter += velX
        if (zigCounter >= zig) {
            moveUp = !moveUp
            zigCounter = 0f
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
