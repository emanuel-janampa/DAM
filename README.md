# PharmaMobil

Aplicación Kotlin Multiplatform (Android + iOS) con Compose Multiplatform, Clean Architecture + MVVM y Koin.
Consume el backend **PharmaSoft** (Spring Boot + Oracle) mediante **Ktor Client**.

<<<<<<< HEAD
Práctica de laboratorio N.º 10 · Autenticación JWT y almacenamiento seguro (continúa la N.º 07: cliente Ktor).
=======
Práctica de laboratorio N.º 07 · Cliente Ktor y consumo GET — rama `feature/ktor-client`.
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190

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
<<<<<<< HEAD
- PUT/DELETE y persistencia offline.


## Autenticación

PharmaMobil inicia sesión contra `POST /api/v1/auth/login` y envía el JWT en cada petición como
`Authorization: Bearer <token>`. El token dura 3600 s y el backend no lo renueva: al expirar hay que repetir el login.

| Pieza | Dónde |
|-------|-------|
| Modelo de sesión (`Credenciales`, `Sesion.puede(permiso)`, `ErrorSesion`) | `domain/model` |
| `AuthRepository` + casos de uso (`IniciarSesion`, `SesionGuardada`, `CerrarSesion`) | `domain/repository`, `domain/usecase` |
| `AlmacenSeguro` (interfaz) | `data/local/AlmacenSeguro.kt` |
| Android: `EncryptedSharedPreferences` + Android Keystore | `androidMain/.../AlmacenSeguroAndroid.kt` |
| iOS: Keychain (`platform.Security`) | `iosMain/.../AlmacenSeguroIos.kt` |
| Plugins Ktor `Auth` (bearer), `Logging` (cabecera saneada, apagado fuera de depuración) | `data/remote/HttpClientFactory.kt` |
| `AuthRepositorioRest` (login, restaurar sesión, cerrar sesión) | `data/repository` |
| `GestorSesion`, `SesionViewModel`, `LoginScreen`, `PerfilScreen` | `presentation/sesion` |

- `AlmacenSeguro` es una **interfaz** (no `expect class`) porque Android necesita un `Context` y iOS no recibe nada;
  se registra en el `platformModule` de cada plataforma.
- `loadTokens` guarda en caché el token: tras guardarlo o borrarlo se llama a `authProvider<BearerAuthProvider>()?.clearToken()`.
- `sendWithoutRequest` devuelve `true` para todo salvo `/auth/login`: el token viaja en la primera petición.
- **401** → `ErrorSesion.SesionExpirada`: se borra el token, se vuelve al login con un aviso.
- **403** → `ErrorSesion.SinPermiso`: la sesión se mantiene y se muestra un mensaje.
- Al abrir la app se lee el token del almacén seguro: si el claim `exp` ya venció se descarta; si sigue vigente se recupera
  nombre y permisos con `GET /api/v1/auth/me` (sin red, se reconstruyen desde los claims del token).
- La interfaz oculta lo que el rol no puede usar (`CATALOGO_ESCRIBIR`, `CLIENTES_LEER`, `CLIENTES_ESCRIBIR`, `VENTAS_LEER`).
- La pantalla «Mi sesión» muestra los permisos y tiene una prueba de acceso a Clientes para provocar el 403 con el usuario `almacen`.
- `allowBackup="false"` en el manifest: la llave del Keystore no viaja en las copias de seguridad.

### Usuarios de prueba (cargados por Flyway)

| Usuario | Contraseña | Rol | Permisos |
|---------|------------|-----|----------|
| `admin` | `Admin123` | ADMIN | Los ocho permisos |
| `vendedor` | `Vendedor123` | VENDEDOR | Catálogo solo lectura; clientes y ventas completos |
| `almacen` | `Almacen123` | ALMACENERO | Catálogo: leer y escribir |
=======
- PUT/DELETE, autenticación JWT y persistencia offline.
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
