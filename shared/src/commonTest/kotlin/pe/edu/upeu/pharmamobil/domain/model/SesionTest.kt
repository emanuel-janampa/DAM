package pe.edu.upeu.pharmamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SesionTest {

    private val sesion = Sesion(
        token = "t",
        usuario = "almacen",
        nombreCompleto = "Almacén",
        permisos = setOf(Permisos.CATALOGO_LEER, Permisos.CATALOGO_ESCRIBIR)
    )

    @Test
    fun puedeSoloLoQueElArregloDePermisosIncluye() {
        assertTrue(sesion.puede(Permisos.CATALOGO_ESCRIBIR))
        assertFalse(sesion.puede(Permisos.CLIENTES_LEER))
    }
}
