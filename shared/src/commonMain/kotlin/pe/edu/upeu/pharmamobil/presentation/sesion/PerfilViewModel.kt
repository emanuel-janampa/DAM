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
import pe.edu.upeu.pharmamobil.domain.usecase.ListarClientesUseCase

/** Resultado de la prueba de acceso mostrada en la pantalla "Mi sesión". */
data class PruebaAcceso(
    val ejecutando: Boolean = false,
    val mensaje: String? = null,
    val exito: Boolean = false
)

/**
 * Permite comprobar en vivo lo que el servidor responde a cada rol (la matriz
 * de la seccion 6 de la guia): p. ej. almacen recibe 403 al pedir clientes.
 */
class PerfilViewModel(
    private val listarClientes: ListarClientesUseCase,
    private val gestorSesion: GestorSesion
) : ViewModel() {

    private val _prueba = MutableStateFlow(PruebaAcceso())
    val prueba: StateFlow<PruebaAcceso> = _prueba.asStateFlow()

    fun probarAccesoAClientes() {

        if (_prueba.value.ejecutando) return

        viewModelScope.launch {

            _prueba.value = PruebaAcceso(ejecutando = true)

            listarClientes().fold(
                onSuccess = { clientes ->
                    _prueba.update {
                        PruebaAcceso(
                            mensaje = "200 · Acceso permitido (${clientes.size} clientes).",
                            exito = true
                        )
                    }
                },
                onFailure = { fallo ->

                    // 401 cierra la sesion; 403 la mantiene y solo informa.
                    if (gestorSesion.alFallar(fallo)) {
                        _prueba.value = PruebaAcceso()
                    } else {
                        val codigo = if (
                            fallo is ErrorSesionException &&
                            fallo.error == ErrorSesion.SinPermiso
                        ) "403 · " else ""

                        _prueba.value = PruebaAcceso(
                            mensaje = codigo + (fallo.message ?: "No se pudo completar la prueba."),
                            exito = false
                        )
                    }
                }
            )
        }
    }
}
