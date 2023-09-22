package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.viewmodel.PoisViewModel

interface CitiesNavigation {
    fun goToHome(poisViewModel:PoisViewModel , fragmentManager: FragmentManager)
    fun goToMap(poisViewModel: PoisViewModel, fragmentManager: FragmentManager)
    fun goToList(poisViewModel:PoisViewModel, fragmentManager: FragmentManager)
    fun goToDetail(poisViewModel:PoisViewModel, fragmentManager: FragmentManager )

    fun goToCarousel(poisViewModel:PoisViewModel, fragmentManager: FragmentManager)
}