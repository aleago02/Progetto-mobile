package com.example.playersquiz.remote.models

data class MyData(
    val `get`: String?,
    val parameters: Parameters?,
    val errors: List<Any>?,
    val results: Int?,
    val paging: Paging?,
    val response: List<Response>?
)