package com.antoniomy.citypoi.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.FragmentMapBinding
import com.antoniomy.citypoi.viewmodel.PoisViewModel

class MapFragment(val poisVM: PoisViewModel, private var cityName: String? = null) : Fragment() {

    private var fragmentMapBinding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMapBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map,
            container,
            false
        )
        return fragmentMapBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set fragment parameters in vm
        context?.let { it1 ->
            fragmentMapBinding?.let { it2 ->
                poisVM.setMapsFragmentBinding(
                    it1, view,
                    it2, savedInstanceState
                )
            }
        }
        poisVM.setMapsUI()
        poisVM.selectedCity = cityName.toString()
    }

}