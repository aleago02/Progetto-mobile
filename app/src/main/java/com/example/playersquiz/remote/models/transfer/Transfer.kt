package com.example.playersquiz.remote.models.transfer

data class Transfer(
    val date: String,
    val teams: Teams,
    val type: String?
)