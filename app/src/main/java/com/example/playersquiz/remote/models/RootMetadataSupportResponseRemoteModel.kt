package com.example.playersquiz.remote.models

data class RootMetadataSupportResponseRemoteModel (
    val player: RootResponsePlayerRemoteModel? = null,
    val transfers: List<RootResponseTransfersRemoteModel>? = null,
)