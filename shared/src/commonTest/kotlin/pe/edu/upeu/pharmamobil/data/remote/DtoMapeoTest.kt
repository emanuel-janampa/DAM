package pe.edu.upeu.pharmamobil.data.remote

import kotlinx.serialization.json.Json
import pe.edu.upeu.pharmamobil.data.mapper.CATEGORIA_POR_DEFECTO
import pe.edu.upeu.pharmamobil.data.mapper.toDomain
import pe.edu.upeu.pharmamobil.data.mapper.toRequestDto
import pe.edu.upeu.pharmamobil.data.remote.dto.ClienteDto
import pe.edu.upeu.pharmamobil.data.remote.dto.PaginaDto
import pe.edu.upeu.pharmamobil.data.remote.dto.ProductoDto
import pe.edu.upeu.pharmamobil.data.remote.dto.toDomain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Comprueba el contrato JSON real del backend sin necesitar red. */
class DtoMapeoTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Forma exacta de PaginaResponseDTO + ProductoResponseDTO del backend.
    private val respuestaProductos = """
        {
          "contenido": [
            {
              "id": 1, "nombre": "Paracetamol 500mg", "precio": 2.50, "stock": 100,
              "estado": true, "categoriaId": 1, "categoriaNombre": "Analgésicos",
              "fechaCreacion": "2026-09-30T20:00:00", "fechaModificacion": null
            }
          ],
          "pagina": 0, "tamanio": 20, "totalElementos": 1, "totalPaginas": 1, "ultima": true
        }
    """.trimIndent()

    @Test
    fun deserializaLaPaginaDeProductosYLaMapeaAlDominio() {

        val pagina = json.decodeFromString<PaginaDto<ProductoDto>>(respuestaProductos)
        val productos = pagina.contenido.map { it.toDomain() }

        assertEquals(1, productos.size)
        assertEquals("Paracetamol 500mg", productos[0].nombre)
        assertEquals(2.5, productos[0].precio)
        assertEquals(100, productos[0].stock)
        assertTrue(pagina.ultima)
    }

    @Test
    fun deserializaLaPaginaDeClientesYLaMapeaAlDominio() {

        val texto = """
            {"contenido":[{"id":7,"dni":"12345678","nombres":"Ana","apellidos":"Lopez",
            "email":"ana@pharma.pe","telefono":null,"direccion":null,"estado":true,
            "fechaCreacion":"2026-09-30T20:00:00","fechaModificacion":null}],
            "pagina":0,"tamanio":20,"totalElementos":1,"totalPaginas":1,"ultima":true}
        """.trimIndent()

        val clientes = json.decodeFromString<PaginaDto<ClienteDto>>(texto)
            .contenido.map { it.toDomain() }

        assertEquals("Ana Lopez", clientes[0].nombre)
        assertEquals("ana@pharma.pe", clientes[0].correo)
        assertEquals(null, clientes[0].telefono)
    }

    @Test
    fun laPaginaVaciaNoRompeLaDeserializacion() {

        val pagina = json.decodeFromString<PaginaDto<ProductoDto>>("""{"contenido":[]}""")

        assertTrue(pagina.contenido.isEmpty())
    }

    @Test
    fun elProductoSeEnviaConLaCategoriaPorDefecto() {

        val dto = ProductoDto(id = 3, nombre = "Ibuprofeno", precio = 4.0, stock = 20)
            .toDomain()
            .toRequestDto()

        assertEquals(CATEGORIA_POR_DEFECTO, dto.categoriaId)
        assertEquals("Ibuprofeno", dto.nombre)
    }

    @Test
    fun unFalloCualquieraSeConvierteEnMensajeLegible() {

        val mensaje = IllegalStateException("boom").mensajeLegible()

        assertTrue(mensaje.contains("No se pudo conectar"))
    }
}
