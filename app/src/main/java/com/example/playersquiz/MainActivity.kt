package com.example.playersquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Log.d("MainActivity", "MainActivity created!")

        val buttonFootball = findViewById<Button>(R.id.buttonFootball)
        buttonFootball.setOnClickListener{
            val intent = Intent(this, FootballMainActivity::class.java)
            startActivity(intent)
        }

        val buttonNba = findViewById<Button>(R.id.buttonNba)
        buttonNba.setOnClickListener{
            val intent = Intent(this, NbaMainActivity::class.java)
            startActivity(intent)
        }

    }
}