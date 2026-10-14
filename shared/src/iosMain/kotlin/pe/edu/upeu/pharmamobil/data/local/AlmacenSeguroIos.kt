@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package pe.edu.upeu.pharmamobil.data.local

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFMutableDictionaryRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.posix.memcpy

/**
 * Token guardado en el Keychain del sistema (cifrado por iOS, fuera del
 * sandbox legible de la app). Un item "generic password" identificado por
 * servicio + cuenta.
 */
class AlmacenSeguroIos : AlmacenSeguro {

    override fun guardarToken(token: String) {

        borrarToken()

        if (token.isEmpty()) return

        val datos = CFBridgingRetain(token.encodeToByteArray().toNSData())
        val consulta = consultaBase()

        CFDictionaryAddValue(consulta, kSecValueData, datos)
        CFDictionaryAddValue(
            consulta,
            kSecAttrAccessible,
            kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
        )

        SecItemAdd(consulta, null)

        CFRelease(datos)
        CFRelease(consulta)
    }

    override fun leerToken(): String? = memScoped {

        val consulta = consultaBase()

        CFDictionaryAddValue(consulta, kSecReturnData, kCFBooleanTrue)
        CFDictionaryAddValue(consulta, kSecMatchLimit, kSecMatchLimitOne)

        val resultado = alloc<CFTypeRefVar>()
        val estado = SecItemCopyMatching(consulta, resultado.ptr)

        CFRelease(consulta)

        if (estado != errSecSuccess) {
            return@memScoped null
        }

        val datos = CFBridgingRelease(resultado.value) as? NSData
            ?: return@memScoped null

        datos.aByteArray().decodeToString()
    }

    override fun borrarToken() {
        val consulta = consultaBase()
        SecItemDelete(consulta)
        CFRelease(consulta)
    }

    /** Diccionario que identifica el item; el diccionario retiene sus valores. */
    private fun consultaBase(): CFMutableDictionaryRef? {

        val consulta = CFDictionaryCreateMutable(
            null,
            0,
            kCFTypeDictionaryKeyCallBacks.ptr,
            kCFTypeDictionaryValueCallBacks.ptr
        )

        val servicio = CFBridgingRetain(SERVICIO)
        val cuenta = CFBridgingRetain(CUENTA)

        CFDictionaryAddValue(consulta, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(consulta, kSecAttrService, servicio)
        CFDictionaryAddValue(consulta, kSecAttrAccount, cuenta)

        CFRelease(servicio)
        CFRelease(cuenta)

        return consulta
    }

    private fun ByteArray.toNSData(): NSData = usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }

    private fun NSData.aByteArray(): ByteArray {

        val tamano = length.toInt()

        if (tamano == 0) return ByteArray(0)

        val bytes = ByteArray(tamano)

        bytes.usePinned {
            memcpy(it.addressOf(0), this.bytes, this.length)
        }

        return bytes
    }

    private companion object {
        const val SERVICIO = "pe.edu.upeu.pharmamobil"
        const val CUENTA = "jwt"
    }
}
