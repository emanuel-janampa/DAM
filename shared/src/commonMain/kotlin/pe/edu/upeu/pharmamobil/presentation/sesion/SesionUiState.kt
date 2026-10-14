package pe.edu.upeu.pharmamobil.presentation.sesion

import pe.edu.upeu.pharmamobil.domain.model.Sesion

data class SesionUiState(
    val usuario: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val sesion: Sesion? = null,
    val usuarioError: String? = null,
    val claveError: String? = null,
    /** Aviso informativo en el login, p. ej. "Tu sesión expiró". */
    val aviso: String? = null,
    /** true mientras se busca una sesion guardada al abrir la app. */
    val restaurando: Boolean = true
)
