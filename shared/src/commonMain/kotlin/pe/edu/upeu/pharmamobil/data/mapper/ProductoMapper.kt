package pe.edu.upeu.pharmamobil.data.mapper

import pe.edu.upeu.pharmamobil.data.remote.dto.ProductoDto
import pe.edu.upeu.pharmamobil.data.remote.dto.ProductoRequestDto
import pe.edu.upeu.pharmamobil.domain.model.Producto

fun ProductoDto.toDomain(): Producto = Producto(
    id = id ?: 0L,
    nombre = nombre,
    precio = precio,
    stock = stock
)
fun Producto.toRequestDto(categoriaId: Long = CATEGORIA_POR_DEFECTO): ProductoRequestDto =
    ProductoRequestDto(
        nombre = nombre,
        precio = precio,
        stock = stock,
        estado = true,
        categoriaId = categoriaId
    )

const val CATEGORIA_POR_DEFECTO = 1L
