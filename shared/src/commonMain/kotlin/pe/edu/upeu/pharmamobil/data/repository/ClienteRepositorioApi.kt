package pe.edu.upeu.pharmamobil.data.repository

import pe.edu.upeu.pharmamobil.data.remote.ClienteApi
import pe.edu.upeu.pharmamobil.data.remote.dto.toDomain
import pe.edu.upeu.pharmamobil.data.remote.dto.toDto
import pe.edu.upeu.pharmamobil.data.remote.llamadaRemota
import pe.edu.upeu.pharmamobil.domain.model.Cliente
import pe.edu.upeu.pharmamobil.domain.repository.ClienteRepository

class ClienteRepositorioApi(
    private val api: ClienteApi
) : ClienteRepository {

    override suspend fun listar(): List<Cliente> = llamadaRemota {
        api.obtenerClientes().map { it.toDomain() }
    }

    override suspend fun registrar(cliente: Cliente): Cliente = llamadaRemota {
        api.crearCliente(cliente.toDto()).toDomain()
    }
}
