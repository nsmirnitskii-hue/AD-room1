package com.example.room1.ventanas

// Importaciones necesarias para Compose y funcionalidades de Android
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.room1.UserViewModel
import com.example.room1.data.InventoryViewModel
import com.example.room1.data.Item
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.widget.Toast

// Función composable que representa la ventana de borrado
@Composable
fun VentanaBorrar(
    navController: NavController,        // Controlador de navegación para cambiar entre pantallas
    modifier: Modifier = Modifier,       // Modificador para el diseño
    userViewModel: UserViewModel         // ViewModel para compartir datos entre pantallas
) {
    // Obtener instancia del ViewModel de inventario
    val inventoryViewModel: InventoryViewModel = viewModel()
    // Obtener el contexto de la aplicación para mostrar Toasts
    val context = LocalContext.current
    // Crear un scope de corrutinas para operaciones asíncronas
    val scope = rememberCoroutineScope()

    // Estado para almacenar el ID ingresado por el usuario
    var textId by remember { mutableStateOf("") }
    // Estado para mostrar mensajes al usuario
    var mensaje by remember { mutableStateOf("") }
    // Estado para el color del mensaje (rojo para errores, verde para éxitos)
    var mensajeColor by remember { mutableStateOf(Color.Red) }
    // Estado para almacenar el item encontrado
    var itemEncontrado by remember { mutableStateOf<Item?>(null) }

    // Columna principal que organiza los elementos verticalmente
    Column(
        modifier = modifier
            .fillMaxSize()       // Ocupar toda la pantalla
            .padding(16.dp),     // Agregar padding interno
        horizontalAlignment = Alignment.CenterHorizontally,  // Centrar elementos horizontalmente
        verticalArrangement = Arrangement.Top                // Alinear elementos en la parte superior
    ) {
        // Título de la ventana
        Text("Eliminar Item por ID")

        // Campo de texto para ingresar el ID
        OutlinedTextField(
            value = textId,                              // Valor actual del texto
            onValueChange = { textId = it },             // Actualizar valor cuando cambia
            label = { Text("ID del item a eliminar") },  // Etiqueta del campo
            modifier = Modifier.padding(vertical = 8.dp), // Espaciado vertical
            singleLine = true                            // Una sola línea de texto
        )

        // Botón para buscar el item antes de eliminarlo
        Button(
            onClick = {
                // Limpiar mensajes anteriores
                mensaje = ""
                itemEncontrado = null
                // Convertir el texto a número, devuelve null si no es válido
                val id = textId.trim().toIntOrNull()

                // Verificar si el ID es válido
                if (id != null) {
                    // Lanzar una corrutina para operación asíncrona
                    scope.launch {
                        try {
                            // Buscar el item por ID y observar el resultado
                            inventoryViewModel.getItem(id).collect { item ->
                                itemEncontrado = item
                                if (item != null) {
                                    // Item encontrado, mostrar información
                                    mensaje = "Item encontrado: ${item.name} - Precio: ${item.price} - Cantidad: ${item.quantity}"
                                    mensajeColor = Color.Green
                                } else {
                                    // Item no encontrado
                                    mensaje = "No se encontró ningún item con ID: $id"
                                    mensajeColor = Color.Red
                                }
                            }
                        } catch (e: Exception) {
                            // Manejar errores en la búsqueda
                            mensaje = "Error al buscar el item: ${e.message}"
                            mensajeColor = Color.Red
                        }
                    }
                } else {
                    // ID inválido
                    mensaje = "Por favor, ingrese un ID válido"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)  // Espaciado vertical
        ) {
            // Texto del botón
            Text("Buscar Item")
        }

        // Botón para eliminar el item encontrado
        Button(
            onClick = {
                // Convertir texto a número
                val id = textId.trim().toIntOrNull()
                // Verificar que tenemos un item válido para eliminar
                if (id != null && itemEncontrado != null) {
                    scope.launch {
                        try {
                            // Llamar al ViewModel para eliminar el item
                            inventoryViewModel.delete(itemEncontrado!!)
                            // Mensaje de éxito
                            mensaje = "Item eliminado correctamente"
                            mensajeColor = Color.Green
                            // Limpiar estados
                            itemEncontrado = null
                            textId = ""
                            // Mostrar Toast de confirmación
                            Toast.makeText(context, "Item eliminado", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            // Manejar errores en la eliminación
                            mensaje = "Error al eliminar el item: ${e.message}"
                            mensajeColor = Color.Red
                        }
                    }
                } else {
                    // No hay item para eliminar
                    mensaje = "Primero busque un item válido para eliminar"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),  // Espaciado vertical
            enabled = itemEncontrado != null               // Solo habilitado si hay item encontrado
        ) {
            Text("Eliminar Item")
        }

        // Mostrar mensajes al usuario
        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,                            // Texto del mensaje
                color = mensajeColor,                      // Color según el tipo de mensaje
                modifier = Modifier.padding(vertical = 8.dp) // Espaciado vertical
            )
        }

        // Mostrar información detallada del item encontrado
        itemEncontrado?.let { item ->
            // Título de la sección
            Text(
                text = "Item a eliminar:",
                modifier = Modifier.padding(top = 16.dp)  // Espaciado superior
            )
            // Detalles del item
            Text("ID: ${item.id}")
            Text("Nombre: ${item.name}")
            Text("Precio: ${item.price}")
            Text("Cantidad: ${item.quantity}")
        }
    }
}