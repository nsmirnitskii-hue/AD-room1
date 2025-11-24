package com.example.room1.ventanas


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.room1.MainActivity
import com.example.room1.MyApp
import com.example.room1.UserViewModel
import com.example.room1.data.InventoryDatabase
import com.example.room1.data.InventoryViewModel
import com.example.room1.data.Item
import com.example.room1.data.ItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun VentanaEditar(navController: NavController,modifier: Modifier, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        Text("Aqui vamos a meter datos")

        var textId  by remember { mutableStateOf("") }
        var textNombreProducto  by remember { mutableStateOf("") }
        var textPrecio by remember { mutableStateOf("") }
        var textcantidad by remember { mutableStateOf("") }

        OutlinedTextField(
            value = textId,
            onValueChange = { newText -> textId = newText },
            label = { Text("Id producto") },
        )
        OutlinedTextField(
            value = textNombreProducto,
            onValueChange = { newText -> textNombreProducto = newText },
            label = { Text("Nombre producto") },
        )

        OutlinedTextField(
            value = textPrecio,
            onValueChange = { newText -> textPrecio = newText },
            label = { Text("Precio") },
        )

        OutlinedTextField(
            value = textcantidad,
            onValueChange = { newText -> textcantidad = newText },
            label = { Text("Cantidad") },
        )
        val inventoryViewModel: InventoryViewModel = viewModel()
        Button({
            val textIdIntro = textId.trim().toInt()
            try {

            } catch (e: Exception) {
                val tipoError = e::class.simpleName   // solo el nombre de la clase de la excepción
                val mensaje = e.message               // mensaje de la excepción
                val msgError = "Error: $tipoError - $mensaje"
                Log.e(MainActivity.personalLogs+" - VentanaEditar", msgError, e)
            }


            val textNombreProductoIntro = textNombreProducto.trim()
            val dbprice = textPrecio.toDouble()
            val dbQuantity = textPrecio.toInt()
            scope.launch(Dispatchers.IO) {

            }
            inventoryViewModel.update(Item(id =textIdIntro, name = textNombreProductoIntro, price =dbprice, quantity = dbQuantity ))
            Toast.makeText(context, "Los datos se han modificado con exito", Toast.LENGTH_SHORT).show()
        }) {
            Text("Editar datos")
        }
    }
}