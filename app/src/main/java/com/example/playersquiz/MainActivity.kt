package com.example.playersquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playersquiz.databinding.MainActivityBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = MainActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Log.d("MainActivity", "MainActivity created!")

        val buttonFootball = findViewById<Button>(R.id.buttonFootball)
        buttonFootball.setOnClickListener {
            val intent = Intent(this, FootballMainActivity::class.java)
            startActivity(intent)
        }

        val buttunNba = findViewById<Button>(R.id.buttonNba)
        buttunNba.setOnClickListener{
            val intent = Intent(this, NbaMainActivity::class.java)
            startActivity(intent)
        }
    }
}