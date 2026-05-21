package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ComputeRequest(
    val matrix: List<List<Double>>
)