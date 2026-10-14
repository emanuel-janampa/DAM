package pe.edu.upeu.pharmamobil.domain.usecase

import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository

/** Al abrir la app: devuelve la sesion guardada, o null si no hay ninguna. */
class SesionGuardadaUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Sesion?> = resultadoDe {
        authRepository.sesionGuardada()
    }
}
