package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
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
    expectSuccess = true

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        })
    }

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
