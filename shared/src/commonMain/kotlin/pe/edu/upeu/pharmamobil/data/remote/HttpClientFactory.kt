package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
<<<<<<< HEAD
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
<<<<<<< HEAD
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguro

fun crearHttpClient(
    engine: HttpClientEngine,
    baseUrl: String,
    almacen: AlmacenSeguro,
    esDepuracion: Boolean
): HttpClient = HttpClient(engine) {
=======
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Crea el unico HttpClient de la app. La URL base depende de la plataforma
 * (10.0.2.2 en el emulador Android, localhost en el simulador de iOS), por
 * eso llega por parametro desde el modulo de plataforma de Koin.
 */
fun crearHttpClient(engine: HttpClientEngine, baseUrl: String): HttpClient = HttpClient(engine) {
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    expectSuccess = true

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        })
    }

<<<<<<< HEAD
    install(Auth) {
        bearer {
            loadTokens {
                almacen.leerToken()?.let { BearerTokens(it, "") }
            }
            // El token viaja ya en la primera peticion (evita enviar sin
            // token, recibir 401 y reintentar). Solo el login va sin el.
            sendWithoutRequest { peticion ->
                !peticion.url.encodedPath.endsWith("/auth/login")
            }
        }
    }

    install(Logging) {
        level = if (esDepuracion) LogLevel.HEADERS else LogLevel.NONE
        sanitizeHeader { nombre -> nombre == HttpHeaders.Authorization }
=======
    install(Logging) {
        level = LogLevel.HEADERS
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
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
