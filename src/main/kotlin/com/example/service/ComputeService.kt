package com.example.service

import com.example.dto.ComputeRequest
import com.example.dto.ResultDto
import com.example.engine.ComputationEngine
import com.example.storage.ResultStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class ComputeService(
    private val engine: ComputationEngine,
    private val storage: ResultStorage,
    private val scope: CoroutineScope
) {

    fun submitTask(request: ComputeRequest): String {
        val id = UUID.randomUUID().toString()
        storage.save(id, ResultDto(id = id, status = "processing"))

        scope.launch {
            try {
                val result = engine.computeDeterminant(request.matrix)
                storage.update(id, ResultDto(
                    id = id,
                    status = "completed",
                    result = result
                ))
            } catch (e: Exception) {
                storage.update(id, ResultDto(
                    id = id,
                    status = "failed",
                    error = e.message
                ))
            }
        }
        return id
    }

    fun getResult(id: String): ResultDto? = storage.findById(id)
}