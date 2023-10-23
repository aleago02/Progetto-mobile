package com.example.playersquiz.remote.models.transfer

/*Perché sto file è diverso?*/

data class Response(
    val player: Player,
    val transfers: List<Transfer>,
    val update: String
)