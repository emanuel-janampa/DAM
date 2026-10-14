package pe.edu.upeu.pharmamobil.data.local

/** Almacen en memoria para pruebas: el real (Keystore/Keychain) no corre en el host. */
class FakeAlmacenSeguro(private var token: String? = null) : AlmacenSeguro {

    override fun guardarToken(token: String) {
        this.token = token
    }

    override fun leerToken(): String? = token

    override fun borrarToken() {
        token = null
    }
}
