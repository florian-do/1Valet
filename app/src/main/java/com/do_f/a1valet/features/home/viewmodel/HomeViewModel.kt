package com.do_f.a1valet.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.do_f.a1valet.App
import com.do_f.a1valet.api.DeviceRepo
import com.do_f.a1valet.base.BDataSourceFactory
import com.do_f.a1valet.base.DeviceQuery
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.database.mediator.DeviceRemoteMediator
import com.do_f.a1valet.datasource.DeviceDataSource

@ExperimentalPagingApi
class HomeViewModel: ViewModel() {

    private val repo by lazy {
        DeviceRepo()
    }

    private val dao by lazy {
        App.cacheDb.deviceDao()
    }

    var data : LiveData<PagedList<IRow>>
//    var dataSourceFactory : BDataSourceFactory<Int, IRow>? = null

    fun getDevices(): LiveData<List<Device>> = repo.getDevices()
    fun search(query: String): LiveData<List<Device>> = repo.search(query)
    fun filterByType(type: String): LiveData<List<Device>> = repo.filterByType(type)
    fun filterByFavorite(isFavorite: Boolean): LiveData<List<Device>> = repo.filterByFavorite(isFavorite)

    // Paging 3
    val pagedData = Pager(
        config = PagingConfig(5),
        remoteMediator = DeviceRemoteMediator()
    ) {
        dao.getDevicesPaged()
    }.flow.cachedIn(viewModelScope)

    private val dataSourceFactory = object : BDataSourceFactory<Int, IRow>() {

        private var type: DeviceQuery = DeviceQuery.Default()

        override fun create(): DataSource<Int, IRow> {
            source = DeviceDataSource(type)
            return source!!
        }

        fun setType(type: DeviceQuery) {
            this.type = type
            source?.invalidate()
        }
    }

    // Paging 2 for Epoxy
    init {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()

        data = LivePagedListBuilder(dataSourceFactory, config)
            .build()
    }

    fun invalidate(type: DeviceQuery) {
        dataSourceFactory.setType(type)
    }
}