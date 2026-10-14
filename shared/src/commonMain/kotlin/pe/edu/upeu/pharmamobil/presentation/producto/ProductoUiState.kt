package pe.edu.upeu.pharmamobil.presentation.producto

data class ProductoUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioProducto = FormularioProducto(),
    val registrando: Boolean = false,
<<<<<<< HEAD
    val mensajeExito: String? = null,
    /** Fallo que no reemplaza el listado, p. ej. 403 al registrar. */
    val avisoError: String? = null
=======
    val mensajeExito: String? = null
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
) {

    /** Fases excluyentes del inventario: solo una puede estar activa. */
    sealed interface Fase {

        data object Cargando : Fase

        data object SinProductos : Fase

        data class ConProductos(val productos: List<ProductoUi>) : Fase

        data class Error(val mensaje: String) : Fase
    }
}

data class FormularioProducto(
    val nombre: String = "",
    val precio: String = "",
    val stock: String = "",
    val nombreError: String? = null,
    val precioError: String? = null,
    val stockError: String? = null
)
