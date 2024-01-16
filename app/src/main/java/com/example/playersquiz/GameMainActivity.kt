package com.example.playersquiz


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class GameMainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_main_activity)
        Log.d("GameMainActivity", "GameMainActivity created!")

    }

}


