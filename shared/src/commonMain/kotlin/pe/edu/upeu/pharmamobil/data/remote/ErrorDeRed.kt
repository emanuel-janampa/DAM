package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Error de la capa de datos con un mensaje ya apto para el usuario. Las capas
 * superiores solo ven este tipo: no necesitan importar Ktor.
 */
class ErrorDeRedException(
    mensaje: String,
    causa: Throwable? = null
) : Exception(mensaje, causa)

/** Traduce cualquier fallo de red o de deserializacion a un texto legible. */
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

/**
 * Ejecuta una llamada remota y convierte cualquier fallo en ErrorDeRedException.
 * La cancelacion de corrutinas se deja pasar para no romper la concurrencia.
 */
internal suspend fun <T> llamadaRemota(bloque: suspend () -> T): T {
    try {
        return bloque()
    } catch (cancelacion: CancellationException) {
        throw cancelacion
    } catch (fallo: Throwable) {
        throw ErrorDeRedException(fallo.mensajeLegible(), fallo)
    }
}
