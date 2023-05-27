package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.districtlist.PoisDistrictListFragment
import com.antoniomy.citypoi.homedistrict.HomeDistrictFragment
import com.antoniomy.citypoi.map.MapFragment
import com.antoniomy.citypoi.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
//import com.antoniomy.data.model.District
//import com.antoniomy.data.model.PoisRemote
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Pois

class CitiesNavigationImpl : CitiesNavigation {
    override fun goToHome(fragmentManager: FragmentManager) {
        replaceFragment(HomeDistrictFragment(), fragmentManager)
    }

    override fun goToMap(
        poisVM: PoisViewModel,
        selectedCity: String,
        fragmentManager: FragmentManager
    ) {
        replaceFragment(MapFragment(poisVM, selectedCity), fragmentManager)
    }

    override fun goToList(
        retrieveDistrict: District?,
        selectedCity: String,
        position: Int,
        fragmentManager: FragmentManager
    ) {
        replaceFragment(PoisDistrictListFragment(retrieveDistrict, selectedCity, position), fragmentManager)

    }

    override fun goToDetail(mPoi: Pois?, poisVM: PoisViewModel, fragmentManager: FragmentManager) {
       replaceFragment(mPoi?.let { it1 -> DetailFragment(it1, poisVM) }, fragmentManager)
    }
}