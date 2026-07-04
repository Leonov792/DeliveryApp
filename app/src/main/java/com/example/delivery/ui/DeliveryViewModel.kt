package com.example.delivery.ui
import android.app.Application; import androidx.lifecycle.AndroidViewModel; import androidx.lifecycle.LiveData; import androidx.lifecycle.viewModelScope
import com.example.delivery.data.AppDatabase; import com.example.delivery.data.OrderRepository; import com.example.delivery.model.Order; import kotlinx.coroutines.launch
class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = OrderRepository(AppDatabase.getDatabase(application).orderDao())
    val orders: LiveData<List<Order>> = repo.all
    fun add(title: String, address: String) = viewModelScope.launch { repo.insert(Order(title = title, address = address)) }
    fun updateStatus(id: Long, status: String) = viewModelScope.launch { repo.all.value?.find { it.id == id }?.let { repo.update(it.copy(status = status)) } }
}
