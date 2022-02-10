package com.do_f.a1valet.datasource

import androidx.paging.PageKeyedDataSource
import com.do_f.a1valet.api.DeviceRepo
import com.do_f.a1valet.base.DeviceQuery
import com.do_f.a1valet.base.IRow
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class DeviceDataSource(val type: DeviceQuery): PageKeyedDataSource<Int, IRow>() {

    // small hack
    private var pageIncrement = 0

    private val repo by lazy {
        DeviceRepo()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, IRow>) {
        pageIncrement = 0
        loadDevices(0) { items, cursor ->
            GlobalScope.launch(Dispatchers.Main) {
                callback.onResult(items, null, cursor)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, IRow>) {
        if (pageIncrement < 2) {
            loadDevices(params.key) { items, cursor ->
                GlobalScope.launch(Dispatchers.Main) {
                    callback.onResult(items, cursor)
                }
            }
            pageIncrement++
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, IRow>) {

    }

    private fun loadDevices(lastIndex: Int, callback: ((List<IRow>, Int) -> Unit)) {
        GlobalScope.launch {
            val nextCursor: Int = 0
            // Mimic Api Call
            val items = when (type) {
                is DeviceQuery.Search -> repo.searchSync(type.query)
                is DeviceQuery.Favorite -> repo.filterByFavoriteSync(type.isFavorite)
                is DeviceQuery.Filter -> repo.filterByTypeSync(type.type)
                else -> repo.getDevicesSync()
            }

            // Process the data
            val devices = items.map { IRow.DeviceRow(it) }
            callback.invoke(devices, nextCursor)
        }
    }
}