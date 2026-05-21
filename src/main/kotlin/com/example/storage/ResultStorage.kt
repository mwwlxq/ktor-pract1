package com.example.storage

import com.example.dto.ResultDto
import java.util.concurrent.ConcurrentHashMap

class ResultStorage {
    private val store = ConcurrentHashMap<String, ResultDto>()

    fun save(id: String, result: ResultDto) {
        store[id] = result
    }

    fun findById(id: String): ResultDto? = store[id]

    fun update(id: String, result: ResultDto) {
        store[id] = result
    }
}