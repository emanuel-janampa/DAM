package pe.edu.upeu.pharmamobil.data.remote.dto

import kotlinx.serialization.Serializable

/** Contrato de GET /api/v1/productos (ProductoResponseDTO del backend). */
@Serializable
data class ProductoDto(
    val id: Long? = null,
    val nombre: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val estado: Boolean = true,
    val categoriaId: Long? = null,
    val categoriaNombre: String? = null
)

/** Cuerpo de POST /api/v1/productos (ProductoRequestDTO del backend). */
@Serializable
data class ProductoRequestDto(
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val estado: Boolean = true,
    val categoriaId: Long
)
