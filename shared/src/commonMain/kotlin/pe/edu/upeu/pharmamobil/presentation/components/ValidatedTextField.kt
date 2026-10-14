package pe.edu.upeu.pharmamobil.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
<<<<<<< HEAD
import androidx.compose.ui.text.input.VisualTransformation
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import androidx.compose.foundation.text.KeyboardOptions


@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier.fillMaxWidth(),
    leadingIcon: ImageVector? = null,
    ayuda: String? = null,
<<<<<<< HEAD
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
=======
    keyboardType: KeyboardType = KeyboardType.Text
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
) {

    val apoyo = error ?: ayuda

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        isError = error != null,
        singleLine = true,
<<<<<<< HEAD
        visualTransformation = visualTransformation,
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        supportingText = apoyo?.let {
            {
                Text(it)
            }
        },
        modifier = modifier
    )
}
