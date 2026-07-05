package com.example.delivery.data
import android.content.Context; import androidx.lifecycle.LiveData; import androidx.room.*
import com.example.delivery.model.Order

@Dao interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC") fun getAll(): LiveData<List<Order>>
    @Insert suspend fun insert(o: Order)
    @Update suspend fun update(o: Order)
    @Delete suspend fun delete(o: Order)
    @Query("SELECT * FROM orders WHERE id = :id") suspend fun getById(id: Long): Order?
}

@Database(entities = [Order::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(c: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(c, AppDatabase::class.java, "delivery_db").build().also { INSTANCE = it }
        }
    }
}

class OrderRepository(private val dao: OrderDao) {
    val all: LiveData<List<Order>> = dao.getAll()
    suspend fun insert(o: Order) = dao.insert(o)
    suspend fun update(o: Order) = dao.update(o)
    suspend fun delete(o: Order) = dao.delete(o)
    suspend fun getById(id: Long) = dao.getById(id)
}
