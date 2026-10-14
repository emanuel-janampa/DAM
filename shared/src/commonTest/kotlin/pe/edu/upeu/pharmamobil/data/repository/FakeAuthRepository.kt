package pe.edu.upeu.pharmamobil.data.repository

import pe.edu.upeu.pharmamobil.domain.model.Credenciales
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository

/** Doble de AuthRepository: devuelve lo que se le configure y cuenta los cierres. */
class FakeAuthRepository(
    var sesionAlIniciar: Sesion? = null,
    var sesionGuardada: Sesion? = null
) : AuthRepository {

    var fallaAlIniciar: Throwable? = null
    var fallaAlRestaurar: Throwable? = null
    var cierres = 0
        private set

    override suspend fun iniciarSesion(credenciales: Credenciales): Sesion {
        fallaAlIniciar?.let { throw it }
        return sesionAlIniciar ?: error("Sin sesion configurada")
    }

    override suspend fun sesionGuardada(): Sesion? {
        fallaAlRestaurar?.let { throw it }
        return sesionGuardada
    }

    override suspend fun cerrarSesion() {
        cierres++
        sesionGuardada = null
    }
}
