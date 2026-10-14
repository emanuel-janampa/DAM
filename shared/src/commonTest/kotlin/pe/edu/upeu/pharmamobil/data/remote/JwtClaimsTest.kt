package pe.edu.upeu.pharmamobil.data.remote

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalEncodingApi::class)
class JwtClaimsTest {

    private fun tokenCon(cargaJson: String): String {
        val cargaB64 = Base64.UrlSafe
            .withPadding(Base64.PaddingOption.ABSENT)
            .encode(cargaJson.encodeToByteArray())
        return "cabecera.$cargaB64.firma"
    }

    @Test
    fun leeSubExpYPermisosSinLosRoles() {

        val token = tokenCon(
            """{"sub":"almacen","authorities":["ROLE_ALMACENERO","CATALOGO_ESCRIBIR","CATALOGO_LEER"],"iat":1000,"exp":4600}"""
        )

        val claims = leerClaimsJwt(token)!!

        assertEquals("almacen", claims.sub)
        assertEquals(4600L, claims.exp)
        assertEquals(setOf("CATALOGO_ESCRIBIR", "CATALOGO_LEER"), claims.permisos)
    }

    @Test
    fun expiraAlLlegarAlClaimExp() {

        val claims = leerClaimsJwt(tokenCon("""{"sub":"admin","exp":5000}"""))!!

        assertFalse(claims.expiro(4999))
        assertTrue(claims.expiro(5000))
        assertTrue(claims.expiro(9999))
    }

    @Test
    fun unTokenMalFormadoDevuelveNull() {

        assertNull(leerClaimsJwt("no-es-un-jwt"))
        assertNull(leerClaimsJwt("a.%%%.c"))
        assertNull(leerClaimsJwt(tokenCon("""{"sub":"admin"}""")))
    }
}
