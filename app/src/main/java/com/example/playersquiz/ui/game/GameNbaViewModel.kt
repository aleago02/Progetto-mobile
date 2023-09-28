package com.example.playersquiz.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playersquiz.remote.models.playernba.Data

class GameNbaViewModel : ViewModel(){

    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private lateinit var _currentWord: String
    val currentWord: String
        get() = _currentWord

    private lateinit var _squad: String
    private lateinit var _position: String
    private lateinit var _altezza: String

    val squad: String
        get() = _squad

    val position: String
        get() = _position

    val altezza: String
        get() = _altezza

    override fun onCleared() {
        super.onCleared()
        Log.d("GameNbaViewModel", "GameViewModel destroyed!")
    }


    private fun getNextWord() {
        val tempWord = _currentWord.toCharArray()
        var i = 1

        while (i+1 < tempWord.size){
            if(tempWord[i] == ' '){

            }else{
                tempWord[i] = '-'
            }
            i += 1
        }
        _currentScrambledWord = String(tempWord)
        ++_currentWordCount
        Log.d("GameViewModel", "GameViewModel created! $_altezza ,$_squad, $_position, $_currentWord")
    }

    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun nextWord(): Boolean {
        return _currentWordCount < MAX_NO_OF_WORDS
    }

    fun setting(responseBody: Data, name: String){
        _altezza = responseBody.height_feet.toString() + " ft " + responseBody.height_inches.toString() + " in"
        _squad = responseBody.team.name
        _position = responseBody.position.toString()
        _currentWord = name
        getNextWord()
    }
}
