package com.thomasspringfeldt.spaceshooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory

const val PWRUP_VELOCITY = 8f

/**
 * PowerUp base class.
 * @author Thomas Springfeldt
 */
abstract class PowerUp() : Entity() {

    private var duration = 0
    private var empty = false

    abstract fun update(playerVelocity: Float)

    fun createScaledBitmap(game: Game, resId: Int) : Bitmap {
        val original = BitmapFactory.decodeResource(game.resources, resId)
        val ratio = PLAYER_HEIGHT.toFloat() / original.height
        val newH = (ratio * original.height).toInt()
        val newW = (ratio * original.width).toInt()
        val scaled = Bitmap.createScaledBitmap(original, newW, newH, true)
        return scaled
    }

    fun setDuration(duration: Int) { this.duration = duration}
    fun getDuration() : Int { return duration }

    fun IsEmpty() : Boolean { return empty }
    fun flipEmpty() { empty = !empty }
}