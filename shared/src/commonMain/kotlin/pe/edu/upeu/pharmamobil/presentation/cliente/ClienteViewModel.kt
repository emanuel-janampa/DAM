package pe.edu.upeu.pharmamobil.presentation.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.presentation.sesion.GestorSesion
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import pe.edu.upeu.pharmamobil.domain.usecase.ClienteInvalidoException
import pe.edu.upeu.pharmamobil.domain.usecase.ListarClientesUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarClienteUseCase


class ClienteViewModel(
    private val registrarCliente: RegistrarClienteUseCase,
<<<<<<< HEAD
    private val listarClientes: ListarClientesUseCase,
    private val gestorSesion: GestorSesion
=======
    private val listarClientes: ListarClientesUseCase
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClienteUiState())
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    init {
        cargarClientes()
    }

    fun cargarClientes() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(fase = ClienteUiState.Fase.Cargando)
            }

            val fase = listarClientes().fold(
                onSuccess = { clientes ->
                    if (clientes.isEmpty()) {
                        ClienteUiState.Fase.SinClientes
                    } else {
                        ClienteUiState.Fase.ConClientes(clientes.map { it.aUi() })
                    }
                },
                onFailure = { fallo ->
<<<<<<< HEAD
                    // 401: cierra la sesion y lleva al login. 403: se queda y avisa.
                    gestorSesion.alFallar(fallo)
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                    ClienteUiState.Fase.Error(
                        fallo.message ?: "No se pudo cargar la cartera de clientes"
                    )
                }
            )

            _uiState.update {
                it.copy(fase = fase)
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(nombre = nombre, nombreError = null),
<<<<<<< HEAD
                mensajeExito = null,
                avisoError = null
=======
                mensajeExito = null
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            )
        }
    }

    fun onCorreoChange(correo: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(correo = correo, correoError = null),
<<<<<<< HEAD
                mensajeExito = null,
                avisoError = null
=======
                mensajeExito = null
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            )
        }
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(telefono = telefono, telefonoError = null),
<<<<<<< HEAD
                mensajeExito = null,
                avisoError = null
=======
                mensajeExito = null
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            )
        }
    }

    fun registrar() {

        if (_uiState.value.registrando) return

        viewModelScope.launch {

            _uiState.update {
<<<<<<< HEAD
                it.copy(registrando = true, mensajeExito = null,
                avisoError = null)
=======
                it.copy(registrando = true, mensajeExito = null)
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            }

            val formulario = _uiState.value.formulario

            registrarCliente(
                nombre = formulario.nombre,
                correo = formulario.correo,
                telefono = formulario.telefono
            ).fold(
                onSuccess = { cliente ->
                    _uiState.update {
                        it.copy(
                            registrando = false,
                            formulario = FormularioCliente(),
                            mensajeExito = "Cliente \"${cliente.nombre}\" registrado correctamente"
                        )
                    }
                    cargarClientes()
                },
                onFailure = { fallo ->
                    when (fallo) {

                        is ClienteInvalidoException -> _uiState.update {
                            it.copy(
                                registrando = false,
                                formulario = it.formulario.copy(
                                    nombreError = fallo.errores.nombre,
                                    correoError = fallo.errores.correo,
                                    telefonoError = fallo.errores.telefono
                                )
                            )
                        }

<<<<<<< HEAD
                        is ErrorSesionException -> {
                            if (fallo.error == ErrorSesion.SesionExpirada) {
                                gestorSesion.alFallar(fallo)
                                _uiState.update { it.copy(registrando = false) }
                            } else {
                                // 403: la sesion sigue; solo se avisa de la falta de permisos.
                                _uiState.update {
                                    it.copy(registrando = false, avisoError = fallo.message)
                                }
                            }
                        }

=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                        else -> _uiState.update {
                            it.copy(
                                registrando = false,
                                fase = ClienteUiState.Fase.Error(
                                    fallo.message ?: "No se pudo registrar el cliente"
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}
