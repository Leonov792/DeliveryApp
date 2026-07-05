package com.example.delivery.ui

import android.os.Bundle; import android.view.LayoutInflater; import android.view.ViewGroup
import android.widget.EditText; import android.widget.LinearLayout; import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity; import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager; import androidx.recyclerview.widget.RecyclerView
import com.example.delivery.databinding.ActivityMainBinding; import com.example.delivery.databinding.ItemOrderBinding
import com.example.delivery.model.Order; import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap; import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment; import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions; import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup; import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var b: ActivityMainBinding; private lateinit var vm: DeliveryViewModel
    private var map: GoogleMap? = null
    private var selectedLat = 55.7558; private var selectedLng = 37.6173

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b = ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        vm = ViewModelProvider(this)[DeliveryViewModel::class.java]
        b.rvOrders.layoutManager = LinearLayoutManager(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        vm.statuses.forEach { s ->
            val chip = Chip(this).apply { text = s; isCheckable = true; setOnClickListener { vm.setFilter(s) } }
            b.chipGroup.addView(chip)
        }
        (b.chipGroup.getChildAt(0) as Chip).isChecked = true

        vm.orders.observe(this) { all ->
            val f = vm.filter.value ?: "Все"
            val filtered = if (f == "Все") all else all.filter { it.status == f }
            b.rvOrders.adapter = Adapter(filtered)
        }
        vm.filter.observe(this) { b.tvFilter.text = it }

        b.fabAdd.setOnClickListener { showAddDialog() }
    }

    override fun onMapReady(gmap: GoogleMap) {
        map = gmap; map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.7558, 37.6173), 10f))
        map?.setOnMapClickListener { latLng -> map?.clear(); map?.addMarker(MarkerOptions().position(latLng).title("Доставка")); selectedLat = latLng.latitude; selectedLng = latLng.longitude }
        vm.orders.observe(this) { orders -> orders.forEach { o -> map?.addMarker(MarkerOptions().position(LatLng(o.lat, o.lng)).title("${o.status}: ${o.title}")) } }
    }

    private fun showAddDialog() {
        val title = EditText(this).apply { hint = "Что доставить?" }
        val addr = EditText(this).apply { hint = "Адрес" }
        val items = EditText(this).apply { hint = "Состав заказа" }
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; addView(title); addView(addr); addView(items) }
        MaterialAlertDialogBuilder(this).setTitle("Новый заказ").setView(layout)
            .setPositiveButton("Добавить") { _, _ ->
                val t = title.text.toString().trim(); val a = addr.text.toString().trim(); val itm = items.text.toString().trim()
                if (t.isNotEmpty()) { vm.add(t, a, itm, selectedLat, selectedLng); Toast.makeText(this, "Заказ создан", Toast.LENGTH_SHORT).show() }
            }.setNegativeButton("Отмена", null).show()
    }

    inner class Adapter(private val items: List<Order>) : RecyclerView.Adapter<Adapter.VH>() {
        inner class VH(val b: ItemOrderBinding) : RecyclerView.ViewHolder(b.root)
        override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemOrderBinding.inflate(LayoutInflater.from(p.context), p, false))
        override fun onBindViewHolder(h: VH, i: Int) {
            val o = items[i]
            h.b.tvTitle.text = o.title; h.b.tvAddress.text = o.address; h.b.tvItems.text = o.items; h.b.tvStatus.text = o.status
            h.b.tvStatus.setOnClickListener { vm.updateStatus(o) }
            h.b.btnDelete.setOnClickListener { vm.delete(o) }
            val color = when (o.status) { "Новый" -> "#FF9800"; "Готовится" -> "#2196F3"; "В пути" -> "#4CAF50"; "Доставлен" -> "#9E9E9E"; else -> "#FF9800" }
            h.b.tvStatus.setTextColor(android.graphics.Color.parseColor(color))
        }
        override fun getItemCount() = items.size
    }
}
