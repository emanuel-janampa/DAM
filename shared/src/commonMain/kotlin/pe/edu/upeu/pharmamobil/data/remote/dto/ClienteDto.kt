package pe.edu.upeu.pharmamobil.data.remote.dto

import kotlinx.serialization.Serializable
import pe.edu.upeu.pharmamobil.domain.model.Cliente

@Serializable
data class ClienteDto(
    val id: Long? = null,
    val dni: String? = null,
    val nombres: String? = null,
    val apellidos: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val estado: Boolean = true
)

fun ClienteDto.toDomain(): Cliente {
    val nombreCompleto = listOfNotNull(this.nombres, this.apellidos)
        .joinToString(" ")
        .ifBlank { "Cliente sin nombre" }

    return Cliente(
        id = this.id ?: 0L,
        nombre = nombreCompleto,
        correo = this.email?.ifBlank { "sin_correo@pharma.com" } ?: "sin_correo@pharma.com",
        telefono = this.telefono?.ifBlank { null }
    )
}

fun Cliente.toDto(): ClienteDto {
    return ClienteDto(
        id = if (this.id == 0L) null else this.id,
        nombres = this.nombre,
        apellidos = "",
        email = this.correo,
        telefono = this.telefono,
        estado = true
    )
}