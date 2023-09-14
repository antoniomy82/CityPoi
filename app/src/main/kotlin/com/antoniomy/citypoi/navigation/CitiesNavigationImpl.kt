package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.carousel.CarouselFragment
import com.antoniomy.citypoi.common.replaceFragment
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.districts.DistrictsFragment
import com.antoniomy.citypoi.map.MapFragment
import com.antoniomy.citypoi.pois.PoisListFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi

class CitiesNavigationImpl : CitiesNavigation {
    override fun goToHome( poisViewModel:PoisViewModel ,fragmentManager: FragmentManager) {
        fragmentManager.replaceFragment(DistrictsFragment(poisViewModel), DistrictsFragment.POI_ID)
    }

    override fun goToMap(
        poisViewModel: PoisViewModel,
        fragmentManager: FragmentManager
    ) {
        fragmentManager.replaceFragment(MapFragment(poisViewModel), MapFragment.POI_ID)
    }

    override fun goToList(
        poisViewModel:PoisViewModel,
        fragmentManager: FragmentManager
    ) {
        fragmentManager.replaceFragment(PoisListFragment(poisViewModel), PoisListFragment.POI_ID)

    }

    override fun goToDetail(mPoi: Poi?, poisVM: PoisViewModel, fragmentManager: FragmentManager) {
        mPoi?.let { it1 -> DetailFragment(it1, poisVM) }
            ?.let { fragmentManager.replaceFragment(it, DetailFragment.POI_ID) }
    }

    override fun goToCarousel(poisVM: PoisViewModel, fragmentManager: FragmentManager) {
        fragmentManager.replaceFragment(CarouselFragment(poisVM), CarouselFragment.POI_ID)
    }
}