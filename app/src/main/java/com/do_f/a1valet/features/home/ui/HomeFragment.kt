package com.do_f.a1valet.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.do_f.a1valet.R
import com.do_f.a1valet.base.BFragment
import com.do_f.a1valet.base.DeviceQuery
import com.do_f.a1valet.base.IRow
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.databinding.FragmentHomeBinding
import com.do_f.a1valet.extensions.setSafeOnClickListener
import com.do_f.a1valet.features.device.ui.DeviceDetailFragment
import com.do_f.a1valet.features.home.controller.DevicesController
import com.do_f.a1valet.features.home.controller.DevicesPagedController
import com.do_f.a1valet.features.home.ui.bottomsheet.FilterDeviceFragment
import com.do_f.a1valet.features.home.viewmodel.HomeViewModel
import com.do_f.a1valet.model.DeviceFilter

@ExperimentalPagingApi
class HomeFragment : BFragment() {

    private lateinit var binding: FragmentHomeBinding

    private var doUserDidASearch = false
    private var isSearchShowed = false

    private var networkType = NETWORKTYPE.ROOM

    private val viewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private val controller by lazy {
        DevicesController()
    }

    private val pagedController by lazy {
        DevicesPagedController()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun initViews() {
        binding.hasData = true
        if (controller.currentData == null) {
            setSearchShowed(false)
            if (networkType == NETWORKTYPE.ROOM)
                callApi()
        } else {
            setSearchShowed(isSearchShowed)
        }

        val editText: EditText = binding.simpleSearchView.findViewById(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(resources.getColor(R.color.white, null))
        editText.setHintTextColor(resources.getColor(R.color.white, null))

        binding.rvFeed.layoutManager = LinearLayoutManager(requireContext())
        when (networkType) {
            NETWORKTYPE.ROOM -> binding.rvFeed.setController(controller)
            NETWORKTYPE.PAGING -> binding.rvFeed.setController(pagedController)
        }

        // Get the data from the API from the DataSource
        viewModel.data.observe(viewLifecycleOwner) {
            pagedController.submitList(it)
        }

//        Epoxy doesn't handle paging3 PagingSource object yet
//        lifecycleScope.launchWhenCreated {
//            viewModel.pagedData.collectLatest {
//                pagedController.submitList(it)
//            }
//        }
    }

    override fun initListeners() {
        controller.onRowClickListener = {
            replace(DeviceDetailFragment.newInstance(it.id))
        }

        pagedController.onRowClickListener = {
            replace(DeviceDetailFragment.newInstance(it.id))
        }

        binding.swipeRefresher.setOnRefreshListener { callApi() }
        binding.toolbarFilter.setSafeOnClickListener { showFilterBottomMenu() }
        binding.simpleSearchView.setOnCloseListener {
            resetResearch()
            false
        }
        
        binding.simpleSearchView.setOnSearchClickListener {
            setSearchShowed(true)
        }

        binding.simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { search(query) }
                doUserDidASearch = true
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun callApi() {
        when (networkType) {
            NETWORKTYPE.ROOM -> viewModel.getDevices().observe(viewLifecycleOwner) { buildData(it) }
            NETWORKTYPE.PAGING -> viewModel.invalidate(DeviceQuery.Default())
        }
    }

    private fun search(query: String) {
        when (networkType) {
            NETWORKTYPE.ROOM -> viewModel.search(query).observe(viewLifecycleOwner) { buildData(it) }
            NETWORKTYPE.PAGING -> viewModel.invalidate(DeviceQuery.Search(query))
        }
    }

    private fun filterByType(type: String) {
        when (networkType) {
            NETWORKTYPE.ROOM -> viewModel.filterByType(type).observe(viewLifecycleOwner) { buildData(it) }
            NETWORKTYPE.PAGING -> viewModel.invalidate(DeviceQuery.Filter(type))
        }
    }

    private fun filterByFavorite(isFavorite: Boolean) {
        when (networkType) {
            NETWORKTYPE.ROOM -> viewModel.filterByFavorite(isFavorite).observe(viewLifecycleOwner) { buildData(it) }
            NETWORKTYPE.PAGING -> viewModel.invalidate(DeviceQuery.Favorite(isFavorite))
        }
    }

    private fun buildData(items: List<Device>) {
        binding.hasData = items.isNotEmpty()
        binding.swipeRefresher.isRefreshing = false
        controller.setData(items.map { device -> IRow.DeviceRow(device) })
    }

    private fun showFilterBottomMenu() {
        val f = FilterDeviceFragment.newInstance()
        f.show(parentFragmentManager, null)
        f.onFilterClickListener = {
            when (it.type) {
                DeviceFilter.FILTERTYPE.FAVORITE -> filterByFavorite(true)
                DeviceFilter.FILTERTYPE.NONEFAVORITE -> filterByFavorite(false)
                DeviceFilter.FILTERTYPE.RESET -> callApi()
                else -> filterByType(it.type.name.lowercase())
            }
        }
    }

    private fun setSearchShowed(b: Boolean) {
        binding.isSearchShowed = b
        isSearchShowed = b
    }

    private fun resetResearch() {
        setSearchShowed(false)
        if (doUserDidASearch) {
            callApi()
            doUserDidASearch = false
        }
    }

    override fun mOnBackPressed(): Boolean {
        return if (isSearchShowed) {
            if (doUserDidASearch) {
                binding.simpleSearchView.onActionViewCollapsed()
                binding.simpleSearchView.onActionViewCollapsed()
                resetResearch()
            } else {
                binding.simpleSearchView.onActionViewCollapsed()
                callApi()
                setSearchShowed(false)
            }
            false
        } else {
            super.mOnBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    enum class NETWORKTYPE {
        ROOM,
        PAGING
    }
}