package com.antoniomy.citypoi.map

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.common.loadIcon
import com.antoniomy.citypoi.databinding.FragmentMapBinding
import com.antoniomy.citypoi.navigation.CitiesNavigationImpl
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment(private val poisVM: PoisViewModel) : Fragment(), OnMapReadyCallback {

    private var fragmentMapsBinding: FragmentMapBinding? = null
    private var mapsBundle: Bundle? = null
    private var map: GoogleMap? = null
    private var citiesNavigation = CitiesNavigationImpl() //TODO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMapsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return fragmentMapsBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let { mapsBundle = it }
        fragmentMapsBinding?.poisVM = poisVM
        setUi()
        loadMap()
        mapRefresh()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun mapRefresh(){
        Handler(Looper.getMainLooper()).postDelayed({
            fragmentMapsBinding?.map?.getMapAsync(this)
        }, 500)
    }

    private fun setUi() {
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = poisVM.toolbarTitle

        fragmentMapsBinding?.headerId?.headerBack?.setOnClickListener { citiesNavigation.goToList(poisVM,  parentFragmentManager)}

        fragmentMapsBinding?.listLayout?.setOnClickListener {
            citiesNavigation.goToList(poisVM,  parentFragmentManager)
        }
    }

    private fun loadMap() {
        fragmentMapsBinding?.map?.apply {
            onCreate(mapsBundle)
            onResume()
        }
        map?.let { onMapReady(it) }
        fragmentMapsBinding?.map?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.let { map = it }
        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val poisSize = poisVM.retrieveDistrict?.pois?.size ?: 0
        val zoomLevel = 6f //15f
        var mLatLng: LatLng

        for (i in 0 until poisSize) {
            mLatLng = LatLng(
                poisVM.retrieveDistrict?.pois?.get(i)?.latitude?.toDouble() ?: 0.0,
                poisVM.retrieveDistrict?.pois?.get(i)?.longitude?.toDouble() ?: 0.0
            )
            val mMarker: Marker? = map?.addMarker(MarkerOptions().position(mLatLng))
            context?.let {
                mMarker?.loadIcon(
                    it,
                    poisVM.retrieveDistrict?.pois?.get(i)?.categoryMarker
                )
            }
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zoomLevel))
        }

        googleMap.setOnMarkerClickListener {
            it.position.latitude
            val mPoi: Poi? =
                poisVM.retrieveDistrict?.pois?.find { p -> p.latitude?.toDouble() == it.position.latitude && p.longitude?.toDouble() == it.position.longitude }

            poisVM.popUpDirection = PoisViewModel.DIRECTION.GO_TO_MAP
            citiesNavigation.goToDetail(
                mPoi,
                poisVM,
                parentFragmentManager
            )
            true
        }
        map?.let { setMapStyle(it) }
    }


    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(context?.let {
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

    companion object {
        const val POI_ID = "MapFragment"
    }

}