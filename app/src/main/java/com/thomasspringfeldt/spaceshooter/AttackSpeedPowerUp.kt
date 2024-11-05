package com.thomasspringfeldt.spaceshooter

const val ATKSPD_PWRUP_DURATION = 7000
const val ATKSPD_PWRUP_FIRE_RATE = BULLET_FIRE_RATE / 2

/**
 * Attack speed power up.
 * @author Thomas Springfeldt
 */
class AttackSpeedPowerUp(game: Game, player: Player) : PowerUp() {

    init {
        velX = PWRUP_VELOCITY
        pwrUpDuration = ATKSPD_PWRUP_DURATION
        val id = R.drawable.pwrup_atkrate
        bitmap = createScaledBitmap(game, id)
        width = bitmap.width.toFloat()
        height = bitmap.height.toFloat()
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
            player.bulletFireRate = BULLET_FIRE_RATE
        }
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)
        player.bulletFireRate = ATKSPD_PWRUP_FIRE_RATE
        left = -width
        timer = System.currentTimeMillis()
        isActive = true
    }
}