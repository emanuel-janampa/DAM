# Cambios aplicados – Sesión 07 (Ktor + GET)

| # | Problema | Corrección |
|---|----------|------------|
| 1 | El backend devuelve una página y la app esperaba `List<ClienteDto>` | `PaginaDto<T>`; `ClienteApi` lee `.contenido` |
| 2 | URL `10.0.2.2` no sirve en iOS | `crearHttpClient(engine, baseUrl)`; URL por plataforma vía Koin (`BASE_URL`) |
| 3 | Android/iOS bloqueaban HTTP | `usesCleartextTraffic` y `NSAllowsLocalNetworking` (solo desarrollo) |
| 4 | Productos seguía en memoria | `ProductoDto`, `ProductoApi`, `ProductoRepositorioApi`, `ProductoMapper`, binding en Koin |
| 5 | Mensajes de error crudos | `ErrorDeRed.kt` (`mensajeLegible`, `llamadaRemota`) aplicado en los repositorios Api |
| 6 | Log de Ktor no visible | Logger propio con `println` |
| 7 | `AppModuleTest` no compilaba (faltaban imports) y afirmaba repos en memoria | Imports y aserciones corregidos |
| 8 | Sin pruebas del contrato JSON | `DtoMapeoTest` |
| 9 | README de plantilla | README con URL, endpoints y campos de DTO |

No se tocó: versión de Ktor (3.1.1; la guía indica 3.6.0, cambiar en `gradle/libs.versions.toml` si el docente lo exige) ni el POST de clientes.
