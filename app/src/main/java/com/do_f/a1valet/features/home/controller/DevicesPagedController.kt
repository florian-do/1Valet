package com.do_f.a1valet.features.home.controller

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagedListEpoxyController
import com.do_f.a1valet.DeviceCardBindingModel_
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.databinding.RowDeviceCardBinding
import com.do_f.a1valet.extensions.setSafeOnClickListener

class DevicesPagedController: PagedListEpoxyController<IRow>() {

    var onRowClickListener: ((Device) -> Unit)? = null

    override fun buildItemModel(currentPosition: Int, item: IRow?): EpoxyModel<*> {
        return when (item) {
            is IRow.DeviceRow -> buildDeviceRow(item.data)
            else -> DeviceCardBindingModel_()
        }
    }

    private fun buildDeviceRow(device: Device): EpoxyModel<*> {
        return DeviceCardBindingModel_().apply {
            id(device.id)
            device(device)
            onBind { _, view, _ ->
                val binding = view.dataBinding as RowDeviceCardBinding
                binding.root.setSafeOnClickListener {
                    this@DevicesPagedController.onRowClickListener?.invoke(device)
                }
            }
        }
    }
}