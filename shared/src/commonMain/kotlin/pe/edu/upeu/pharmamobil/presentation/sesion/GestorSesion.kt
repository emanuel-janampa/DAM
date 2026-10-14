package pe.edu.upeu.pharmamobil.presentation.sesion

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.domain.usecase.CerrarSesionUseCase

/**
 * Unica fuente de verdad de "hay sesion o no". Es un singleton porque el 401
 * puede llegar desde cualquier pantalla y debe llevar al login desde donde sea.
 * La interfaz solo ve la Sesion (permisos, nombre); nunca el token.
 */
class GestorSesion(
    private val cerrarSesion: CerrarSesionUseCase
) {

    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    fun abrir(sesion: Sesion) {
        _aviso.value = null
        _sesion.value = sesion
    }

    fun avisar(texto: String?) {
        _aviso.value = texto
    }

    /** Borra el token (almacen y cache de Ktor), limpia el estado y avisa. */
    suspend fun cerrar(aviso: String? = null) {
        cerrarSesion()
        _sesion.value = null
        _aviso.value = aviso
    }

    /**
     * Para el onFailure de cualquier ViewModel:
     *  - 401 (SesionExpirada): cierra la sesion con aviso y devuelve true;
     *  - 403 (SinPermiso) y el resto: no toca la sesion y devuelve false.
     */
    suspend fun alFallar(fallo: Throwable): Boolean {

        if (fallo is ErrorSesionException && fallo.error == ErrorSesion.SesionExpirada) {
            cerrar(fallo.message)
            return true
        }

        return false
    }
}
