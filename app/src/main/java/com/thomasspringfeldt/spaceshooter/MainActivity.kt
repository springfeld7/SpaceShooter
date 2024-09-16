package com.thomasspringfeldt.spaceshooter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for the main starting screen.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start_button)?.setOnClickListener {
            Log.d(TAG, "Start button was pressed")
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val longestDistance = prefs.getFloat(LONGEST_DIST, 0.0f)
        val highscore = findViewById<TextView>(R.id.highscore)
        highscore.text = getString(R.string.longest_distance, longestDistance.toInt())
        super.onResume()
    }
}