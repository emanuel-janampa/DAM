package pe.edu.upeu.pharmamobil.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }

    // El simulador de iOS comparte la red de la Mac: aqui si sirve localhost.
    single(named(BASE_URL)) { "http://localhost:8080/" }
}
