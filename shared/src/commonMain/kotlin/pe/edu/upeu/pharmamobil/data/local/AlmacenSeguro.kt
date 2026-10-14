package pe.edu.upeu.pharmamobil.data.local

/**
 * Almacen cifrado del token JWT. Cada plataforma aporta el suyo (Keystore en
 * Android, Keychain en iOS) y se registra en el platformModule de Koin.
 * Es una interfaz y no un expect class porque las implementaciones se
 * construyen con argumentos distintos (Android necesita un Context).
 */
interface AlmacenSeguro {
    fun guardarToken(token: String)
    fun leerToken(): String?
    fun borrarToken()
}
