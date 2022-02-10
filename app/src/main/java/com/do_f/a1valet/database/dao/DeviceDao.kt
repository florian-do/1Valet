package com.do_f.a1valet.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.do_f.a1valet.database.entity.Device

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevice(device: Device)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevices(devices: List<Device>)

    @Query("SELECT * FROM device WHERE id=:id")
    fun getDevice(id: Int): LiveData<Device>

    @Query("SELECT * FROM device")
    fun getDevices(): LiveData<List<Device>>

    @Query("SELECT * FROM device")
    fun getDevicesPaged(): PagingSource<Int, Device>

    @Query("SELECT * FROM device WHERE type=:type")
    fun filterByType(type: String): LiveData<List<Device>>

    @Query("SELECT * FROM device WHERE isFavorite=:isFavorite")
    fun filterByFavorite(isFavorite: Boolean): LiveData<List<Device>>

    @Query("SELECT * FROM device WHERE title LIKE '%' || :query || '%'")
    fun search(query: String): LiveData<List<Device>>
}