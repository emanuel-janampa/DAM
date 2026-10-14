package pe.edu.upeu.pharmamobil.presentation.producto

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
import pe.edu.upeu.pharmamobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ProductoInvalidoException
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarProductoUseCase


class ProductoViewModel(
    private val registrarProducto: RegistrarProductoUseCase,
<<<<<<< HEAD
    private val listarProductos: ListarProductosUseCase,
    private val gestorSesion: GestorSesion
=======
    private val listarProductos: ListarProductosUseCase
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(fase = ProductoUiState.Fase.Cargando)
            }

            val fase = listarProductos().fold(
                onSuccess = { productos ->
                    if (productos.isEmpty()) {
                        ProductoUiState.Fase.SinProductos
                    } else {
                        ProductoUiState.Fase.ConProductos(productos.map { it.aUi() })
                    }
                },
                onFailure = { fallo ->
<<<<<<< HEAD
                    // 401: cierra la sesion y lleva al login. 403: se queda y avisa.
                    gestorSesion.alFallar(fallo)
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                    ProductoUiState.Fase.Error(
                        fallo.message ?: "No se pudo cargar el inventario"
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

    fun onPrecioChange(precio: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(precio = precio, precioError = null),
<<<<<<< HEAD
                mensajeExito = null,
                avisoError = null
=======
                mensajeExito = null
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            )
        }
    }

    fun onStockChange(stock: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(stock = stock, stockError = null),
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

            registrarProducto(
                nombre = formulario.nombre,
                precio = formulario.precio,
                stock = formulario.stock
            ).fold(
                onSuccess = { producto ->
                    _uiState.update {
                        it.copy(
                            registrando = false,
                            formulario = FormularioProducto(),
                            mensajeExito = "Producto \"${producto.nombre}\" registrado correctamente"
                        )
                    }
                    cargarProductos()
                },
                onFailure = { fallo ->
                    when (fallo) {

                        is ProductoInvalidoException -> _uiState.update {
                            it.copy(
                                registrando = false,
                                formulario = it.formulario.copy(
                                    nombreError = fallo.errores.nombre,
                                    precioError = fallo.errores.precio,
                                    stockError = fallo.errores.stock
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
                                fase = ProductoUiState.Fase.Error(
                                    fallo.message ?: "No se pudo registrar el producto"
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}
