package com.antoniomy.citypoi.viewmodel

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.FragmentDistrictListBinding
import com.antoniomy.citypoi.databinding.FragmentMapBinding
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.main.getTimeResult
import com.antoniomy.citypoi.main.loadIcon
import com.antoniomy.citypoi.main.mediaProgress
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.navigation.CitiesNavigationImpl
import com.antoniomy.citypoi.pois.PoisListFragment
import com.antoniomy.domain.datasource.local.LocalRepository
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
class PoisViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel(), OnMapReadyCallback {

    var fetchDistricts = MutableStateFlow(District())
    var fetchPois = MutableStateFlow(listOf<Poi>())
    val loaderEvent = MutableStateFlow<LoaderEvent>(LoaderEvent.ShowLoading)
    var readPoiObserver = MutableStateFlow(Poi())

    private var citiesNavigation = CitiesNavigationImpl()
    private var mainBundle: Bundle? = null
    private var position: Int? = 0

    //Main fragment values
    val districtTittle = MutableLiveData<String>()
    val poisCount = MutableLiveData("0")
    var fragmentPoisListBinding: FragmentDistrictListBinding? = null

    //Maps Fragment values
    var fragmentMapBinding: FragmentMapBinding? = null
    var mapsBundle: Bundle? = null

    //Global values
    var retrieveDistrict: District? = null
    var selectedCity: String = ""
    private var map: GoogleMap? = null
    private var isIntoPopUp: Boolean = false
    private var selectedPoi: Poi? = null
    private var iconCategory: String? = null
    private lateinit var timeValue: String

    //Media player
    val remainingTime = MutableLiveData<String>()
    var popUpBinding: PopUpPoisDetailBinding? = null
    private var totalDuration by Delegates.notNull<Long>()
    private var mediaPlayer: MediaPlayer? = null
    private var myUri: Uri? = null
    private var launchTimer: CountDownTimer? = null
    var popUpDirection: DIRECTION = DIRECTION.GO_TO_LIST

    fun getDistrict(urlId: String) = viewModelScope.launch {
        remoteRepository.getDistrictList(urlId).collect { fetchDistricts.value = it }
    }

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

    //TODO : Cities Navigator
    fun goToList() = position?.let { PoisListFragment(retrieveDistrict, selectedCity, it, this) }
        ?.let {
            replaceFragment(
                it,
                (fragmentPoisListBinding?.root?.context as AppCompatActivity).supportFragmentManager,
                PoisListFragment.POI_ID
            )
        }

    private fun goToDetail(mPoi: Poi?) = mPoi?.let { it1 -> DetailFragment(it1, this) }
        ?.let {
            replaceFragment(
                it,
                (fragmentMapBinding?.root?.context as AppCompatActivity).supportFragmentManager,
                DetailFragment.POI_ID
            )
        }

    fun getVM(): PoisViewModel = this

    fun goToMap() = citiesNavigation.goToMap(
        this,
        selectedCity,
        (fragmentPoisListBinding?.root?.context as AppCompatActivity).supportFragmentManager
    )


    //Set the POI detail in a popup
    fun popUpDetail(mPoi: Poi?, popUpBinding: PopUpPoisDetailBinding) {

        //Media Player values
        myUri = Uri.parse(mPoi?.audio) // initialize Uri here
        mediaPlayer = MediaPlayer.create(popUpBinding.root.context, myUri)
        totalDuration = mediaPlayer?.duration?.toLong() ?: 0
        timeValue = getTimeResult(totalDuration)
        remainingTime.value = timeValue

        if (timeValue != "null") popUpBinding.soundLayout.visibility = View.VISIBLE

        popUpBinding.titlePopup.text = mPoi?.name
        popUpBinding.streetPopup.text = mPoi?.description

        //Set image
        if (mPoi?.image != null) popUpBinding.root.context.let {
            popUpBinding.photoPopup.let { it1 ->
                if (it != null) {
                    Glide.with(it).load(mPoi.image).into(it1)
                }
            }
        }

        //Set icon image
        popUpBinding.root.context.let {
            popUpBinding.iconPopup.let { it1 ->
                if (it != null) {
                    Glide.with(it).load(mPoi?.categoryIcon).into(it1)
                }
            }
        }

        iconCategory = mPoi?.categoryIcon
        selectedPoi = mPoi
        popUpBinding.vm = this //Update the view with dataBinding

        loadMapPopUp(popUpBinding)
    }

