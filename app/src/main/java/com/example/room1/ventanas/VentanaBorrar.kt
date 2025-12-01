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

@Composable
fun VentanaBorrar(
    navController: NavController, modifier: Modifier = Modifier, userViewModel: UserViewModel)
{
    val inventoryViewModel: InventoryViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var textId by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mensajeColor by remember { mutableStateOf(Color.Red) }
    var itemEncontrado by remember { mutableStateOf<Item?>(null) } // Estado para almacenar el item encontrado

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Eliminar Item por ID")

        OutlinedTextField(
            value = textId,
            onValueChange = { textId = it },
            label = { Text("ID del item a eliminar") },
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
                            // Buscar el item por ID y observar el resultado
                            inventoryViewModel.getItem(id).collect { item ->
                                itemEncontrado = item
                                if (item != null) {
                                    mensaje = "Item encontrado: ${item.name} - Precio: ${item.price} - Cantidad: ${item.quantity}"
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

        Button(
            onClick = {
                val id = textId.trim().toIntOrNull()
                if (id != null && itemEncontrado != null) {
                    scope.launch {
                        try {
                            // Llamar al ViewModel para eliminar el item
                            inventoryViewModel.delete(itemEncontrado!!)
                            mensaje = "Item eliminado correctamente"
                            mensajeColor = Color.Green
                            itemEncontrado = null
                            textId = ""
                            Toast.makeText(context, "Item eliminado", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            mensaje = "Error al eliminar el item: ${e.message}"
                            mensajeColor = Color.Red
                        }
                    }
                } else {
                    mensaje = "Primero busque un item válido para eliminar"
                    mensajeColor = Color.Red
                }
            },
            modifier = Modifier.padding(vertical = 8.dp),
            enabled = itemEncontrado != null
        ) {
            Text("Eliminar Item")
        }

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = mensajeColor,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Mostrar información detallada del item encontrado
        itemEncontrado?.let { item ->
            Text(
                text = "Item a eliminar:",
                modifier = Modifier.padding(top = 16.dp)
            )
            // Detalles del item
            Text("ID: ${item.id}")
            Text("Nombre: ${item.name}")
            Text("Precio: ${item.price}")
            Text("Cantidad: ${item.quantity}")
        }
    }
}