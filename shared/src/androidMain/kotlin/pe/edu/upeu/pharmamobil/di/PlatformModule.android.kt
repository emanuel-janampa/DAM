package pe.edu.upeu.pharmamobil.di

import android.content.pm.ApplicationInfo
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguro
import pe.edu.upeu.pharmamobil.data.local.AlmacenSeguroAndroid

actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }

    // 10.0.2.2 es el "localhost" de la PC visto desde el emulador Android.
    // En un celular fisico usa la IP de tu PC en la red (ej. http://192.168.1.20:8080/).
    single(named(BASE_URL)) { "http://10.0.2.2:8080/" }

    // Token cifrado con el Keystore: en Android necesita el Context.
    single<AlmacenSeguro> { AlmacenSeguroAndroid(androidContext()) }

    // El modulo shared no tiene BuildConfig; se lee la marca "debuggable" del APK.
    single(named(ES_DEPURACION)) {
        (androidContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}
