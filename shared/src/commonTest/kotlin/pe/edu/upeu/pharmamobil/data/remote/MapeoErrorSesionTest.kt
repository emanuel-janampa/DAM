package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.http.HttpStatusCode
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import kotlin.test.Test
import kotlin.test.assertEquals

class MapeoErrorSesionTest {

    @Test
    fun un401DelLoginSonCredencialesInvalidas() {
        assertEquals(
            ErrorSesion.CredencialesInvalidas,
            mapearErrorSesion(HttpStatusCode.Unauthorized, "Usuario o contraseña incorrectos")
        )
    }

    @Test
    fun un401QueHablaDeExpiracionEsSesionExpirada() {
        assertEquals(
            ErrorSesion.SesionExpirada,
            mapearErrorSesion(
                HttpStatusCode.Unauthorized,
                "El token ha expirado. Inicie sesión nuevamente"
            )
        )
    }

    @Test
    fun un403EsSinPermiso() {
        assertEquals(
            ErrorSesion.SinPermiso,
            mapearErrorSesion(HttpStatusCode.Forbidden, null)
        )
    }

    @Test
    fun cualquierOtroCodigoEsSinConexion() {
        assertEquals(
            ErrorSesion.SinConexion,
            mapearErrorSesion(HttpStatusCode.InternalServerError, null)
        )
    }
}
