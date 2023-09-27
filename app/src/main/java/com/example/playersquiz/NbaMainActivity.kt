package com.example.playersquiz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class NbaMainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nbamain_activity)
        Log.d("NbaMainActivity", "NbaMainActivity created!")

    }

}