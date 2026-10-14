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
<<<<<<< HEAD
        client.get("/api/v1/productos") {
=======
        client.get("api/v1/productos") {
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            parameter("tamanio", tamanio)
        }.body<PaginaDto<ProductoDto>>().contenido

    suspend fun crearProducto(request: ProductoRequestDto): ProductoDto =
<<<<<<< HEAD
        client.post("/api/v1/productos") {
=======
        client.post("api/v1/productos") {
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
            setBody(request)
        }.body()
}
