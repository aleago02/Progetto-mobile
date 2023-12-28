package com.example.playersquiz.ui.game.adapters


import com.example.playersquiz.remote.models.transfer.Response

class AdapterTransfer(private val response: Response) {

    fun getLogo():MutableList<String> {
        val logoList: MutableList<String> = mutableListOf()
        for (myData in response.transfers){
            logoList.add(myData.teams.`in`.logo)
        }
        return logoList
    }

    fun getDate():MutableList<String> {
        val dateList: MutableList<String> = mutableListOf()
        for (myData in response.transfers){
            dateList.add(myData.date)
        }
        return dateList
    }
}