package pe.edu.upeu.pharmamobil.data.repository

import pe.edu.upeu.pharmamobil.data.mapper.toDomain
import pe.edu.upeu.pharmamobil.data.mapper.toRequestDto
import pe.edu.upeu.pharmamobil.data.remote.ProductoApi
import pe.edu.upeu.pharmamobil.data.remote.llamadaRemota
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository

class ProductoRepositorioApi(
    private val api: ProductoApi
) : ProductoRepository {

    override suspend fun listar(): List<Producto> = llamadaRemota {
        api.obtenerProductos().map { it.toDomain() }
    }

    override suspend fun registrar(producto: Producto): Producto = llamadaRemota {
        api.crearProducto(producto.toRequestDto()).toDomain()
    }
}
