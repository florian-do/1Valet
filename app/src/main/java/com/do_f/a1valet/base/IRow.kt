package com.do_f.a1valet.base

import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.model.DeviceFilter

interface IRow {
    class DeviceRow(val data: Device): IRow
    class DeviceFilterRow(val deviceFilter: DeviceFilter): IRow
}