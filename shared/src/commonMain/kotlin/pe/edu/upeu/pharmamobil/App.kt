package pe.edu.upeu.pharmamobil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
<<<<<<< HEAD
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
<<<<<<< HEAD
import androidx.compose.material3.CircularProgressIndicator
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
<<<<<<< HEAD
import androidx.lifecycle.compose.collectAsStateWithLifecycle
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.domain.model.Permisos
import pe.edu.upeu.pharmamobil.domain.model.Sesion
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import pe.edu.upeu.pharmamobil.navigation.Screen
import pe.edu.upeu.pharmamobil.presentation.cliente.ClienteScreen
import pe.edu.upeu.pharmamobil.presentation.components.EstadoVacio
import pe.edu.upeu.pharmamobil.presentation.inicio.InicioScreen
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoScreen
<<<<<<< HEAD
import pe.edu.upeu.pharmamobil.presentation.sesion.LoginScreen
import pe.edu.upeu.pharmamobil.presentation.sesion.PerfilScreen
import pe.edu.upeu.pharmamobil.presentation.sesion.SesionViewModel
=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
import pe.edu.upeu.pharmamobil.theme.PharmaMobilTheme

/** Una sola fuente para el menu lateral y el titulo de la barra superior. */
private data class Destino(
    val screen: Screen,
    val titulo: String,
<<<<<<< HEAD
    val icono: ImageVector,
    /** Permiso que exige el servidor; el destino se oculta si la sesion no lo tiene. */
    val permiso: String? = null
=======
    val icono: ImageVector
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
)

private val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
<<<<<<< HEAD
    Destino(Screen.Productos, "Productos", Icons.Default.Medication, Permisos.CATALOGO_LEER),
    Destino(Screen.Clientes, "Clientes", Icons.Default.Person, Permisos.CLIENTES_LEER),
    Destino(Screen.Pedidos, "Pedidos", Icons.Default.ShoppingCart, Permisos.VENTAS_LEER),
    Destino(Screen.Perfil, "Mi sesión", Icons.Default.AccountCircle)
=======
    Destino(Screen.Productos, "Productos", Icons.Default.Medication),
    Destino(Screen.Clientes, "Clientes", Icons.Default.Person),
    Destino(Screen.Pedidos, "Pedidos", Icons.Default.ShoppingCart)
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
)


private val ScreenSaver = Saver<Screen, Int>(
    save = { pantalla ->
        DESTINOS.indexOfFirst { it.screen == pantalla }
    },
    restore = { indice ->
        DESTINOS[indice].screen
    }
)

@Composable
fun App() = KoinContext {

<<<<<<< HEAD
    val sesionViewModel: SesionViewModel = koinViewModel()
    val estado by sesionViewModel.uiState.collectAsStateWithLifecycle()

    var darkTheme by rememberSaveable {
        mutableStateOf(false)
    }

    PharmaMobilTheme(
        darkTheme = darkTheme
    ) {

        val sesion = estado.sesion

        when {

            // Buscando un token guardado en el almacen seguro.
            estado.restaurando ->
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            sesion == null ->
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen(
                        uiState = estado,
                        onUsuarioChange = sesionViewModel::onUsuarioChange,
                        onClaveChange = sesionViewModel::onClaveChange,
                        onIniciar = sesionViewModel::iniciar
                    )
                }

            else ->
                ContenidoAutenticado(
                    sesion = sesion,
                    darkTheme = darkTheme,
                    onCambiarTema = { darkTheme = it },
                    onCerrarSesion = sesionViewModel::cerrarSesion
                )
        }
    }
}

@Composable
private fun ContenidoAutenticado(
    sesion: Sesion,
    darkTheme: Boolean,
    onCambiarTema: (Boolean) -> Unit,
    onCerrarSesion: () -> Unit
) {

    // Solo se ofrece lo que el rol puede usar: lo demas el servidor lo negaria con 403.
    val destinos = DESTINOS.filter { it.permiso == null || sesion.puede(it.permiso) }

=======
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) {
        mutableStateOf<Screen>(Screen.Inicio)
    }

<<<<<<< HEAD
    if (destinos.none { it.screen == pantallaActual }) {
        pantallaActual = Screen.Inicio
    }

    // Cada inicio de sesion usa ViewModels nuevos: asi no se ven datos del usuario anterior.
    val claveDeSesion = sesion.token.hashCode().toString()

=======
    var darkTheme by rememberSaveable {
        mutableStateOf(false)
    }

>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

