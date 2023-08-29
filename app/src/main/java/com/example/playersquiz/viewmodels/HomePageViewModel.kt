package com.example.playersquiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playersquiz.remote.RemoteApi
import com.example.playersquiz.remote.models.RootMetadataSupportResponseRemoteModel
import kotlinx.coroutines.launch

interface HomePageViewModelListener {
    fun onTransfersList(list: List<RootMetadataSupportResponseRemoteModel>)
}
class HomePageViewModel: ViewModel() {

    private var listener: HomePageViewModelListener? = null
    fun getPlayer(player: Long) {
        viewModelScope.launch {
            val metadata = RemoteApi.playerRemoteService.getMetadata(player)
            val transfersList = metadata.response ?: emptyList()
            listener?.onTransfersList(transfersList)
        }
    }
}