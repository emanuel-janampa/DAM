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
<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.data.repository.AuthRepositorioRest
import pe.edu.upeu.pharmamobil.data.repository.ClienteRepositorioApi
import pe.edu.upeu.pharmamobil.data.repository.ProductoRepositorioApi
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository
import pe.edu.upeu.pharmamobil.domain.repository.ClienteRepository
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository
import pe.edu.upeu.pharmamobil.domain.usecase.CerrarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.IniciarSesionUseCase
=======
import pe.edu.upeu.pharmamobil.data.repository.ClienteRepositorioApi
import pe.edu.upeu.pharmamobil.data.repository.ProductoRepositorioApi
import pe.edu.upeu.pharmamobil.domain.repository.ClienteRepository
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import pe.edu.upeu.pharmamobil.domain.usecase.ListarClientesUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarClienteUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarProductoUseCase
<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.domain.usecase.SesionGuardadaUseCase
import pe.edu.upeu.pharmamobil.presentation.cliente.ClienteViewModel
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoViewModel
import pe.edu.upeu.pharmamobil.presentation.sesion.GestorSesion
import pe.edu.upeu.pharmamobil.presentation.sesion.PerfilViewModel
import pe.edu.upeu.pharmamobil.presentation.sesion.SesionViewModel
=======
import pe.edu.upeu.pharmamobil.presentation.cliente.ClienteViewModel
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoViewModel
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190

/** Nombre del qualifier con la URL base; cada plataforma declara su valor. */
const val BASE_URL = "baseUrl"

<<<<<<< HEAD
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
=======
val dataModule = module {
    single { crearHttpClient(get(), get(named(BASE_URL))) }
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190

    single { ClienteApi(get()) }
    single { ProductoApi(get()) }

<<<<<<< HEAD
    single<AuthRepository> { AuthRepositorioRest(cliente = get(), almacen = get()) }

=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    single<ProductoRepository> { ProductoRepositorioApi(get()) }

    single<ClienteRepository> { ClienteRepositorioApi(get()) }
}

val domainModule = module {
<<<<<<< HEAD
    factory { IniciarSesionUseCase(authRepository = get()) }
    factory { SesionGuardadaUseCase(authRepository = get()) }
    factory { CerrarSesionUseCase(authRepository = get()) }
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    factory { RegistrarProductoUseCase(productoRepository = get()) }
    factory { ListarProductosUseCase(productoRepository = get()) }
    factory { RegistrarClienteUseCase(clienteRepository = get()) }
    factory { ListarClientesUseCase(clienteRepository = get()) }
}

val presentationModule = module {
<<<<<<< HEAD
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
=======
    viewModel { ProductoViewModel(registrarProducto = get(), listarProductos = get()) }
    viewModel { ClienteViewModel(registrarCliente = get(), listarClientes = get()) }
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
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