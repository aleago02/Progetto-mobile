package com.example.playersquiz.remote.old

import com.example.playersquiz.remote.old.RootResponsePlayerRemoteModel
import com.example.playersquiz.remote.old.RootResponseTransfersRemoteModel


data class RootMetadataSupportResponseRemoteModel (
    val player: RootResponsePlayerRemoteModel? = null,
    val transfers: List<RootResponseTransfersRemoteModel>? = null,
)