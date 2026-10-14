package pe.edu.upeu.pharmamobil.presentation.sesion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pe.edu.upeu.pharmamobil.presentation.components.MensajeError
import pe.edu.upeu.pharmamobil.presentation.components.ValidatedTextField

/** El composable no conoce el token: solo recibe el estado y emite eventos. */
@Composable
fun LoginScreen(
    uiState: SesionUiState,
    onUsuarioChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIniciar: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {

            Icon(
                imageVector = Icons.Default.LocalPharmacy,
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
                    .size(48.dp)
            )
        }

        Spacer16()

        Text(
            text = "PharmaMobil",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer16()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Aviso (p. ej. sesion expirada) o error del servidor.
            uiState.aviso?.let { MensajeError(it) }
            uiState.error?.let { MensajeError(it) }

            ValidatedTextField(
                value = uiState.usuario,
                onValueChange = onUsuarioChange,
                label = "Usuario",
                error = uiState.usuarioError,
                leadingIcon = Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )

            ValidatedTextField(
                value = uiState.clave,
                onValueChange = onClaveChange,
                label = "Contraseña",
                error = uiState.claveError,
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Se deshabilita mientras cargando es verdadero.
            Button(
                onClick = onIniciar,
                enabled = !uiState.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "Ingresando…",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}

@Composable
private fun Spacer16() {
    androidx.compose.foundation.layout.Spacer(Modifier.height(16.dp))
}
