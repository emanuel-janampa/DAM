package pe.edu.upeu.pharmamobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.pharmamobil.data.repository.FakeAuthRepository
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class IniciarSesionUseCaseTest {

    private val sesion = Sesion("t", "admin", "Admin", setOf("CATALOGO_LEER"))

    @Test
    fun rechazaCamposVaciosSinLlamarAlServidor() = runTest {

        val repositorio = FakeAuthRepository(sesionAlIniciar = sesion)

        val resultado = IniciarSesionUseCase(repositorio)("  ", "")

        val fallo = resultado.exceptionOrNull()
        assertIs<CredencialesIncompletasException>(fallo)
        assertEquals("El usuario es obligatorio", fallo.errores.usuario)
        assertEquals("La contraseña es obligatoria", fallo.errores.clave)
    }

    @Test
    fun devuelveLaSesionCuandoElServidorAutentica() = runTest {

        val repositorio = FakeAuthRepository(sesionAlIniciar = sesion)

        val resultado = IniciarSesionUseCase(repositorio)("admin", "Admin123")

        assertEquals(sesion, resultado.getOrNull())
    }

    @Test
    fun propagaElErrorDeSesionDelRepositorio() = runTest {

        val repositorio = FakeAuthRepository().apply {
            fallaAlIniciar = ErrorSesionException(ErrorSesion.CredencialesInvalidas)
        }

        val fallo = IniciarSesionUseCase(repositorio)("admin", "mala").exceptionOrNull()

        assertIs<ErrorSesionException>(fallo)
        assertEquals(ErrorSesion.CredencialesInvalidas, fallo.error)
        assertTrue(fallo.message!!.isNotBlank())
    }
}
