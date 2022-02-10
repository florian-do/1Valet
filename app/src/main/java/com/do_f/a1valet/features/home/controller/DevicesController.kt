package com.do_f.a1valet.features.home.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.databinding.RowDeviceCardBinding
import com.do_f.a1valet.deviceCard
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.extensions.setSafeOnClickListener

class DevicesController: TypedEpoxyController<List<IRow>>() {

    var onRowClickListener: ((Device) -> Unit)? = null

    override fun buildModels(data: List<IRow>?) {
        data?.forEach {
            when (it) {
                is IRow.DeviceRow -> buildDeviceRow(it.data)
            }
        }
    }

    private fun buildDeviceRow(device: Device) {
        deviceCard {
            id(device.id)
            device(device)
            onBind { _, view, _ ->
                val binding = view.dataBinding as RowDeviceCardBinding
                binding.root.setSafeOnClickListener {
                    this@DevicesController.onRowClickListener?.invoke(device)
                }
            }
        }
    }
}