package com.antoniomy.citypoi.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoniomy.domain.datasource.local.LocalRepository
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PoisViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    var fetchDistricts = MutableStateFlow(District())
    var fetchPois = MutableStateFlow(listOf<Poi>())
    val loaderEvent = MutableStateFlow<LoaderEvent>(LoaderEvent.ShowLoading)
    var readPoiObserver = MutableStateFlow(Poi())

    //Toolbar values
    var toolbarSubtitle =""
    var toolbarTitle= ""
    var poisCount : String = "0"


    var retrieveDistrict: District? = null
    var selectedPoi: Poi? = null
    var iconCategory: String? = null


    var popUpDirection: DIRECTION = DIRECTION.GO_TO_LIST  //TODO

/*
    fun getDistrict(urlId: String) = viewModelScope.launch {
        remoteRepository.getDistrictList(urlId).collect { fetchDistricts.value = it }
    }
*/
    fun getDistrictMocked(context: Context) = viewModelScope.launch {
        remoteRepository.getMockedList(context).collect {
            fetchDistricts.value = it
            Handler(Looper.getMainLooper()).postDelayed({
                loaderEvent.value = LoaderEvent.HideLoading
            }, 1000)
        }
    }

    fun getSavedPois() =
        viewModelScope.launch { localRepository.fetchPoiList().collect { fetchPois.value = it } }

    //Local DB
    fun insertLocalPoi(mPoi: Poi): Boolean = localRepository.insertPoi(mPoi)

    fun deleteLocalPoi(name: String): Boolean = localRepository.deletePoi(name)

    suspend fun readPoi(name: String) = viewModelScope.launch {
        localRepository.readPoi(name).collect {
            readPoiObserver.value = it
        }
    }

    sealed class LoaderEvent {
        data object ShowLoading : LoaderEvent()
        data object HideLoading : LoaderEvent()
    }

    enum class DIRECTION { GO_TO_LIST, GO_TO_MAP }
}