package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
<<<<<<< HEAD
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

=======
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Error de la capa de datos con un mensaje ya apto para el usuario. Las capas
 * superiores solo ven este tipo: no necesitan importar Ktor.
 */
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
class ErrorDeRedException(
    mensaje: String,
    causa: Throwable? = null
) : Exception(mensaje, causa)

<<<<<<< HEAD
=======
/** Traduce cualquier fallo de red o de deserializacion a un texto legible. */
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
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

<<<<<<< HEAD
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

=======
/**
 * Ejecuta una llamada remota y convierte cualquier fallo en ErrorDeRedException.
 * La cancelacion de corrutinas se deja pasar para no romper la concurrencia.
 */
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
internal suspend fun <T> llamadaRemota(bloque: suspend () -> T): T {
    try {
        return bloque()
    } catch (cancelacion: CancellationException) {
        throw cancelacion
<<<<<<< HEAD
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
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    } catch (fallo: Throwable) {
        throw ErrorDeRedException(fallo.mensajeLegible(), fallo)
    }
}
<<<<<<< HEAD

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
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
