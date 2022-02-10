package com.do_f.a1valet.datasource

import androidx.paging.PageKeyedDataSource
import com.do_f.a1valet.api.DeviceRepo
import com.do_f.a1valet.base.DeviceQuery
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.database.entity.Device

class DeviceDataSource(val type: DeviceQuery): PageKeyedDataSource<Int, IRow>() {

    private val repo by lazy {
        DeviceRepo()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, IRow>) {
        loadDevices(params.key) { items, cursor ->
            callback.onResult(items, cursor)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, IRow>) {

    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, IRow>) {
        loadDevices(0) { items, cursor ->
            callback.onResult(items, null, cursor)
        }
    }

    private fun loadDevices(lastIndex: Int, callback: ((List<IRow>, Int) -> Unit)) {
        val items: List<Device> = emptyList()
        val nextCursor: Int = 0
        when (type) {
            is DeviceQuery.Search -> {
                // Get data from the api
                // repo.search(type.query)
            }
            is DeviceQuery.Default -> {
                // Get data from the api
//                repo.getDevices()
            }
            is DeviceQuery.Favorite -> {
                // Get data from the api
                // repo.filterByFavorite(type.isFavorite)
            }
            is DeviceQuery.Filter -> {
                // Get data from the api
                // repo.filterByType(type.type)
            }
        }

        // Process the data
        val devices = items.map { IRow.DeviceRow(it) }
        callback.invoke(devices, nextCursor)
    }
}