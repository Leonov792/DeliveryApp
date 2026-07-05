package com.example.delivery.model
import androidx.room.Entity; import androidx.room.PrimaryKey
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String, val address: String, val items: String,
    val status: String = "Новый", val lat: Double = 55.7558, val lng: Double = 37.6173,
    val timestamp: Long = System.currentTimeMillis()
)
