package com.example.delivery.model
import androidx.room.Entity; import androidx.room.PrimaryKey
@Entity(tableName = "orders") data class Order(@PrimaryKey(autoGenerate = true) val id: Long = 0, val title: String, val address: String, val status: String = "Новый", val timestamp: Long = System.currentTimeMillis())
