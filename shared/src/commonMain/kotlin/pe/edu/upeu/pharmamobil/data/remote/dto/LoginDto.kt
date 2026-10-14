package pe.edu.upeu.pharmamobil.data.remote.dto

import kotlinx.serialization.Serializable
import pe.edu.upeu.pharmamobil.domain.model.Sesion

/** Cuerpo de POST /api/v1/auth/login (LoginRequestDTO del backend). */
@Serializable
data class LoginRequestDto(val username: String, val password: String)

/** Respuesta del login (LoginResponseDTO del backend). */
@Serializable
data class LoginResponseDto(
    val token: String,
    val tipo: String = "Bearer",
    val expiraEnSegundos: Long = 0,
    val username: String,
    val nombreCompleto: String = "",
    val roles: List<String> = emptyList(),
    val permisos: List<String> = emptyList()
)

/** Respuesta de GET /api/v1/auth/me (UsuarioResponseDTO del backend). */
@Serializable
data class UsuarioActualDto(
    val username: String,
    val empleadoNombre: String? = null,
    val roles: List<String> = emptyList(),
    val permisos: List<String> = emptyList()
)

fun LoginResponseDto.aSesion(): Sesion = Sesion(
    token = token,
    usuario = username,
    nombreCompleto = nombreCompleto.ifBlank { username },
    permisos = permisos.toSet()
)

/** El /me no repite el token: se vuelve a adjuntar el que ya esta guardado. */
fun UsuarioActualDto.aSesion(token: String): Sesion = Sesion(
    token = token,
    usuario = username,
    nombreCompleto = empleadoNombre?.ifBlank { null } ?: username,
    permisos = permisos.toSet()
)
