package com.example.playersquiz.remote.models

import java.util.Date
data class RootResponseTransfersRemoteModel (
    val date: Date? = null,
    val type: String? = null,
    val teams: RootMetaDataSupportTransfersRemoteModel? = null,
)