package com.do_f.a1valet.base

interface DeviceQuery {
    class Search(val query: String): DeviceQuery
    class Filter(val type: String): DeviceQuery
    class Favorite(val isFavorite: Boolean): DeviceQuery
    class Default: DeviceQuery
}