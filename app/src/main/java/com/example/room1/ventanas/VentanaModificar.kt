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
    navController: NavController, modifier: Modifier = Modifier, userViewModel: UserViewModel)
{
    val inventoryViewModel: InventoryViewModel = viewModel() // Obtener instancia del ViewModel de inventario
    val context = LocalContext.current // Obtener contexto de la aplicación
    val scope = rememberCoroutineScope() // Scope para corrutinas

    var textId by remember { mutableStateOf("") }
    var textNombre by remember { mutableStateOf("") }
    var textPrecio by remember { mutableStateOf("") }
    var textCantidad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mensajeColor by remember { mutableStateOf(Color.Red) } // Estado para color del mensaje
    var itemEncontrado by remember { mutableStateOf<Item?>(null) } // Estado para almacenar el item encontrado

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {
        Text("Modificar Item por ID")

        OutlinedTextField(
            value = textId,
            onValueChange = { textId = it },
            label = { Text("ID del item a modificar") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true
        )

        Button(
            onClick = {
                mensaje = ""
                itemEncontrado = null
                val id = textId.trim().toIntOrNull()
                if (id != null) {
                    scope.launch {
                        try {
                            // Buscar el item por ID
                            inventoryViewModel.getItem(id).collect { item ->
                                itemEncontrado = item
                                if (item != null) {
                                    // Si se encuentra, llena los campos con los datos actuales
                                    textNombre = item.name
                                    textPrecio = item.price.toString()
                                    textCantidad = item.quantity.toString()
                                    mensaje = "Item encontrado. Complete los campos a modificar"
                                    mensajeColor = Color.Green
                                } else {
                                    mensaje = "No se encontró ningún item con ID: $id"
                                    mensajeColor = Color.Red

                                }
                            }
                        } catch (e: Exception) {
                            mensaje = "Error al buscar el item: ${e.message}"
                            mensajeColor = Color.Red

                        }
                    }
                } else {
                    mensaje = "Por favor, ingrese un ID válido"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Buscar Item")
        }

        // Campo para modificar el nombre
        OutlinedTextField(
            value = textNombre,
            onValueChange = { textNombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true,
            enabled = itemEncontrado != null           // Solo habilitado si hay item
        )

        OutlinedTextField(
            value = textPrecio,
            onValueChange = { textPrecio = it },
            label = { Text("Precio") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true,
            enabled = itemEncontrado != null
        )

        OutlinedTextField(
            value = textCantidad,
            onValueChange = { textCantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true,
            enabled = itemEncontrado != null
        )

        Button(
            onClick = {
                if (itemEncontrado != null) {
                    val precio = textPrecio.trim().toDoubleOrNull()
                    val cantidad = textCantidad.trim().toIntOrNull()
                    if (textNombre.isNotEmpty() && precio != null && cantidad != null) {
                        scope.launch {
                            try {
                                // Crear nuevo objeto Item con los datos actualizados
                                val itemActualizado = Item(
                                    id = itemEncontrado!!.id,
                                    name = textNombre,
                                    price = precio,
                                    quantity = cantidad
                                )
                                // Llamar al ViewModel para actualizar
                                inventoryViewModel.update(itemActualizado)
                                mensaje = "Item actualizado correctamente"
                                mensajeColor = Color.Green
                                Toast.makeText(context, "Item modificado", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                mensaje = "Error al actualizar el item: ${e.message}"
                                mensajeColor = Color.Red
                            }
                        }
                    } else {
                        mensaje = "Por favor, complete todos los campos con valores válidos"
                        mensajeColor = Color.Red
                    }
                } else {
                    mensaje = "Primero busque un item para modificar"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = itemEncontrado != null
        ) {
            Text("Guardar Cambios")
        }

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = mensajeColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


