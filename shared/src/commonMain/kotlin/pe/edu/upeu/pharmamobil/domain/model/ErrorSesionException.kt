package pe.edu.upeu.pharmamobil.domain.model

/**
 * Lleva un ErrorSesion por las capas sin que ninguna importe Ktor. El mensaje
 * ya es apto para mostrarlo al usuario.
 */
class ErrorSesionException(
    val error: ErrorSesion,
    causa: Throwable? = null
) : Exception(mensajeDe(error), causa)

fun mensajeDe(error: ErrorSesion): String = when (error) {
    ErrorSesion.CredencialesInvalidas -> "Usuario o contraseña incorrectos."
    ErrorSesion.SesionExpirada -> "Tu sesión expiró. Inicia sesión nuevamente."
    ErrorSesion.SinPermiso -> "No tienes permisos para realizar esta operación."
    ErrorSesion.SinConexion -> "No se pudo conectar con el servidor. Revisa tu conexión."
}
