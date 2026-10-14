package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import kotlin.coroutines.cancellation.CancellationException

class ErrorDeRedException(
    mensaje: String,
    causa: Throwable? = null
) : Exception(mensaje, causa)

fun Throwable.mensajeLegible(): String = when (this) {
    is ErrorDeRedException -> message ?: MENSAJE_GENERICO
    is HttpRequestTimeoutException -> "El servidor tardó demasiado en responder."
    is ClientRequestException -> "El servidor rechazó la solicitud (código ${response.status.value})."
    is ServerResponseException -> "El servidor tuvo un problema (código ${response.status.value}). Inténtalo más tarde."
    is SerializationException -> "La respuesta del servidor no tiene el formato esperado."
    else -> MENSAJE_GENERICO
}

private const val MENSAJE_GENERICO =
    "No se pudo conectar con el servidor. Revisa tu conexión e inténtalo de nuevo."

fun mapearErrorSesion(estado: HttpStatusCode, mensaje: String?): ErrorSesion =
    when (estado) {
        HttpStatusCode.Unauthorized ->
            if (mensaje?.contains("expirado") == true)
                ErrorSesion.SesionExpirada
            else ErrorSesion.CredencialesInvalidas
        HttpStatusCode.Forbidden -> ErrorSesion.SinPermiso
        else -> ErrorSesion.SinConexion
    }

internal suspend fun ResponseException.mensajeDelServidor(): String? = try {
    Json.parseToJsonElement(response.bodyAsText())
        .jsonObject["message"]
        ?.jsonPrimitive
        ?.contentOrNull
} catch (fallo: CancellationException) {
    throw fallo
} catch (fallo: Throwable) {
    null
}

internal suspend fun <T> llamadaRemota(bloque: suspend () -> T): T {
    try {
        return bloque()
    } catch (cancelacion: CancellationException) {
        throw cancelacion
    } catch (sesion: ErrorSesionException) {
        throw sesion
    } catch (rechazo: ClientRequestException) {
        throw when (rechazo.response.status) {
            HttpStatusCode.Unauthorized ->
                ErrorSesionException(ErrorSesion.SesionExpirada, rechazo)
            HttpStatusCode.Forbidden ->
                ErrorSesionException(ErrorSesion.SinPermiso, rechazo)
            else -> ErrorDeRedException(rechazo.mensajeLegible(), rechazo)
        }
    } catch (fallo: Throwable) {
        throw ErrorDeRedException(fallo.mensajeLegible(), fallo)
    }
}

internal suspend fun <T> llamadaDeLogin(bloque: suspend () -> T): T {
    try {
        return bloque()
    } catch (cancelacion: CancellationException) {
        throw cancelacion
    } catch (sesion: ErrorSesionException) {
        throw sesion
    } catch (rechazo: ClientRequestException) {
        val estado = rechazo.response.status
        if (estado == HttpStatusCode.Unauthorized || estado == HttpStatusCode.Forbidden) {
            throw ErrorSesionException(
                mapearErrorSesion(estado, rechazo.mensajeDelServidor()),
                rechazo
            )
        }
        throw ErrorDeRedException(rechazo.mensajeLegible(), rechazo)
    } catch (servidor: ServerResponseException) {
        throw ErrorDeRedException(servidor.mensajeLegible(), servidor)
    } catch (fallo: SerializationException) {
        throw ErrorDeRedException(fallo.mensajeLegible(), fallo)
    } catch (fallo: Throwable) {
        throw ErrorSesionException(ErrorSesion.SinConexion, fallo)
    }
}
