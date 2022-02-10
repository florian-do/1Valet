package com.do_f.a1valet.features.home.ui.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.do_f.a1valet.R
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.databinding.FragmentFilterDeviceBinding
import com.do_f.a1valet.extensions.setSafeOnClickListener
import com.do_f.a1valet.filterMenu
import com.do_f.a1valet.model.DeviceFilter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterDeviceFragment : BottomSheetDialogFragment() {

    var onFilterClickListener: ((DeviceFilter) -> Unit)? = null

    private val controller by lazy  {
        BottomMenuController()
    }

    private lateinit var binding: FragmentFilterDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_device, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFeed.setController(controller)
        buildData()
        controller.onRowListener = {
            onFilterClickListener?.invoke(it)
            dismiss()
        }
    }

    private fun buildData() {
        val items: MutableList<IRow> = mutableListOf()
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_favorite, R.drawable.ic_favorite, DeviceFilter.FILTERTYPE.FAVORITE)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_none_favorite, R.drawable.ic_favorite_border, DeviceFilter.FILTERTYPE.NONEFAVORITE)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_type_smartlock, R.drawable.ic_smartlock, DeviceFilter.FILTERTYPE.SMARTLOCK)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_type_thermostat, R.drawable.ic_thermostat, DeviceFilter.FILTERTYPE.THERMOSTAT)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_type_android, R.drawable.ic_phone_android, DeviceFilter.FILTERTYPE.ANDROID)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_type_iphone, R.drawable.ic_phone_iphone, DeviceFilter.FILTERTYPE.IPHONE)))
        items.add(IRow.DeviceFilterRow(DeviceFilter(R.string.filter_reset, R.drawable.ic_close, DeviceFilter.FILTERTYPE.RESET)))
        controller.setData(items)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilterDeviceFragment()
    }

    private inner class BottomMenuController: TypedEpoxyController<List<IRow>>() {

        var onRowListener: ((DeviceFilter) -> Unit)? = null

        override fun buildModels(data: List<IRow>?) {
            data?.forEach {
                when (it) {
                    is IRow.DeviceFilterRow -> buildRow(it.deviceFilter)
                }
            }
        }

        private fun buildRow(deviceFilter: DeviceFilter) {
            filterMenu {
                id(deviceFilter.stringId)
                stringId(deviceFilter.stringId)
                drawableId(deviceFilter.drawableId)
                onBind { _, view, _ ->
                    view.dataBinding.root.setSafeOnClickListener {
                        this@BottomMenuController.onRowListener?.invoke(deviceFilter)
                    }
                }
            }
        }
    }
}