package com.example.playersquiz.ui.game.adapters

import com.example.playersquiz.remote.models.MyData
import com.example.playersquiz.remote.old.RootMetadataSupportResponseRemoteModel

class AdapterTransfer(private val transfers: MyData?) {

//   fun getPlayerName(): String? {
//        return transfers[0].player?.name
//    }

    fun getLogo():MutableList<String> {
        val logoList: MutableList<String> = mutableListOf()
        var i=0
        while (i< transfers?.response?.get(0)?.transfers?.size !!)
        {
            logoList.add(transfers.response[0].transfers[i].teams.`in`.logo)
            i++
        }
        return logoList
    }

    fun getDate():MutableList<String> {
        val dateList: MutableList<String> = mutableListOf()
        var i=0
        while (i< transfers?.response?.get(0)?.transfers?.size !!)
        {
            dateList.add(transfers.response[0].transfers[i].date)
            i++
        }
        return dateList
    }
}