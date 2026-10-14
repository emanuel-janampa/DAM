package pe.edu.upeu.pharmamobil.presentation.sesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.domain.usecase.CredencialesIncompletasException
import pe.edu.upeu.pharmamobil.domain.usecase.IniciarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.SesionGuardadaUseCase

class SesionViewModel(
    private val iniciarSesion: IniciarSesionUseCase,
    private val sesionGuardada: SesionGuardadaUseCase,
    private val gestorSesion: GestorSesion
) : ViewModel() {

    private val _uiState = MutableStateFlow(SesionUiState())
    val uiState: StateFlow<SesionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            gestorSesion.sesion.collect { sesion ->
                _uiState.update { it.copy(sesion = sesion) }
            }
        }

        viewModelScope.launch {
            gestorSesion.aviso.collect { aviso ->
                _uiState.update { it.copy(aviso = aviso) }
            }
        }

        restaurar()
    }

    /** Al arrancar: si el almacen seguro guarda un token vigente, entra directo. */
    private fun restaurar() {

        viewModelScope.launch {

            sesionGuardada().fold(
                onSuccess = { sesion ->
                    if (sesion != null) gestorSesion.abrir(sesion)
                },
                onFailure = { fallo ->
                    if (fallo is ErrorSesionException &&
                        fallo.error == ErrorSesion.SesionExpirada
                    ) {
                        gestorSesion.avisar(fallo.message)
                    }
                }
            )

            _uiState.update { it.copy(restaurando = false) }
        }
    }

    fun onUsuarioChange(usuario: String) {
        _uiState.update {
            it.copy(usuario = usuario, usuarioError = null, error = null)
        }
    }

    fun onClaveChange(clave: String) {
        _uiState.update {
            it.copy(clave = clave, claveError = null, error = null)
        }
    }

    fun iniciar() {

        if (_uiState.value.cargando) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    cargando = true,
                    error = null,
                    usuarioError = null,
                    claveError = null
                )
            }

            val actual = _uiState.value

            iniciarSesion(actual.usuario, actual.clave).fold(
                onSuccess = { sesion ->
                    gestorSesion.abrir(sesion)
                    _uiState.update {
                        it.copy(cargando = false, clave = "")
                    }
                },
                onFailure = { fallo ->
                    when (fallo) {

                        is CredencialesIncompletasException -> _uiState.update {
                            it.copy(
                                cargando = false,
                                usuarioError = fallo.errores.usuario,
                                claveError = fallo.errores.clave
                            )
                        }

                        else -> _uiState.update {
                            it.copy(
                                cargando = false,
                                error = fallo.message ?: "No se pudo iniciar sesión"
                            )
                        }
                    }
                }
            )
        }
    }

    /** "Cerrar sesión": borra el token y devuelve al login con el formulario limpio. */
    fun cerrarSesion() {

        viewModelScope.launch {

            gestorSesion.cerrar()

            _uiState.update {
                it.copy(usuario = "", clave = "", error = null)
            }
        }
    }
}
