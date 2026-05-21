package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val id: String,
    val status: String,
    val result: Double? = null,
    val error: String? = null
)