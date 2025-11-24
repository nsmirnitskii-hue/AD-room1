package com.example.room1.ventanas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.room1.UserViewModel
import com.example.room1.data.InventoryViewModel
import com.example.room1.data.Item
import kotlinx.coroutines.launch

@Composable
fun VentanaBuscar(
    navController: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    val inventoryViewModel: InventoryViewModel = viewModel()

    // Estado para mostrar items
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var textId by remember { mutableStateOf("") }
    var textNombreProducto by remember { mutableStateOf("") }

    var msgError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Esta es la ventana de búsquedas")

        // TextFields para búsqueda
        OutlinedTextField(
            value = textId,
            onValueChange = { textId = it },
            label = { Text("Buscar por ID") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = textNombreProducto,
            onValueChange = { textNombreProducto = it },
            label = { Text("Buscar por nombre") },
            modifier = Modifier.padding(vertical = 8.dp),
            singleLine = true
        )

        // Botón Buscar
        Button(
            onClick = {
                msgError = ""
                val idText = textId.trim()
                val nameText = textNombreProducto.trim()

                when {
                    idText.isNotEmpty() -> {
                        val id = idText.toIntOrNull()
                        if (id != null) {
                            scope.launch {
                                try {
                                    inventoryViewModel.getItem(id).collect { result ->
                                        items = result?.let { listOf(it) } ?: emptyList() // Si no existe el item, devolvemos lista vacía
                                    }
                                } catch(e:Exception) {
                                    val tipoError = e::class.simpleName   // solo el nombre de la clase de la excepción
                                    val mensaje = e.message               // mensaje de la excepción
                                    msgError = "Error: $tipoError - $mensaje"

                                }
                            }
                        }else{
                            msgError = "El id tiene que ser un valor numerico"
                        }
                    }
                    nameText.isNotEmpty() -> {
                        scope.launch {
                            inventoryViewModel.getItemsByNameLike(nameText)
                                .collect { result ->
                                    items = result
                                }
                        }
                    }
                }
            }
        ) {
            Text("Buscar")
        }

        // Botón Ver Todos
        Button(
            onClick = {
                msgError = ""
                scope.launch {
                    inventoryViewModel.allItems.collect { result ->
                        items = result
                    }
                }
            }
        ) {
            Text("Ver Todos")
        }

        // Mostrar resultados
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(msgError, color = Color.Red)
            if (items.isEmpty()) {
                Text("No hay resultados")
            } else {
                items.forEach { item ->
                    Text("${item.id} - ${item.name} - ${item.price} - ${item.quantity}")
                }
            }
        }
    }
}
