package com.antoniomy.citypoi.navigation

import androidx.fragment.app.FragmentManager
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi

//import com.antoniomy.data.model.District
//import com.antoniomy.data.model.PoisRemote

interface CitiesNavigation {
    fun goToHome(poisViewModel:PoisViewModel , fragmentManager: FragmentManager)
    fun goToMap(poisVM: PoisViewModel, selectedCity:String, fragmentManager: FragmentManager)
    fun goToList(retrieveDistrict: District?, selectedCity: String, position: Int, fragmentManager: FragmentManager, poisViewModel:PoisViewModel )
    fun goToDetail(mPoi: Poi?, poisVM:PoisViewModel, fragmentManager: FragmentManager )
}