package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import pe.edu.upeu.pharmamobil.data.remote.dto.PaginaDto
import pe.edu.upeu.pharmamobil.data.remote.dto.ProductoDto
import pe.edu.upeu.pharmamobil.data.remote.dto.ProductoRequestDto

class ProductoApi(private val client: HttpClient) {

    suspend fun obtenerProductos(tamanio: Int = 50): List<ProductoDto> =
        client.get("/api/v1/productos") {
            parameter("tamanio", tamanio)
        }.body<PaginaDto<ProductoDto>>().contenido

    suspend fun crearProducto(request: ProductoRequestDto): ProductoDto =
        client.post("/api/v1/productos") {
            setBody(request)
        }.body()
}
