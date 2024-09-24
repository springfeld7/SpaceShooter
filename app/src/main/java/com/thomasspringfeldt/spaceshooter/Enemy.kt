package com.thomasspringfeldt.spaceshooter

/**
 * Base class for enemies.
 * @author Thomas Springfeldt
 */
abstract class Enemy : Entity() {
    abstract fun update(velX: Float)
    abstract fun respawn()
    abstract fun move(playerVelocity: Float)
}