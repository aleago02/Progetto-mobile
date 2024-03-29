package com.example.playersquiz.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playersquiz.ui.game.adapters.AdapterTransfer
import com.example.playersquiz.remote.models.transfer.Response


class GameViewModel : ViewModel(){

    private lateinit var transfer: AdapterTransfer
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

    private var _uriList: MutableList<String> = mutableListOf()
    private var _yearList: MutableList<String> = mutableListOf()

    val uriList: MutableList<String>
        get() = _uriList

    val yearList: MutableList<String>
        get() = _yearList

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
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
        _uriList.clear()
        _yearList.clear()
        getNextSquad()
    }

    private fun getNextSquad(){
        _uriList = transfer.getLogo()
        _yearList = transfer.getDate()
        Log.d("GameViewModel", "GameViewModel created! $yearList ,${uriList}, $_currentWord")
    }


    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
    }

    fun increaseScore() {
        _score += SCORE_INCREASE
        Log.d("GameViewModel", "Score increased! New score: $_score")
    }

    fun nextWord(): Boolean {
        return _currentWordCount < MAX_NO_OF_WORDS
    }

    fun setting(responseBody: Response, name: String){
        transfer = AdapterTransfer(responseBody)
        _currentWord = name
        getNextWord()
    }
}
