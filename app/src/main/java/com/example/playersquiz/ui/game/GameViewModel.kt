package com.example.playersquiz.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel
import com.example.playersquiz.ui.game.adapters.AdapterTransfer
import com.example.playersquiz.viewmodels.HomePageViewModel

class GameViewModel : ViewModel() {

    private var _score = 0
    private var transfersList: List<RootMetadataSupportResponseRemoteModel>
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private var _uriList: MutableList<String> = mutableListOf()
    private var _yearList: MutableList<String> = mutableListOf()

    val uriList: MutableList<String>
        get() = _uriList

    val yearList: MutableList<String>
        get() = _yearList


    init {
       val call = HomePageViewModel()
        transfersList = call.getPlayer((generateRandomPLayerId()))
        Log.d("callAPI", "$transfersList")
        getNextWord()
        getNextSquad()
        Log.d("GameFragment", "GameViewModel created! $yearList ,${uriList}")
    }

    private fun generateRandomPLayerId() : Long {
        return (1..200015).random().toLong()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }


    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        var i = 1

        while (i+1 < tempWord.size){

            tempWord[i] = '-'
            i += 1

        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord = String(tempWord)
            ++_currentWordCount
            wordsList.add(currentWord)
        }
      //  _uriList.clear()
      //  _yearList.clear()
       // getNextSquad()
    }

    private fun getNextSquad(){

        val adapterTransfer = AdapterTransfer(transfersList)


        //qui da fare le chiamate per aggionare le variabili _uriList(URL delle squadre) e _yearList(anni di trasferta)
        _uriList = adapterTransfer.getLogo()
        _yearList = adapterTransfer.getDate()
    }


    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
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
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}