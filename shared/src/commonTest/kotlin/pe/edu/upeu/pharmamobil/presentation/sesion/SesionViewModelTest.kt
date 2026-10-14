package pe.edu.upeu.pharmamobil.presentation.sesion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.pharmamobil.data.repository.FakeAuthRepository
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.domain.usecase.CerrarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.IniciarSesionUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.SesionGuardadaUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SesionViewModelTest {

    private val sesion = Sesion("t", "admin", "Admin", setOf("CATALOGO_LEER"))

    @BeforeTest
    fun instalarMain() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun restaurarMain() {
        Dispatchers.resetMain()
    }

    private fun nuevoViewModel(
        repositorio: FakeAuthRepository,
        gestor: GestorSesion = GestorSesion(CerrarSesionUseCase(repositorio))
    ) = SesionViewModel(
        iniciarSesion = IniciarSesionUseCase(repositorio),
        sesionGuardada = SesionGuardadaUseCase(repositorio),
        gestorSesion = gestor
    )

    @Test
    fun sinTokenGuardadoTerminaDeRestaurarSinSesion() = runTest {

        val viewModel = nuevoViewModel(FakeAuthRepository())

        assertFalse(viewModel.uiState.value.restaurando)
        assertNull(viewModel.uiState.value.sesion)
    }

    @Test
    fun conTokenGuardadoEntraDirectoSinPedirCredenciales() = runTest {

        val viewModel = nuevoViewModel(FakeAuthRepository(sesionGuardada = sesion))

        assertEquals(sesion, viewModel.uiState.value.sesion)
    }

    @Test
    fun unTokenVencidoAlAbrirMuestraElAvisoEnElLogin() = runTest {

        val repositorio = FakeAuthRepository().apply {
            fallaAlRestaurar = ErrorSesionException(ErrorSesion.SesionExpirada)
        }

        val viewModel = nuevoViewModel(repositorio)

        assertNull(viewModel.uiState.value.sesion)
        assertNotNull(viewModel.uiState.value.aviso)
    }

    @Test
    fun elLoginCorrectoAbreLaSesion() = runTest {

        val viewModel = nuevoViewModel(FakeAuthRepository(sesionAlIniciar = sesion))

        viewModel.onUsuarioChange("admin")
        viewModel.onClaveChange("Admin123")
        viewModel.iniciar()

        assertEquals(sesion, viewModel.uiState.value.sesion)
        assertFalse(viewModel.uiState.value.cargando)
        assertEquals("", viewModel.uiState.value.clave)
    }

    @Test
    fun camposVaciosMuestranErroresPorCampo() = runTest {

        val viewModel = nuevoViewModel(FakeAuthRepository(sesionAlIniciar = sesion))

        viewModel.iniciar()

        assertNotNull(viewModel.uiState.value.usuarioError)
        assertNotNull(viewModel.uiState.value.claveError)
        assertNull(viewModel.uiState.value.sesion)
    }

    @Test
    fun credencialesInvalidasMuestranElMensajeYNoAbrenSesion() = runTest {

        val repositorio = FakeAuthRepository().apply {
            fallaAlIniciar = ErrorSesionException(ErrorSesion.CredencialesInvalidas)
        }
        val viewModel = nuevoViewModel(repositorio)

        viewModel.onUsuarioChange("admin")
        viewModel.onClaveChange("mala")
        viewModel.iniciar()

        assertEquals("Usuario o contraseña incorrectos.", viewModel.uiState.value.error)
        assertNull(viewModel.uiState.value.sesion)
    }

    @Test
    fun cerrarSesionBorraElTokenYVuelveAlLogin() = runTest {

        val repositorio = FakeAuthRepository(sesionGuardada = sesion)
        val viewModel = nuevoViewModel(repositorio)

        viewModel.cerrarSesion()

        assertNull(viewModel.uiState.value.sesion)
        assertEquals(1, repositorio.cierres)
    }

    @Test
    fun el401CierraLaSesionConAvisoYElOtroFalloNo() = runTest {

        val repositorio = FakeAuthRepository(sesionGuardada = sesion)
        val gestor = GestorSesion(CerrarSesionUseCase(repositorio))
        val viewModel = nuevoViewModel(repositorio, gestor)

        // 403: la sesion se mantiene.
        val cerroPorPermiso = gestor.alFallar(ErrorSesionException(ErrorSesion.SinPermiso))
        assertFalse(cerroPorPermiso)
        assertEquals(sesion, viewModel.uiState.value.sesion)

        // 401: la sesion se cierra y el login muestra el aviso.
        val cerroPorExpiracion = gestor.alFallar(ErrorSesionException(ErrorSesion.SesionExpirada))
        assertTrue(cerroPorExpiracion)
        assertNull(viewModel.uiState.value.sesion)
        assertNotNull(viewModel.uiState.value.aviso)
    }
}
