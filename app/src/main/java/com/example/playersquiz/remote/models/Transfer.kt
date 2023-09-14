package com.example.playersquiz.remote.models

data class Transfer(
    val date: String,
    val teams: Teams,
    val type: String?
)