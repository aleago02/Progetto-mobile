package com.example.playersquiz.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playersquiz.ui.game.adapters.AdapterTransfer
import com.example.playersquiz.remote.models.Response


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

    private lateinit var currentWord: String

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
        currentWord = transfer.getPlayerName()
        val tempWord = currentWord.toCharArray()
        var i = 1

        while (i+1 < tempWord.size){

            tempWord[i] = '-'
            i += 1

        }
        _currentScrambledWord = String(tempWord)
        ++_currentWordCount
        _uriList.clear()
        _yearList.clear()
        getNextSquad()
    }

    private fun getNextSquad(){
        //qui da fare le chiamate per aggionare le variabili _uriList(URL delle squadre) e _yearList(anni di trasferta)
        _uriList = transfer.getLogo()
        _yearList = transfer.getDate()
        Log.d("GameViewModel", "GameViewModel created! $yearList ,${uriList}, $currentWord")
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

    fun setting(responseBody: Response){
        transfer = AdapterTransfer(responseBody)
        getNextWord()
    }
}
