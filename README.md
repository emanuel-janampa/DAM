# PharmaMobil

Aplicación Kotlin Multiplatform (Android + iOS) con Compose Multiplatform, Clean Architecture + MVVM y Koin.
Consume el backend **PharmaSoft** (Spring Boot + Oracle) mediante **Ktor Client**.

Práctica de laboratorio N.º 07 · Cliente Ktor y consumo GET — rama `feature/ktor-client`.

## Conexión con la API

| Dato | Valor |
|------|-------|
| URL base (emulador Android) | `http://10.0.2.2:8080/` |
| URL base (simulador iOS) | `http://localhost:8080/` |
| Celular físico | `http://<IP-de-tu-PC>:8080/` (cambiar en `PlatformModule.android.kt`) |
| Endpoints GET consumidos | `GET api/v1/productos?tamanio=50` y `GET api/v1/clientes?tamanio=50` |
| Formato de respuesta | Página: `{ contenido: [...], pagina, tamanio, totalElementos, totalPaginas, ultima }` |

> **HTTP solo en desarrollo.** El backend local no tiene TLS, por eso el manifest de Android declara
> `usesCleartextTraffic="true"` y el `Info.plist` de iOS permite `NSAllowsLocalNetworking`.
> En producción se debe usar HTTPS y quitar ambas excepciones.

La URL base se declara en cada plataforma (`shared/src/androidMain|iosMain/.../di/PlatformModule.*.kt`)
y llega a `crearHttpClient(engine, baseUrl)` por Koin (qualifier `BASE_URL`).

## Campos de los DTO

**`ProductoDto`** (`GET /api/v1/productos`): `id`, `nombre`, `precio`, `stock`, `estado`, `categoriaId`, `categoriaNombre`.
Se mapea a `Producto(id, nombre, precio, stock)` con `ProductoDto.toDomain()`.

**`ClienteDto`** (`GET /api/v1/clientes`): `id`, `dni`, `nombres`, `apellidos`, `email`, `telefono`, `direccion`, `estado`.
Se mapea a `Cliente(id, nombre, correo, telefono)` con `ClienteDto.toDomain()`.

**`PaginaDto<T>`**: envoltorio de todos los listados del backend (`contenido`, `pagina`, `tamanio`, `totalElementos`, `totalPaginas`, `ultima`).

Los campos desconocidos del JSON (`fechaCreacion`, `fechaModificacion`) se ignoran (`ignoreUnknownKeys = true`).

## Arquitectura de red

```
presentation (ViewModel/UiState)  →  domain (casos de uso, modelos, interfaces)
                                          ↑
data: repositorios Api → ProductoApi / ClienteApi (HttpClient) → DTO → mapper → dominio
```

- `data/remote/HttpClientFactory.kt`: cliente único con `ContentNegotiation`, `Logging`, `HttpTimeout`, `defaultRequest` y `expectSuccess`.
- `data/remote/ErrorDeRed.kt`: convierte fallos de red/HTTP/serialización en mensajes legibles (`ErrorDeRedException`).
- Motores: OkHttp (`androidMain`) y Darwin (`iosMain`).
- El paquete `domain` no importa Ktor ni `@Serializable`.

## Cómo ejecutar

1. Levantar Oracle (puerto 1522) y el backend PharmaSoft (`./mvnw spring-boot:run`, perfil `dev`, puerto 8080).
2. Probar en el navegador: `http://localhost:8080/api/v1/productos`.
3. Android: `./gradlew :androidApp:assembleDebug` o ejecutar desde Android Studio.
4. iOS: abrir `iosApp/` en Xcode y ejecutar en el simulador.
5. Log de Ktor: Logcat filtrando `System.out` / `Ktor>` (Android) o consola de Xcode (iOS).

## Pruebas

- Android: `./gradlew :shared:testAndroidHostTest`
- iOS: `./gradlew :shared:iosSimulatorArm64Test`

## Pendiente (siguientes sesiones)

- Formulario de clientes: el backend exige `dni` (8 dígitos) y `apellidos`, que la app aún no captura.
- Formulario de productos: se envía con categoría fija (`CATEGORIA_POR_DEFECTO = 1`) hasta agregar categorías al dominio.
- PUT/DELETE, autenticación JWT y persistencia offline.
