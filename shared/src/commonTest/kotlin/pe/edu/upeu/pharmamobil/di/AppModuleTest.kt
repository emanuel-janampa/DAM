package pe.edu.upeu.pharmamobil.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
<<<<<<< HEAD
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguro
import pe.edu.upeu.pharmamobil.data.local.FakeAlmacenSeguro
import pe.edu.upeu.pharmamobil.data.repository.AuthRepositorioRest
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository
import pe.edu.upeu.pharmamobil.presentation.sesion.GestorSesion
import pe.edu.upeu.pharmamobil.presentation.sesion.PerfilViewModel
import pe.edu.upeu.pharmamobil.presentation.sesion.SesionViewModel
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

/**
 * Comprueba que el grafo se ensambla sin arrancar la aplicacion: si una
 * definicion falta o esta declarada con el tipo equivocado, falla aqui.
 *
 * Se cargan los cuatro modulos, incluido presentationModule: construir un
 * ViewModel es justo lo que se rompe al cambiar un constructor, y antes era
 * lo unico que el grafo no cubria. Como los ViewModel arrancan una carga en
 * su init, hace falta un Dispatchers.Main de prueba.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppModuleTest {

    // Los repositorios ahora son los conectados a la API; los EnMemoria
    // quedan solo como apoyo para pruebas unitarias.

    @BeforeTest
    fun instalarMain() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun detenerKoin() {
        stopKoin()
        Dispatchers.resetMain()
    }

<<<<<<< HEAD
    // El almacen real (Keystore/Keychain) no existe en las pruebas del host: se
    // sustituye por uno en memoria. Como va al final, reemplaza al de platformModule.
    private val sustitutosDePrueba = module {
        single<AlmacenSeguro> { FakeAlmacenSeguro() }
        single(named(ES_DEPURACION)) { false }
    }

    private fun grafoCompleto(): Koin = startKoin {
        modules(
            dataModule,
            domainModule,
            presentationModule,
            platformModule,
            sustitutosDePrueba
        )
=======
    private fun grafoCompleto(): Koin = startKoin {
        modules(dataModule, domainModule, presentationModule, platformModule)
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    }.koin

    @Test
    fun resuelveLosRepositoriosPorSuInterfazDeDominio() {

        val koin = grafoCompleto()

        assertIs<ProductoRepositorioApi>(koin.get<ProductoRepository>())
        assertIs<ClienteRepositorioApi>(koin.get<ClienteRepository>())
    }

    @Test
    fun losRepositoriosSonUnicosEnTodaLaAplicacion() {

        val koin = grafoCompleto()

        assertSame(
            koin.get<ProductoRepository>(),
            koin.get<ProductoRepository>()
        )
        assertSame(
            koin.get<ClienteRepository>(),
            koin.get<ClienteRepository>()
        )
    }

    @Test
    fun resuelveLosCuatroCasosDeUsoConSusRepositorios() {

        val koin = grafoCompleto()

        koin.get<RegistrarProductoUseCase>()
        koin.get<ListarProductosUseCase>()
        koin.get<RegistrarClienteUseCase>()
        koin.get<ListarClientesUseCase>()
    }

    @Test
    fun resuelveLosViewModelConSusCasosDeUso() {

        val koin = grafoCompleto()

        koin.get<ProductoViewModel>()
        koin.get<ClienteViewModel>()
    }
<<<<<<< HEAD

    @Test
    fun resuelveElRepositorioDeAutenticacionYLosCasosDeUsoDeSesion() {

        val koin = grafoCompleto()

        assertIs<AuthRepositorioRest>(koin.get<AuthRepository>())
        koin.get<pe.edu.upeu.pharmamobil.domain.usecase.IniciarSesionUseCase>()
        koin.get<pe.edu.upeu.pharmamobil.domain.usecase.SesionGuardadaUseCase>()
        koin.get<pe.edu.upeu.pharmamobil.domain.usecase.CerrarSesionUseCase>()
    }

    @Test
    fun elGestorDeSesionEsUnicoYLosViewModelDeSesionSeResuelven() {

        val koin = grafoCompleto()

        assertSame(koin.get<GestorSesion>(), koin.get<GestorSesion>())
        koin.get<SesionViewModel>()
        koin.get<PerfilViewModel>()
    }
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
}
