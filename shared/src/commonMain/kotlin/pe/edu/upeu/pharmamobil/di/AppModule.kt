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
import pe.edu.upeu.pharmamobil.data.repository.AuthRepositorioRest
import pe.edu.upeu.pharmamobil.data.repository.ClienteRepositorioApi
import pe.edu.upeu.pharmamobil.data.repository.ProductoRepositorioApi
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository
import pe.edu.upeu.pharmamobil.domain.repository.ClienteRepository
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository
import pe.edu.upeu.pharmamobil.domain.usecase.CerrarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.IniciarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ListarClientesUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarClienteUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarProductoUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.SesionGuardadaUseCase
import pe.edu.upeu.pharmamobil.presentation.cliente.ClienteViewModel
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoViewModel
import pe.edu.upeu.pharmamobil.presentation.sesion.GestorSesion
import pe.edu.upeu.pharmamobil.presentation.sesion.PerfilViewModel
import pe.edu.upeu.pharmamobil.presentation.sesion.SesionViewModel

/** Nombre del qualifier con la URL base; cada plataforma declara su valor. */
const val BASE_URL = "baseUrl"

/** Qualifier del Boolean que activa el registro de Ktor (solo en depuracion). */
const val ES_DEPURACION = "esDepuracion"

val dataModule = module {
    // AlmacenSeguro y ES_DEPURACION los aporta cada plataforma (platformModule).
    single {
        crearHttpClient(
            engine = get(),
            baseUrl = get(named(BASE_URL)),
            almacen = get(),
            esDepuracion = get(named(ES_DEPURACION))
        )
    }

    single { ClienteApi(get()) }
    single { ProductoApi(get()) }

    single<AuthRepository> { AuthRepositorioRest(cliente = get(), almacen = get()) }

    single<ProductoRepository> { ProductoRepositorioApi(get()) }

    single<ClienteRepository> { ClienteRepositorioApi(get()) }
}

val domainModule = module {
    factory { IniciarSesionUseCase(authRepository = get()) }
    factory { SesionGuardadaUseCase(authRepository = get()) }
    factory { CerrarSesionUseCase(authRepository = get()) }
    factory { RegistrarProductoUseCase(productoRepository = get()) }
    factory { ListarProductosUseCase(productoRepository = get()) }
    factory { RegistrarClienteUseCase(clienteRepository = get()) }
    factory { ListarClientesUseCase(clienteRepository = get()) }
}

val presentationModule = module {
    // Unico en toda la app: lo comparten el login y los ViewModel que reciben 401.
    single { GestorSesion(cerrarSesion = get()) }

    viewModel {
        SesionViewModel(
            iniciarSesion = get(),
            sesionGuardada = get(),
            gestorSesion = get()
        )
    }
    viewModel { PerfilViewModel(listarClientes = get(), gestorSesion = get()) }
    viewModel {
        ProductoViewModel(
            registrarProducto = get(),
            listarProductos = get(),
            gestorSesion = get()
        )
    }
    viewModel {
        ClienteViewModel(
            registrarCliente = get(),
            listarClientes = get(),
            gestorSesion = get()
        )
    }
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