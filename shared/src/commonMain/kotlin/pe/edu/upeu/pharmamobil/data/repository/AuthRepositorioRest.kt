package pe.edu.upeu.pharmamobil.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguro
import pe.edu.upeu.pharmamobil.data.local.segundosActuales
import pe.edu.upeu.pharmamobil.data.remote.ErrorDeRedException
import pe.edu.upeu.pharmamobil.data.remote.dto.LoginRequestDto
import pe.edu.upeu.pharmamobil.data.remote.dto.LoginResponseDto
import pe.edu.upeu.pharmamobil.data.remote.dto.UsuarioActualDto
import pe.edu.upeu.pharmamobil.data.remote.dto.aSesion
import pe.edu.upeu.pharmamobil.data.remote.leerClaimsJwt
import pe.edu.upeu.pharmamobil.data.remote.llamadaDeLogin
import pe.edu.upeu.pharmamobil.data.remote.llamadaRemota
import pe.edu.upeu.pharmamobil.domain.model.Credenciales
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesion
import pe.edu.upeu.pharmamobil.domain.model.ErrorSesionException
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.domain.repository.AuthRepository

/**
 * Traduce la respuesta HTTP al modelo de dominio y guarda el token. Ninguna
 * llamada escribe la cabecera Authorization: de eso se encarga el plugin Auth.
 */
class AuthRepositorioRest(
    private val cliente: HttpClient,
    private val almacen: AlmacenSeguro,
    private val ahoraEnSegundos: () -> Long = ::segundosActuales
) : AuthRepository {

    override suspend fun iniciarSesion(credenciales: Credenciales): Sesion =
        llamadaDeLogin {

            val dto: LoginResponseDto = cliente.post("api/v1/auth/login") {
                setBody(LoginRequestDto(credenciales.usuario, credenciales.clave))
            }.body()

            almacen.guardarToken(dto.token)
            olvidarTokenEnCache()

            dto.aSesion()
        }

    /**
     * Sesion al abrir la app:
     *  - sin token guardado -> null (hay que iniciar sesion);
     *  - token vencido (claim exp) -> se borra y se lanza SesionExpirada;
     *  - token vigente -> se pide GET auth/me para recuperar nombre y permisos
     *    y, si no hay red, se reconstruyen desde los claims del propio token.
     */
    override suspend fun sesionGuardada(): Sesion? {

        val token = almacen.leerToken() ?: return null

        val claims = leerClaimsJwt(token)

        if (claims == null) {
            // Token ilegible: no sirve de nada conservarlo.
            cerrarSesion()
            return null
        }

        if (claims.expiro(ahoraEnSegundos())) {
            cerrarSesion()
            throw ErrorSesionException(ErrorSesion.SesionExpirada)
        }

        olvidarTokenEnCache()

        return try {
            llamadaRemota {
                cliente.get("api/v1/auth/me").body<UsuarioActualDto>()
            }.aSesion(token)
        } catch (sesion: ErrorSesionException) {
            if (sesion.error == ErrorSesion.SesionExpirada) {
                cerrarSesion()
                throw sesion
            }
            sesionDesdeClaims(token, claims.sub, claims.permisos)
        } catch (red: ErrorDeRedException) {
            sesionDesdeClaims(token, claims.sub, claims.permisos)
        }
    }

    override suspend fun cerrarSesion() {
        almacen.borrarToken()
        olvidarTokenEnCache()
    }

    private fun sesionDesdeClaims(
        token: String,
        usuario: String,
        permisos: Set<String>
    ) = Sesion(
        token = token,
        usuario = usuario,
        nombreCompleto = usuario,
        permisos = permisos
    )

    /**
     * loadTokens se consulta una sola vez y su resultado queda en cache: tras
     * guardar o borrar el token hay que forzar que vuelva a leer el almacen.
     */
    private fun olvidarTokenEnCache() {
        cliente.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
