package com.thomasspringfeldt.spaceshooter

const val INVINC_PWRUP_DURATION = 8000

/**
 * Invincibility power up.
 * @Thomas Springfeldt
 */
class InvincibilityPowerUp(game: Game, player: Player) : PowerUp() {

    init {
        velX = PWRUP_VELOCITY
        pwrUpDuration = INVINC_PWRUP_DURATION
        val id = R.drawable.pwrup_invinc
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
            player.isInvincible = false
            isDead = true
            player.isBlinking = false
        }
        if (isActive && !isDead) { player.handleIFrames(INVINC_PWRUP_DURATION) }
    }

    override fun onCollision(that: Entity) {
        super.onCollision(that)

        left = -width
        timer = System.currentTimeMillis()
        isActive = true
        player.isInvincible = true
        player.iFramesTimer = System.currentTimeMillis()
        player.isBlinking = true
        player.blinkTimer = System.currentTimeMillis()
    }
}