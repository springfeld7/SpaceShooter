package com.thomasspringfeldt.spaceshooter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

const val SCORE_MULTIPLIER_PWRUP_DURATION = 10000

/**
 * Score multiplier power up.
 * @author Thomas Springfeldt
 */
class ScoreMultiplierPowerUp(game: Game, player: Player) : PowerUp() {

    init {
        velX = PWRUP_VELOCITY
        pwrUpDuration = SCORE_MULTIPLIER_PWRUP_DURATION
        val id = R.drawable.pwrup_scoremultiplier
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
        super.game = game
        super.player = player
        left = STAGE_WIDTH.toFloat() + RNG.nextInt(STAGE_WIDTH * 2)
        centerY = RNG.nextInt((height / 2).toInt(), STAGE_HEIGHT - (height / 2).toInt()).toFloat()
    }

    override fun update(playerVelocity: Float) {
        super.update()

        if (!isActive) {
            x -= playerVelocity + velX
        }
        if (isActive && System.currentTimeMillis() - timer >= pwrUpDuration) {
            isDead = true
        }
        if (isActive && !isDead) {
            player.distanceTraveled += player.velX
        }
    }

    override fun render(canvas: Canvas, paint: Paint) {
        super.render(canvas, paint)

        if (isActive && !isDead) {
            val textSize = 48f
            val textPosition = 10f
            paint.color = Color.YELLOW
            paint.textSize = textSize
            paint.textAlign = Paint.Align.LEFT
            val multiply = game.resources.getString(R.string.multiply)
            canvas.drawText(multiply, textPosition, textSize * 3, paint)
        }
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)

        left = -width
        timer = System.currentTimeMillis()
        isActive = true
    }
}