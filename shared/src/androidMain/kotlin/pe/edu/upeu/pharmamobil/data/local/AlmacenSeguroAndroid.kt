package pe.edu.upeu.pharmamobil.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey
import java.security.GeneralSecurityException

/**
 * Token cifrado con una llave AES-256 custodiada por el Android Keystore.
 * Clave y valor quedan cifrados en el archivo "sesion_segura".
 */
class AlmacenSeguroAndroid(private val contexto: Context) : AlmacenSeguro {

    private val prefs: SharedPreferences = crearPrefs()

    private fun crearPrefs(): SharedPreferences = try {
        abrirPrefs()
    } catch (fallo: GeneralSecurityException) {
        // Llave del Keystore invalida (restauracion de dispositivo, cambio de
        // bloqueo de pantalla...): el token viejo ya no se puede leer. Se
        // descarta y se vuelve a empezar; el usuario solo debe iniciar sesion.
        contexto.deleteSharedPreferences(ARCHIVO)
        abrirPrefs()
    }

    private fun abrirPrefs(): SharedPreferences {
        val llave = MasterKey.Builder(contexto)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            contexto,
            ARCHIVO,
            llave,
            PrefKeyEncryptionScheme.AES256_SIV,
            PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun guardarToken(token: String) =
        prefs.edit().putString(CLAVE, token).apply()

    override fun leerToken(): String? = prefs.getString(CLAVE, null)

    override fun borrarToken() =
        prefs.edit().remove(CLAVE).apply()

    private companion object {
        const val ARCHIVO = "sesion_segura"
        const val CLAVE = "jwt"
    }
}
