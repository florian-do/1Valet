package com.do_f.a1valet.model

data class DeviceFilter(
    val stringId: Int,
    val drawableId: Int,
    val type: FILTERTYPE
) {
    enum class FILTERTYPE {
        FAVORITE,
        NONEFAVORITE,
        ANDROID,
        IPHONE,
        THERMOSTAT,
        SMARTLOCK,
        RESET
    }
}
