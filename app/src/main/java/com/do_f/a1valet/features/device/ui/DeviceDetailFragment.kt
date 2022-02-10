package com.do_f.a1valet.features.device.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.do_f.a1valet.R
import com.do_f.a1valet.base.BFragment
import com.do_f.a1valet.databinding.FragmentDeviceDetailBinding
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.extensions.setSafeOnClickListener
import com.do_f.a1valet.features.device.viewmodel.DeviceDetailViewModel
import com.google.gson.Gson

class DeviceDetailFragment: BFragment() {

    private lateinit var binding: FragmentDeviceDetailBinding
    private var deviceId: Int = 0

    private val viewModel by lazy {
        ViewModelProvider(this).get(DeviceDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deviceId = it.getInt(ARG_DEVICE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_detail, container, false)
        return binding.root
    }

    override fun initViews() {
        viewModel.getDevice(deviceId).observe(viewLifecycleOwner) {
            if (it != null) {
                binding.device = it
            } else {
                showErrorDialog(R.string.alert_error) { _, _ -> mPopBackStack() }
            }
        }
    }

    override fun initListeners() {
        binding.toolbarBack.setSafeOnClickListener { mPopBackStack() }
    }

    companion object {

        const val ARG_DEVICE = "arg_device"

        @JvmStatic
        fun newInstance(id: Int) = DeviceDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_DEVICE, id)
            }
        }
    }
}