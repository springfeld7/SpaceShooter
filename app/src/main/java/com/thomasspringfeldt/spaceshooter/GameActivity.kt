package com.thomasspringfeldt.spaceshooter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for the game screen.
 */
class GameActivity : AppCompatActivity() {
    private lateinit var game : Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = Game(this)
        setContentView(game)

    }

    override fun onPause() {
        game.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        game.onResume()
    }

    override fun onDestroy() {
        game.onDestroy()
        super.onDestroy()
    }
}