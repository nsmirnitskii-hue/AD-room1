package com.example.room1.ventanas

// Importaciones necesarias
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

// Función composable para la ventana de modificación
@Composable
fun VentanaModificar(
    navController: NavController,        // Controlador de navegación
    modifier: Modifier = Modifier,       // Modificador para diseño
    userViewModel: UserViewModel         // ViewModel compartido
) {
    // Obtener instancia del ViewModel de inventario
    val inventoryViewModel: InventoryViewModel = viewModel()
    // Obtener contexto de la aplicación
    val context = LocalContext.current
    // Scope para corrutinas
    val scope = rememberCoroutineScope()

    // Estados para los campos de texto
    var textId by remember { mutableStateOf("") }           // ID del item a buscar
    var textNombre by remember { mutableStateOf("") }       // Nuevo nombre
    var textPrecio by remember { mutableStateOf("") }       // Nuevo precio
    var textCantidad by remember { mutableStateOf("") }     // Nueva cantidad
    // Estado para mensajes
    var mensaje by remember { mutableStateOf("") }
    // Estado para color del mensaje
    var mensajeColor by remember { mutableStateOf(Color.Red) }
    // Estado para almacenar el item encontrado
    var itemEncontrado by remember { mutableStateOf<Item?>(null) }

    // Columna principal
    Column(
        modifier = modifier
            .fillMaxSize()       // Ocupar toda la pantalla
            .padding(16.dp),     // Padding interno
        horizontalAlignment = Alignment.CenterHorizontally,  // Centrado horizontal
        verticalArrangement = Arrangement.Top                // Alineación superior
    ) {
        // Título de la ventana
        Text("Modificar Item por ID")

        // Campo para ingresar el ID del item a modificar
        OutlinedTextField(
            value = textId,                              // Valor actual
            onValueChange = { textId = it },             // Actualizar al cambiar
            label = { Text("ID del item a modificar") }, // Etiqueta
            modifier = Modifier.padding(vertical = 8.dp), // Espaciado
            singleLine = true                            // Una línea
        )

        // Botón para buscar el item
        Button(
            onClick = {
                // Limpiar estados anteriores
                mensaje = ""
                itemEncontrado = null
                // Convertir ID a número
                val id = textId.trim().toIntOrNull()

                // Verificar si el ID es válido
                if (id != null) {
                    scope.launch {
                        try {
                            // Buscar el item por ID
                            inventoryViewModel.getItem(id).collect { item ->
                                itemEncontrado = item
                                if (item != null) {
                                    // Si se encuentra, llenar los campos con los datos actuales
                                    textNombre = item.name                    // Establecer nombre actual
                                    textPrecio = item.price.toString()        // Establecer precio actual
                                    textCantidad = item.quantity.toString()   // Establecer cantidad actual
                                    mensaje = "Item encontrado. Complete los campos a modificar"
                                    mensajeColor = Color.Green
                                } else {
                                    // Item no encontrado
                                    mensaje = "No se encontró ningún item con ID: $id"
                                    mensajeColor = Color.Red

                                }
                            }
                        } catch (e: Exception) {
                            // Manejar errores
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
            modifier = Modifier.padding(vertical = 8.dp)  // Espaciado
        ) {
            Text("Buscar Item")
        }

        // Campo para modificar el nombre
        OutlinedTextField(
            value = textNombre,                        // Valor actual
            onValueChange = { textNombre = it },       // Actualizar al cambiar
            label = { Text("Nombre") },                // Etiqueta
            modifier = Modifier.padding(vertical = 8.dp), // Espaciado
            singleLine = true,                         // Una línea
            enabled = itemEncontrado != null           // Solo habilitado si hay item
        )

        // Campo para modificar el precio
        OutlinedTextField(
            value = textPrecio,                        // Valor actual
            onValueChange = { textPrecio = it },       // Actualizar al cambiar
            label = { Text("Precio") },                // Etiqueta
            modifier = Modifier.padding(vertical = 8.dp), // Espaciado
            singleLine = true,                         // Una línea
            enabled = itemEncontrado != null           // Solo habilitado si hay item
        )

        // Campo para modificar la cantidad
        OutlinedTextField(
            value = textCantidad,                      // Valor actual
            onValueChange = { textCantidad = it },     // Actualizar al cambiar
            label = { Text("Cantidad") },              // Etiqueta
            modifier = Modifier.padding(vertical = 8.dp), // Espaciado
            singleLine = true,                         // Una línea
            enabled = itemEncontrado != null           // Solo habilitado si hay item
        )

        // Botón para guardar los cambios
        Button(
            onClick = {
                // Verificar que hay un item encontrado
                if (itemEncontrado != null) {
                    // Convertir valores a los tipos correctos
                    val precio = textPrecio.trim().toDoubleOrNull()    // Convertir a Double
                    val cantidad = textCantidad.trim().toIntOrNull()   // Convertir a Int

                    // Validar que todos los campos estén completos y sean válidos
                    if (textNombre.isNotEmpty() && precio != null && cantidad != null) {
                        scope.launch {
                            try {
                                // Crear nuevo objeto Item con los datos actualizados
                                val itemActualizado = Item(
                                    id = itemEncontrado!!.id,      // Mantener el mismo ID
                                    name = textNombre,             // Nuevo nombre
                                    price = precio,                // Nuevo precio
                                    quantity = cantidad            // Nueva cantidad
                                )
                                // Llamar al ViewModel para actualizar
                                inventoryViewModel.update(itemActualizado)
                                mensaje = "Item actualizado correctamente"
                                mensajeColor = Color.Green
                                // Mostrar Toast de confirmación
                                Toast.makeText(context, "Item modificado", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                // Manejar errores en la actualización
                                mensaje = "Error al actualizar el item: ${e.message}"
                                mensajeColor = Color.Red
                            }
                        }
                    } else {
                        // Campos inválidos
                        mensaje = "Por favor, complete todos los campos con valores válidos"
                        mensajeColor = Color.Red
                    }
                } else {
                    // No hay item para modificar
                    mensaje = "Primero busque un item para modificar"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),  // Espaciado
            enabled = itemEncontrado != null               // Solo habilitado si hay item
        ) {
            Text("Guardar Cambios")
        }

        // Mostrar mensajes al usuario
        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,                            // Texto del mensaje
                color = mensajeColor,                      // Color del mensaje
                modifier = Modifier.padding(vertical = 8.dp) // Espaciado
            )
        }
    }
}

// Función auxiliar para limpiar todos los campos
