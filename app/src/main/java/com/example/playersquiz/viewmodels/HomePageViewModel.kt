package com.example.playersquiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel
import kotlinx.coroutines.launch

class HomePageViewModel {
    fun getPlayer(player: Long): List<RootMetadataSupportResponseRemoteModel> {
        val metadata = RemoteApi.playerRemoteService.getMetadata(player)
        val transfers = metadata.response ?: emptyList()
        return transfers
    }
}