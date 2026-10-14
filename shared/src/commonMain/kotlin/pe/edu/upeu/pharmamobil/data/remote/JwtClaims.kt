package pe.edu.upeu.pharmamobil.data.remote

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
data class ClaimsJwt(
    val sub: String,
    val exp: Long,
    val authorities: List<String>
) {

    fun expiro(ahoraEnSegundos: Long): Boolean = ahoraEnSegundos >= exp

    val permisos: Set<String>
        get() = authorities.filterNot { it.startsWith("ROLE_") }.toSet()
}

@OptIn(ExperimentalEncodingApi::class)
internal fun leerClaimsJwt(token: String): ClaimsJwt? = runCatching {

    val partes = token.split(".")
    require(partes.size == 3)

    val carga = Base64.UrlSafe
        .withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)
        .decode(partes[1])
        .decodeToString()

    val json = Json.parseToJsonElement(carga).jsonObject

    ClaimsJwt(
        sub = json.getValue("sub").jsonPrimitive.content,
        exp = json.getValue("exp").jsonPrimitive.long,
        authorities = json["authorities"]
            ?.jsonArray
            ?.map { it.jsonPrimitive.content }
            ?: emptyList()
    )
}.getOrNull()
