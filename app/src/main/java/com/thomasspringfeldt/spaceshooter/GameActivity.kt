package com.thomasspringfeldt.spaceshooter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for the game screen.
 */
class GameActivity : AppCompatActivity() {
    private val TAG = "GameActivity"
    lateinit var game : Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Game activity was launched")
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


}