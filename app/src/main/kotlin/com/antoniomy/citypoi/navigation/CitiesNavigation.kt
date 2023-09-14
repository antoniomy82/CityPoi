package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi

interface CitiesNavigation {
    fun goToHome(poisViewModel:PoisViewModel , fragmentManager: FragmentManager)
    fun goToMap(poisViewModel: PoisViewModel, fragmentManager: FragmentManager)
    fun goToList(poisViewModel:PoisViewModel, fragmentManager: FragmentManager,  )
    fun goToDetail(mPoi: Poi?, poisVM:PoisViewModel, fragmentManager: FragmentManager )

    fun goToCarousel(poisVM:PoisViewModel, fragmentManager: FragmentManager)
}