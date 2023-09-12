package com.antoniomy.citypoi.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.FragmentMapBinding
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.pois.PoisListFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel

class MapFragment(val poisVM: PoisViewModel, private var cityName: String? = null) : Fragment() {

    private var fragmentMapsBinding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMapsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return fragmentMapsBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisVM.apply {
            fragmentMapsBinding?.let { fragmentMapBinding = it}
            savedInstanceState?.let {  mapsBundle = it }
            loadMap()
            fragmentMapBinding?.poisVM = poisVM
        }

        fragmentMapsBinding?.let { setTittle(it) }
    }

    private fun setTittle(fragmentMapBinding: FragmentMapBinding) {
        fragmentMapBinding.headerId.headerTitle.text = getString(R.string.maps_default_tittle) //Top bar title

        fragmentMapBinding.headerId.headerBack.setOnClickListener {//Back arrow
            replaceFragment(
                PoisListFragment(poisViewModel = poisVM), parentFragmentManager, PoisListFragment.POI_ID)
        }

    }

    companion object {
        const val POI_ID = "MapFragment"
    }

}