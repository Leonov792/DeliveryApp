package com.example.delivery.ui

import android.app.Application; import androidx.lifecycle.AndroidViewModel; import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData; import androidx.lifecycle.viewModelScope
import com.example.delivery.data.AppDatabase; import com.example.delivery.data.OrderRepository
import com.example.delivery.model.Order; import kotlinx.coroutines.launch

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = OrderRepository(AppDatabase.getDatabase(application).orderDao())
    val orders: LiveData<List<Order>> = repo.all
    private val _filter = MutableLiveData("Все")
    val filter: LiveData<String> = _filter

    fun add(title: String, address: String, items: String, lat: Double, lng: Double) =
        viewModelScope.launch { repo.insert(Order(title = title, address = address, items = items, lat = lat, lng = lng)) }

    fun updateStatus(order: Order) {
        val next = when (order.status) { "Новый" -> "Готовится"; "Готовится" -> "В пути"; "В пути" -> "Доставлен"; else -> "Новый" }
        viewModelScope.launch { repo.update(order.copy(status = next)) }
    }

    fun delete(order: Order) = viewModelScope.launch { repo.delete(order) }
    fun setFilter(status: String) { _filter.value = status }

    val statuses = listOf("Все", "Новый", "Готовится", "В пути", "Доставлен")
}
