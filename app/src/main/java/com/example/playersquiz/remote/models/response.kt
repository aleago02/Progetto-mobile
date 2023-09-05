package com.example.playersquiz.remote.models

data class Response(
    val player: Player,
    val transfers: List<Transfer>,
    val update: String
)