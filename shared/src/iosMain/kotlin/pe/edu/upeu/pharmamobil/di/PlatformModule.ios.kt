package pe.edu.upeu.pharmamobil.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.qualifier.named
import org.koin.dsl.module
<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguro
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguroIos
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
=======

>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }

    // El simulador de iOS comparte la red de la Mac: aqui si sirve localhost.
    single(named(BASE_URL)) { "http://localhost:8080/" }
<<<<<<< HEAD

    // Token en el Keychain: en iOS no recibe nada.
    single<AlmacenSeguro> { AlmacenSeguroIos() }

    single(named(ES_DEPURACION)) { Platform.isDebugBinary }
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
}
