package com.do_f.a1valet.api

import androidx.lifecycle.LiveData
import com.do_f.a1valet.App
import com.do_f.a1valet.database.entity.Device

class DeviceRepo {

    private val dao by lazy {
        App.cacheDb.deviceDao()
    }

    fun getDevice(id: Int): LiveData<Device> = dao.getDevice(id)

    fun getDevices(): LiveData<List<Device>> {
        return dao.getDevices()
    }

    fun search(query: String): LiveData<List<Device>> {
        return dao.search(query)
    }

    fun filterByType(type: String): LiveData<List<Device>> {
        return dao.filterByType(type)
    }

    fun filterByFavorite(isFavorite: Boolean): LiveData<List<Device>> {
        return dao.filterByFavorite(isFavorite)
    }

    fun getDevicesSync(): List<Device> = dao.getDevicesSync()
    fun searchSync(query: String): List<Device> = dao.searchSync(query)
    fun filterByTypeSync(type: String): List<Device> = dao.filterByTypeSync(type)
    fun filterByFavoriteSync(isFavorite: Boolean): List<Device> = dao.filterByFavoriteSync(isFavorite)
}