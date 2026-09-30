package pe.edu.upeu.pharmamobil.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }

    // 10.0.2.2 es el "localhost" de la PC visto desde el emulador Android.
    // En un celular fisico usa la IP de tu PC en la red (ej. http://192.168.1.20:8080/).
    single(named(BASE_URL)) { "http://10.0.2.2:8080/" }
}
