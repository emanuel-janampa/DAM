package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import pe.edu.upeu.pharmamobil.data.remote.dto.ClienteDto
import pe.edu.upeu.pharmamobil.data.remote.dto.PaginaDto

class ClienteApi(private val client: HttpClient) {

<<<<<<< HEAD
=======
    /** El backend responde una pagina ({contenido, pagina, ...}), no un arreglo. */
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    suspend fun obtenerClientes(tamanio: Int = 50): List<ClienteDto> =
        client.get("api/v1/clientes") {
            parameter("tamanio", tamanio)
        }.body<PaginaDto<ClienteDto>>().contenido

    suspend fun crearCliente(clienteDto: ClienteDto): ClienteDto =
        client.post("api/v1/clientes") {
            setBody(clienteDto)
        }.body()
}
