package com.example

import com.example.dto.ComputeRequest
import com.example.dto.ResultDto
import com.example.engine.ComputationEngine
import com.example.service.ComputeService
import com.example.storage.ResultStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }

        val engine = ComputationEngine()
        val storage = ResultStorage()
        val service = ComputeService(engine, storage, CoroutineScope(Dispatchers.IO))

        routing {
            post("/compute") {
                val request = call.receive<ComputeRequest>()
                val taskId = service.submitTask(request)
                call.respond(
                    HttpStatusCode.Accepted,
                    ResultDto(id = taskId, status = "processing")
                )
            }

            get("/result/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing id")
                    return@get
                }
                val result = service.getResult(id)
                if (result != null) {
                    call.respond(result)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            }
        }
    }.start(wait = true)
}