package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Crea el unico HttpClient de la app. La URL base depende de la plataforma
 * (10.0.2.2 en el emulador Android, localhost en el simulador de iOS), por
 * eso llega por parametro desde el modulo de plataforma de Koin.
 */
fun crearHttpClient(engine: HttpClientEngine, baseUrl: String): HttpClient = HttpClient(engine) {
    expectSuccess = true

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        })
    }

    install(Logging) {
        level = LogLevel.HEADERS
        // println sale en Logcat (filtro "System.out") y en la consola de Xcode.
        logger = object : Logger {
            override fun log(message: String) {
                println("Ktor> $message")
            }
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 15000
        connectTimeoutMillis = 10000
    }

    defaultRequest {
        url(baseUrl)
        contentType(ContentType.Application.Json)
    }
}
