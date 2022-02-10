package com.do_f.a1valet.base

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

open class BDataSourceFactory<Key : Any, Value : Any>: DataSource.Factory<Key, Value>() {

    var source : DataSource<Key, Value>? = null

    override fun create(): DataSource<Key, Value> {
        return source!!
    }
}