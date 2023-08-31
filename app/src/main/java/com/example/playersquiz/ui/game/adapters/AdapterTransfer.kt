package com.example.playersquiz.ui.game.adapters

import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel

class AdapterTransfer(private val transfers: List<RootMetadataSupportResponseRemoteModel>) {

    fun getPlayerName(): String? {
        return transfers[0].player?.name
    }

    fun getLogo():MutableList<String> {
        val logoList: MutableList<String> = mutableListOf()
        var i=0
        while (i< transfers[0].transfers?.size!!)
        {
            if (transfers[0].transfers?.get(i)?.teams?.int?.logo != null) {
                logoList.add(transfers[0].transfers?.get(i)?.teams?.int?.logo!!)
            }
            i++
        }
        return logoList
    }

    fun getDate():MutableList<String> {
        val dateList: MutableList<String> = mutableListOf()
        var i=0
        while (i< transfers[0].transfers?.size!!)
        {
            if (transfers[0].transfers?.get(i)?.date != null) {
                dateList.add(transfers[0].transfers?.get(i)?.date!!.toString())
            }
            i++
        }
        return dateList
    }
}