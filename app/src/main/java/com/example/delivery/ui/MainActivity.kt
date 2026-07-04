package com.example.delivery.ui
import android.os.Bundle; import android.view.LayoutInflater; import android.view.ViewGroup; import android.widget.EditText; import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.databinding.ActivityMainBinding; import com.example.delivery.databinding.ItemOrderBinding
import com.example.delivery.model.Order; import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding; private lateinit var vm: DeliveryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        vm = ViewModelProvider(this)[DeliveryViewModel::class.java]; b.rvOrders.layoutManager = LinearLayoutManager(this)
        vm.orders.observe(this) { b.rvOrders.adapter = Adapter(it) }; b.fabAdd.setOnClickListener { showDialog() }
    }
    private fun showDialog() {
        val title = EditText(this).apply { hint = "Что доставить?" }; val addr = EditText(this).apply { hint = "Адрес" }
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; addView(title); addView(addr) }
        MaterialAlertDialogBuilder(this).setTitle("Новый заказ").setView(layout)
            .setPositiveButton("Добавить") { _, _ -> val t = title.text.toString().trim(); val a = addr.text.toString().trim(); if (t.isNotEmpty()) vm.add(t, a) }
            .setNegativeButton("Отмена", null).show()
    }
    inner class Adapter(private val items: List<Order>) : RecyclerView.Adapter<Adapter.VH>() {
        inner class VH(val b: ItemOrderBinding) : RecyclerView.ViewHolder(b.root)
        override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemOrderBinding.inflate(LayoutInflater.from(p.context), p, false))
        override fun onBindViewHolder(h: VH, i: Int) { val o = items[i]; h.b.tvTitle.text = o.title; h.b.tvAddress.text = o.address; h.b.tvStatus.text = o.status; h.b.tvStatus.setOnClickListener { vm.updateStatus(o.id, if (o.status == "Новый") "В пути" else if (o.status == "В пути") "Доставлен" else "Новый") } }
        override fun getItemCount() = items.size
    }
}