    fun closePopUp() {
        Toast.makeText(popUpBinding?.root?.context, "Volviendo a POIs...", Toast.LENGTH_SHORT)
            .show()
        when (popUpDirection) {
            DIRECTION.GO_TO_LIST -> goToList()
            DIRECTION.GO_TO_MAP -> goToMap()
        }
        buttonStop()
    }

    fun buttonPlay() {
        launchTimer = mediaProgress(totalDuration, this)
        mediaPlayer?.start()

        popUpBinding?.apply {
            tvPass.visibility = View.VISIBLE
            playBtn.visibility = View.GONE
            stopBtn.visibility = View.VISIBLE
        }
    }

    fun buttonStop() {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(fragmentPoisListBinding?.root?.context, myUri)
        launchTimer?.cancel()

        popUpBinding?.apply {
            tvPass.text = timeValue
            playBtn.visibility = View.VISIBLE
            stopBtn.visibility = View.GONE
            vm = getVM() //Update the view with dataBinding
        }
    }

    //Map fragment
    fun loadMap() {
        if (retrieveDistrict != null) {
            districtTittle.value = retrieveDistrict?.name?.uppercase()
            if (retrieveDistrict?.pois?.size == 0) poisCount.value = "0"
            else poisCount.value = retrieveDistrict?.pois?.size.toString()
        }

        fragmentMapBinding?.map?.apply {
            onCreate(mapsBundle)
            onResume()
        }
        fragmentMapBinding?.map?.getMapAsync(this)
        isIntoPopUp = false

        map?.let { onMapReady(it) }
    }

    //Map into PopUp
    private fun loadMapPopUp(popUpBinding: PopUpPoisDetailBinding) {
        popUpBinding.mapPopup.apply {
            onCreate(mainBundle)
            onResume()
        }
        popUpBinding.mapPopup.getMapAsync(this)
        isIntoPopUp = true
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(fragmentMapBinding?.root?.context?.let {
                MapStyleOptions.loadRawResourceStyle(
                    it,
                    R.raw.map_style
                )
            })
            if (!success) Log.e("__MAP", "Style parsing failed.")
        } catch (e: Resources.NotFoundException) {
            Log.e("__MAP", "Can't find style. Error: ", e)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.let { map = it }
        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val poisSize = retrieveDistrict?.pois?.size ?: 0
        val zoomLevel = 6f //15f
        var mLatLng: LatLng

        when (isIntoPopUp) {
            false -> {
                for (i in 0 until poisSize) {
                    mLatLng = LatLng(
                        retrieveDistrict?.pois?.get(i)?.latitude?.toDouble() ?: 0.0,
                        retrieveDistrict?.pois?.get(i)?.longitude?.toDouble() ?: 0.0
                    )
                    val mMarker: Marker? = map?.addMarker(MarkerOptions().position(mLatLng))
                    fragmentMapBinding?.root?.context?.let {
                        mMarker?.loadIcon(
                            it,
                            retrieveDistrict?.pois?.get(i)?.categoryMarker
                        )
                    }
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel))
                }

                googleMap.setOnMarkerClickListener {
                    it.position.latitude
                    val mPoi: Poi? =
                        retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }
                    isIntoPopUp = false
                    popUpDirection = DIRECTION.GO_TO_MAP
                    //TODO : Cities Navigator
                    goToDetail(mPoi)

                    true
                }
                map?.let { setMapStyle(it) }
            }

            true -> {
                selectedPoi.let {
                    fragmentMapBinding?.root?.context.let {
                        map?.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    selectedPoi?.latitude?.toDouble() ?: 0.0,
                                    selectedPoi?.longitude?.toDouble() ?: 0.0
                                )
                            )
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )
                    }
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                selectedPoi?.latitude?.toDouble() ?: 0.0,
                                selectedPoi?.longitude?.toDouble() ?: 0.0
                            ), zoomLevel
                        )
                    )
                }
            }
        }//When
    }

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