package com.do_f.a1valet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.do_f.a1valet.database.dao.DeviceDao
import com.do_f.a1valet.database.entity.Device

@Database(
    entities = [
        Device::class
    ], version = 1, exportSchema = false
)
abstract class CacheDB: RoomDatabase() {
    companion object {
        const val name = "cache.db"
    }



    abstract fun deviceDao(): DeviceDao
}