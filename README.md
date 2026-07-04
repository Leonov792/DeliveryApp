# 📦 DeliveryApp — Доставка на Android

Приложение для отслеживания заказов доставки со статусами.

## 📱 Screenshots
*(Скриншоты будут добавлены — запусти эмулятор в Android Studio)*

## 🛠 Tech Stack
- **Kotlin** + Coroutines
- **MVVM** Architecture
- **Room** Database (заказы)
- **LiveData** + **ViewBinding**
- **RecyclerView** с кастомными карточками
- **Material Design**

## 📁 Project Structure
```
app/src/main/java/com/example/delivery/
├── data/   — OrderDao, AppDatabase, OrderRepository
├── model/  — Order entity (id, title, address, status)
└── ui/     — MainActivity, DeliveryViewModel
```

## 📦 Download APK
[⬇️ v1.0 Release](https://github.com/Leonov792/DeliveryApp/releases/tag/v1.0)
