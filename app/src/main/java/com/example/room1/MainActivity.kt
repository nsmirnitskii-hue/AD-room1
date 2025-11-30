package com.example.room1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.room1.ui.theme.Room1Theme
import com.example.room1.ventanas.Footer
import com.example.room1.ventanas.VentanaBorrar
import com.example.room1.ventanas.VentanaBuscar
import com.example.room1.ventanas.VentanaInicio
import com.example.room1.ventanas.VentanaIntro
import com.example.room1.ventanas.VentanaModificar

class MainActivity : ComponentActivity() {
    companion object {
        var personalLogs: String = "misLogs"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Room1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    gestorVentanas(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun gestorVentanas(modifier: Modifier) {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    Scaffold(
        bottomBar = { Footer(navController) } // footer común a todas las pantallas
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "inicio") {
            composable("inicio") { VentanaInicio(navController, modifier, userViewModel) }
            composable("ventanaIntro") { VentanaIntro(navController, modifier, userViewModel) }
            composable("ventanaBuscar") { VentanaBuscar(navController, modifier, userViewModel) }
            composable("borrar") { VentanaBorrar(navController, modifier, userViewModel) }
            composable("modificar") { VentanaModificar(navController, modifier, userViewModel) }
        }
    }


}

class UserViewModel : ViewModel() {
    var variableCompartir by mutableStateOf(0)
}