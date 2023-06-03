package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.districtlist.PoisDistrictListFragment
import com.antoniomy.citypoi.homedistrict.HomeDistrictFragment
import com.antoniomy.citypoi.map.MapFragment
import com.antoniomy.citypoi.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi

class CitiesNavigationImpl : CitiesNavigation {
    override fun goToHome( poisViewModel:PoisViewModel ,fragmentManager: FragmentManager) {
        replaceFragment(HomeDistrictFragment(poisViewModel), fragmentManager)
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
        fragmentManager: FragmentManager,
        poisViewModel:PoisViewModel
    ) {
        replaceFragment(PoisDistrictListFragment(
            retrieveDistrict,
            selectedCity,
            position,
            poisViewModel
        ), fragmentManager)

    }

    override fun goToDetail(mPoi: Poi?, poisVM: PoisViewModel, fragmentManager: FragmentManager) {
       replaceFragment(mPoi?.let { it1 -> DetailFragment(it1, poisVM) }, fragmentManager)
    }
}