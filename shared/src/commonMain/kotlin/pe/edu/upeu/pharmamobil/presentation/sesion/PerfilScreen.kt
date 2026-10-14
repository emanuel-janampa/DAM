package pe.edu.upeu.pharmamobil.presentation.sesion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.pharmamobil.domain.model.Sesion
import pe.edu.upeu.pharmamobil.presentation.components.MensajeError
import pe.edu.upeu.pharmamobil.presentation.components.MensajeExito

/** Datos de la sesion activa, prueba de permisos y "Cerrar sesión". */
@Composable
fun PerfilScreen(
    sesion: Sesion,
    viewModel: PerfilViewModel,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {

    val prueba by viewModel.prueba.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = sesion.nombreCompleto,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Usuario: ${sesion.usuario}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "Permisos",
                    style = MaterialTheme.typography.titleMedium
                )

                sesion.permisos.sorted().forEach { permiso ->
                    Text(
                        text = "• $permiso",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Prueba de acceso",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Pide la lista de clientes al servidor. Con el rol almacen " +
                        "responde 403: la sesión se mantiene y solo se avisa.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FilledTonalButton(
                    onClick = viewModel::probarAccesoAClientes,
                    enabled = !prueba.ejecutando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (prueba.ejecutando) "Consultando…" else "Probar acceso a Clientes")
                }

                prueba.mensaje?.let { mensaje ->
                    if (prueba.exito) MensajeExito(mensaje) else MensajeError(mensaje)
                }
            }
        }

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
