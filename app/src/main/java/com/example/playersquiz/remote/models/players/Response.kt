package com.example.playersquiz.remote.models.players

data class Response(
    val player: Player,
    val statistics: List<Statistic>
)