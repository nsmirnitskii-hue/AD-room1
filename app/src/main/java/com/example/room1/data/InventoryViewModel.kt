package com.example.room1.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val itemDao = InventoryDatabase.getDatabase(application).itemDao()

    fun insertItem(name: String, price: Double, quantity: Int) {
        val newItem = Item(name = name, price = price, quantity = quantity)
        Log.d("InventoryViewModel", "Nuevo Item creado: $newItem")

        viewModelScope.launch(Dispatchers.IO) {
            itemDao.insert(newItem)
            Log.d("InventoryViewModel", "Insert realizado")
        }
    }

    fun getItem(id: Int): Flow<Item?> {
        return itemDao.getItem(id)
    }

    fun getItemsByNameLike(name: String): Flow<List<Item>> {
        return itemDao.getItemsByNameLike(name)
    }

    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    fun delete(item: Item)
    ItemDao.delete(item)

    fun update(item: Item)
    ItemDao.update(item)

}