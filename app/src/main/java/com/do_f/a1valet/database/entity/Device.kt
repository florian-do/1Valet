package com.do_f.a1valet.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.do_f.a1valet.R

@Entity
data class Device(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val price: Int,
    val currency: String,
    val isFavorite: Boolean,
    val imageUrl: String = "",
    val coverUrl: String = "",
    val title: String = "",
    val description: String = ""
) {
    fun getDrawableFromType(): Int = when (type) {
        "iphone" -> R.drawable.ic_phone_iphone
        "thermostat" -> R.drawable.ic_thermostat
        "android" -> R.drawable.ic_phone_android
        "sensor" -> R.drawable.ic_thermostat
        "smartlock" -> R.drawable.ic_smartlock
        else -> R.drawable.ic_sensor_door
    }
}