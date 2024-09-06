package com.thomasspringfeldt.spaceshooter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private final val TAG = "MainActivity"
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
}