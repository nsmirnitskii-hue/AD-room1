package com.example.room1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
// Anotación @Database que define la configuración de la base de datos
@Database(entities = [Item::class], version = 1, exportSchema = false)
// Declaración de clase abstracta que extiende RoomDatabase
abstract class InventoryDatabase : RoomDatabase() {

    // Método abstracto que devuelve el DAO (Data Access Object)
    // Room se encargará de implementar este método automáticamente
    abstract fun itemDao(): ItemDao

    // Companion object para implementar el patrón Singleton
    companion object {
        // Variable volatile que garantiza la visibilidad inmediata entre hilos
        @Volatile
        // Instancia única de la base de datos (nullable)
        private var Instance: InventoryDatabase? = null

        // Método público para obtener la instancia de la base de datos
        fun getDatabase(context: Context): InventoryDatabase {
            // Línea comentada: podría usarse para borrar la base de datos durante desarrollo
            //context.deleteDatabase("item_database")

            // Operador Elvis: si Instance no es null, lo devuelve; si es null, ejecuta el bloque
            return Instance ?: synchronized(this) {
                // Bloque synchronized para evitar creación múltiple en hilos concurrentes
                // Construye la base de datos usando Room.databaseBuilder
                Room.databaseBuilder(context,  //Contexto de la aplicación
                    InventoryDatabase::class.java, // Clase de la base de datos
                    "item_database" //Nombre del archivo de base de datos
                )
                    .build() // Construye la instancia de la base de datos
                    .also { Instance = it }// Asigna la instancia creada a la variable Instance
            }
        }
    }
}