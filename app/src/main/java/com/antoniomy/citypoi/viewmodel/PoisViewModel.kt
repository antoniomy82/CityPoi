package com.antoniomy.citypoi.viewmodel

import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.FragmentDistrictListBinding
import com.antoniomy.citypoi.databinding.FragmentMapBinding
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.districtlist.PoisDistrictListFragment
import com.antoniomy.citypoi.main.getTimeResult
import com.antoniomy.citypoi.main.loadIcon
import com.antoniomy.citypoi.main.mediaProgress
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.navigation.CitiesNavigationImpl
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class PoisViewModel @Inject constructor(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository) : ViewModel(), OnMapReadyCallback {

    private var _fetchDistricts = MutableStateFlow(District())
    val fetchDistricts: StateFlow<District> get() = _fetchDistricts


    private var _fetchPois = MutableStateFlow(mutableListOf <Poi>())
    val fetchPois: StateFlow<List<Poi>> get() = _fetchPois

    private val _errorResponse = MutableStateFlow("Loading..")
    val errorResponse: StateFlow<String> get() = _errorResponse

    private var citiesNavigation = CitiesNavigationImpl()
    private var mainBundle: Bundle? = null
    private var position: Int? = 0

    //Main fragment values
    val districtTittle = MutableLiveData<String>()
    val poisCount = MutableLiveData<String>().also { it.value = "0" }
    lateinit var fragmentPoisListBinding : FragmentDistrictListBinding

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
    var popUpLocation: Int = 0

    fun getDistrict(urlId: String) = viewModelScope.launch { remoteRepository.getDistrictList(urlId).collect { _fetchDistricts.value = it } }
    fun getSavedPois() = viewModelScope.launch { fetchLocalPois() }

    fun setMapsUI() {
        fragmentMapBinding?.headerId?.headerTitle?.text = selectedCity //Top bar title

        fragmentMapBinding?.headerId?.headerBack?.setOnClickListener {//Back arrow
            citiesNavigation.goToHome(this, (fragmentMapBinding?.root?.context as AppCompatActivity).supportFragmentManager)
        }

        if (retrieveDistrict != null) {
            districtTittle.value = retrieveDistrict?.name?.uppercase()
            if (retrieveDistrict?.pois?.size == 0) poisCount.value = "0"
            else poisCount.value = retrieveDistrict?.pois?.size.toString()
        }

        fragmentMapBinding?.poisVM = this //Update the view with dataBinding
        loadMap()
        map?.let { onMapReady(it) }
    }

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
        if (mPoi?.image != null) popUpBinding.root.context.let { popUpBinding.photoPopup.let { it1 -> if (it != null) { Glide.with(it).load(mPoi.image).into(it1) } } }

        //Set icon image
        popUpBinding.root.context.let { popUpBinding.iconPopup.let { it1 -> if (it != null) { Glide.with(it).load(mPoi?.categoryIcon).into(it1) } } }

        iconCategory = mPoi?.categoryIcon
        selectedPoi = mPoi
        popUpBinding.vm = this //Update the view with dataBinding

        loadMapPopUp(popUpBinding)
    }

    //TODO : Cities Navigator
    fun goToList() = replaceFragment(position?.let { PoisDistrictListFragment(retrieveDistrict, selectedCity, it, this) }, (fragmentPoisListBinding.root.context as AppCompatActivity).supportFragmentManager)

    private fun goToDetail(mPoi: Poi?) = replaceFragment(mPoi?.let { it1 -> DetailFragment(it1, this) }, (fragmentMapBinding?.root?.context as AppCompatActivity).supportFragmentManager)

    private fun setMapStyle(map: GoogleMap) {
        try { val success = map.setMapStyle(fragmentMapBinding?.root?.context?.let { MapStyleOptions.loadRawResourceStyle(it, R.raw.map_style) })
            if (!success) Log.e("__MAP", "Style parsing failed.")
        } catch (e: Resources.NotFoundException) { Log.e("__MAP", "Can't find style. Error: ", e) }
    }

    //Map fragment
    private fun loadMap() {
        fragmentMapBinding?.map?.apply {
            onCreate(mapsBundle)
            onResume()
        }
        fragmentMapBinding?.map?.getMapAsync(this)
        isIntoPopUp = false
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

    fun getVM(): PoisViewModel = this

    fun goToMap() = citiesNavigation.goToMap(this, selectedCity, (fragmentPoisListBinding.root.context as AppCompatActivity).supportFragmentManager)

    fun closePopUp() {
        when (popUpLocation) {
            0 -> goToList()
            1 -> goToMap()
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
        mediaPlayer = MediaPlayer.create(fragmentPoisListBinding.root.context, myUri)
        launchTimer?.cancel()

        popUpBinding?.apply {
            tvPass.text = timeValue
            playBtn.visibility = View.VISIBLE
            stopBtn.visibility = View.GONE
            vm = getVM() //Update the view with dataBinding
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.let { map = it }
        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val poisSize = retrieveDistrict?.pois?.size ?: 0
        val zoomLevel = 15f
        var mLatLng: LatLng

        when (isIntoPopUp) {
            false -> {
                for (i in 0 until poisSize) {
                    mLatLng = LatLng(retrieveDistrict?.pois?.get(i)?.latitude?.toDouble() ?: 0.0, retrieveDistrict?.pois?.get(i)?.longitude?.toDouble() ?: 0.0)
                    val mMarker: Marker? = map?.addMarker(MarkerOptions().position(mLatLng))
                    fragmentMapBinding?.root?.context?.let { mMarker?.loadIcon(it, retrieveDistrict?.pois?.get(i)?.categoryMarker) }
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel))
                }

                googleMap.setOnMarkerClickListener {
                    it.position.latitude
                    val mPoi: Poi? = retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }
                    isIntoPopUp = false
                    popUpLocation = 1
                    //TODO : Cities Navigator
                    goToDetail(mPoi)

                    true
                }
                map?.let { setMapStyle(it) }
            }

            true -> {
                selectedPoi.let {
                    fragmentMapBinding?.root?.context.let {
                        map?.addMarker(MarkerOptions().position(LatLng(selectedPoi?.latitude?.toDouble() ?: 0.0, selectedPoi?.longitude?.toDouble() ?: 0.0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))) }
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedPoi?.latitude?.toDouble() ?: 0.0, selectedPoi?.longitude?.toDouble() ?: 0.0), zoomLevel))
                }
            }
        }//When
    }

    fun insertLocalPoi(mPoi: Poi) = localRepository.insertPoi(mPoi)

    fun deleteLocalPoi(name: String) = localRepository.deletePoi(name)

    fun fetchLocalPois() : List<Poi> = localRepository.fetchPoiList()
}