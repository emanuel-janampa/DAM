package pe.edu.upeu.pharmamobil.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pe.edu.upeu.pharmamobil.data.remote.ClienteApi
import pe.edu.upeu.pharmamobil.data.remote.ProductoApi
import pe.edu.upeu.pharmamobil.data.remote.crearHttpClient
import pe.edu.upeu.pharmamobil.data.repository.ClienteRepositorioApi
import pe.edu.upeu.pharmamobil.data.repository.ProductoRepositorioApi
import pe.edu.upeu.pharmamobil.domain.repository.ClienteRepository
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository
import pe.edu.upeu.pharmamobil.domain.usecase.ListarClientesUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarClienteUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarProductoUseCase
import pe.edu.upeu.pharmamobil.presentation.cliente.ClienteViewModel
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoViewModel

/** Nombre del qualifier con la URL base; cada plataforma declara su valor. */
const val BASE_URL = "baseUrl"

val dataModule = module {
    single { crearHttpClient(get(), get(named(BASE_URL))) }

    single { ClienteApi(get()) }
    single { ProductoApi(get()) }

    single<ProductoRepository> { ProductoRepositorioApi(get()) }

    single<ClienteRepository> { ClienteRepositorioApi(get()) }
}

val domainModule = module {
    factory { RegistrarProductoUseCase(productoRepository = get()) }
    factory { ListarProductosUseCase(productoRepository = get()) }
    factory { RegistrarClienteUseCase(clienteRepository = get()) }
    factory { ListarClientesUseCase(clienteRepository = get()) }
}

val presentationModule = module {
    viewModel { ProductoViewModel(registrarProducto = get(), listarProductos = get()) }
    viewModel { ClienteViewModel(registrarCliente = get(), listarClientes = get()) }
}

expect val platformModule: Module

fun initKoin(configuracionAdicional: KoinAppDeclaration = {}) {
    startKoin {
        configuracionAdicional()
        modules(
            dataModule,
            domainModule,
            presentationModule,
            platformModule
        )
    }
}