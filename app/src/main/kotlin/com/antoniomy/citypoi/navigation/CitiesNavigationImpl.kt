package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.districts.DistrictsFragment
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.map.MapFragment
import com.antoniomy.citypoi.pois.PoisListFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi

class CitiesNavigationImpl : CitiesNavigation {
    override fun goToHome( poisViewModel:PoisViewModel ,fragmentManager: FragmentManager) {
        replaceFragment(DistrictsFragment(poisViewModel), fragmentManager, DistrictsFragment.POI_ID)
    }

    override fun goToMap(
        poisVM: PoisViewModel,
        selectedCity: String,
        fragmentManager: FragmentManager
    ) {
        replaceFragment(MapFragment(poisVM, selectedCity), fragmentManager, MapFragment.POI_ID)
    }

    override fun goToList(
        retrieveDistrict: District?,
        selectedCity: String,
        position: Int,
        fragmentManager: FragmentManager,
        poisViewModel:PoisViewModel
    ) {
        replaceFragment(PoisListFragment(
            retrieveDistrict,
            selectedCity,
            position,
            poisViewModel
        ), fragmentManager, PoisListFragment.POI_ID)

    }

    override fun goToDetail(mPoi: Poi?, poisVM: PoisViewModel, fragmentManager: FragmentManager) {
        mPoi?.let { it1 -> DetailFragment(it1, poisVM) }
            ?.let { replaceFragment(it, fragmentManager, DetailFragment.POI_ID) }
    }
}