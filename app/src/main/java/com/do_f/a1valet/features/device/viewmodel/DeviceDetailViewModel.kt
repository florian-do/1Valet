package com.do_f.a1valet.features.device.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.do_f.a1valet.api.DeviceRepo
import com.do_f.a1valet.database.entity.Device

class DeviceDetailViewModel: ViewModel() {

    private val repo by lazy {
        DeviceRepo()
    }

    fun getDevice(id: Int): LiveData<Device> = repo.getDevice(id)
}