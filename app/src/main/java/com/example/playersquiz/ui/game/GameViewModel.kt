package com.example.playersquiz.ui.game

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.playersquiz.remote.models.MyData
import com.example.playersquiz.ui.game.adapters.AdapterTransfer
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.Prova
import com.example.playersquiz.remote.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class GameViewModel : ViewModel(){

    private lateinit var dataSource: List<Prova>
    private var _score = 0
    private lateinit var transfersList: MyData
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
        val id = generateRandomPLayerId()
        Log.d("generateRandomPLayerId()", "number id : $id")
        getPOIList(id)
//        getNextWord()
//        getNextSquad()
//        Log.d("GameFragment", "GameViewModel created! $yearList ,${uriList}")
    }

    private fun generateRandomPLayerId() : Long {
        return (1..200015).random().toLong()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }


    private fun getNextWord() {
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

    fun onLocationListRetrieved(list: List<Response>) {
        Log.d("onLocationListRetrieved 2", list[0].player.name)
        dataSource = list.map {
            Prova(
                player = it.player.name,
                name = it.transfers[0].teams.`in`.name,
                date = it.transfers[0].date,
                logo = it.transfers[0].teams.`in`.logo
            )
        }


    }

    private fun getPOIList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ViewModel Launch", "prima del metadata")
            val metadata = RemoteApi.apiService.getMetadata(id)
            Log.d("ViewModel Launch", "dopo del metadata")
            val result = metadata.enqueue(object : Callback<MyData> {
                override fun onResponse(call: Call<MyData>, response: retrofit2.Response<MyData>) {
                    val responseBody = response.body()!!.response[0]
                    currentWord = responseBody.player.name
                    for (myData in responseBody.transfers){
                        _uriList.add(myData.teams.`in`.logo)
                        _yearList.add(myData.date)
                   }
                    Log.d("onResponse", "currentWord: $currentWord, _uriList: $_uriList, _yearList: $_yearList")

                }

                override fun onFailure(call: Call<MyData>, t: Throwable) {
                    Log.d("MyActivity", "onFailure: "+t.message)
                }
            })

        }

    }
}
