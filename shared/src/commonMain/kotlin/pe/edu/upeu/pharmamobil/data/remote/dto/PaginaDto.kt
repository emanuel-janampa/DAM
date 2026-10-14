package pe.edu.upeu.pharmamobil.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Envoltorio que usa el backend (PaginaResponseDTO) en todos los listados:
 * los elementos vienen dentro de "contenido", no como arreglo JSON directo.
 */
@Serializable
data class PaginaDto<T>(
    val contenido: List<T> = emptyList(),
    val pagina: Int = 0,
    val tamanio: Int = 0,
    val totalElementos: Long = 0,
    val totalPaginas: Int = 0,
    val ultima: Boolean = true
)
