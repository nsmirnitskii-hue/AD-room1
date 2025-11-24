package com.example.room1.ventanas


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.room1.UserViewModel

@Composable
fun VentanaInicio(navController: NavController,modifier: Modifier, userViewModel: UserViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text("Soy la primera ventana")
        Button({ navController.navigate("ventanaIntro") }) { Text("Ir a intro") }
        Button({ navController.navigate("ventanaBuscar") }) { Text("Ir a Buscar") }
    }
}