package com.example.room1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ItemDao {

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Especifique la estrategia de conflicto como IGNORE,  cuando el usuario intenta agregar un
    // elemento existente a la base de datos, la sala ignora el conflicto
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("SELECT * FROM items WHERE name LIKE '%' || :name || '%'")
    fun getItemsByNameLike(name: String): Flow<List<Item>>

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}