<<<<<<< HEAD
    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                DrawerHeader(sesion)

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                destinos.forEach { destino ->

                    NavigationDrawerItem(
                        label = {
                            Text(destino.titulo)
                        },
                        selected = pantallaActual == destino.screen,
                        onClick = {

                            pantallaActual = destino.screen

                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destino.icono,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(
                            NavigationDrawerItemDefaults.ItemPadding
=======
    PharmaMobilTheme(
        darkTheme = darkTheme
    ) {

        ModalNavigationDrawer(

            drawerState = drawerState,

            drawerContent = {

                ModalDrawerSheet {

                    DrawerHeader()

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    DESTINOS.forEach { destino ->

                        NavigationDrawerItem(
                            label = {
                                Text(destino.titulo)
                            },
                            selected = pantallaActual == destino.screen,
                            onClick = {

                                pantallaActual = destino.screen

                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destino.icono,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.padding(
                                NavigationDrawerItemDefaults.ItemPadding
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    HorizontalDivider()

                    ModoOscuro(
                        activo = darkTheme,
                        onCambiar = { darkTheme = it }
                    )
                }
            }
        ) {

            Scaffold(

                topBar = {

                    TopAppBar(

                        title = {
                            Text(
                                text = tituloDe(pantallaActual)
                            )
                        },

                        navigationIcon = {

                            IconButton(
                                onClick = {

                                    scope.launch {

                                        drawerState.open()
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Abrir menú"
                                )
                            }
                        },

                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                        )
                    )
                }

<<<<<<< HEAD
                Spacer(
                    modifier = Modifier.weight(1f)
                )

                HorizontalDivider()

                ModoOscuro(
                    activo = darkTheme,
                    onCambiar = onCambiarTema
                )

                NavigationDrawerItem(
                    label = {
                        Text("Cerrar sesión")
                    },
                    selected = false,
                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        onCerrarSesion()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.padding(
                        NavigationDrawerItemDefaults.ItemPadding
                    )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }
    ) {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text(
                            text = tituloDe(pantallaActual)
                        )
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {

                                scope.launch {

                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                when (pantallaActual) {

                    Screen.Inicio ->
                        InicioScreen(
                            sesion = sesion,
                            onNavegar = { destino ->
                                pantallaActual = destino
                            }
                        )

                    Screen.Productos ->
                        ProductoScreen(
                            viewModel = koinViewModel(key = "productos-$claveDeSesion"),
                            // Oculta "Registrar producto" a quien no tiene CATALOGO_ESCRIBIR.
                            puedeRegistrar = sesion.puede(Permisos.CATALOGO_ESCRIBIR)
                        )

                    Screen.Clientes ->
                        ClienteScreen(
                            viewModel = koinViewModel(key = "clientes-$claveDeSesion"),
                            puedeRegistrar = sesion.puede(Permisos.CLIENTES_ESCRIBIR)
                        )

                    Screen.Pedidos ->
                        EstadoVacio(
                            icono = Icons.Default.ShoppingCart,
                            titulo = "Pedidos en construcción",
                            descripcion = "Este módulo llega en una próxima sesión del curso.",
                            modifier = Modifier.align(Alignment.Center)
                        )

                    Screen.Perfil ->
                        PerfilScreen(
                            sesion = sesion,
                            viewModel = koinViewModel(key = "perfil-$claveDeSesion"),
                            onCerrarSesion = onCerrarSesion
                        )
=======
            ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    when (pantallaActual) {

                        Screen.Inicio ->
                            InicioScreen(
                                onNavegar = { destino ->
                                    pantallaActual = destino
                                }
                            )

                        Screen.Productos ->
                            ProductoScreen(
                                viewModel = koinViewModel()
                            )

                        Screen.Clientes ->
                            ClienteScreen(
                                viewModel = koinViewModel()
                            )

                        Screen.Pedidos ->
                            EstadoVacio(
                                icono = Icons.Default.ShoppingCart,
                                titulo = "Pedidos en construcción",
                                descripcion = "Este módulo llega en una próxima sesión del curso.",
                                modifier = Modifier.align(Alignment.Center)
                            )
                    }
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                }
            }
        }
    }
}


@Composable
<<<<<<< HEAD
private fun DrawerHeader(sesion: Sesion) {
=======
private fun DrawerHeader() {
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                    .padding(10.dp)
                    .size(28.dp)
            )
        }

        Column {

            Text(
                text = "PharmaMobil",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
<<<<<<< HEAD
                text = sesion.nombreCompleto,
=======
                text = "Gestión farmacéutica",
>>>>>>> 5e0efee5a01d7e38fc7329cd95e859bdc6de4190
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun ModoOscuro(
    activo: Boolean,
    onCambiar: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 28.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Modo oscuro",
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = activo,
            onCheckedChange = onCambiar
        )
    }
}


private fun tituloDe(
    screen: Screen
): String {

    return DESTINOS.first { it.screen == screen }.titulo
}
