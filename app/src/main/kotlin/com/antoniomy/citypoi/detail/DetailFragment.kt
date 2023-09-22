package com.antoniomy.citypoi.detail

import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.common.CustomBottomSheet
import com.antoniomy.citypoi.common.CustomProgressDialog
import com.antoniomy.citypoi.common.PoiProvider
import com.antoniomy.citypoi.common.collectInLifeCycle
import com.antoniomy.citypoi.common.countDown
import com.antoniomy.citypoi.common.getTimeResult
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.viewmodel.DIRECTION
import com.antoniomy.citypoi.viewmodel.LoaderEvent
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailFragment(private val viewModel: PoisViewModel) : Fragment(), OnMapReadyCallback {

    private var popUpPoisDetailBinding: PopUpPoisDetailBinding? = null
    private val bottomSheet by lazy { CustomBottomSheet(requireContext()) }

    @Inject lateinit var citiesNavigation : CitiesNavigation
    @Inject lateinit var poiProvider: PoiProvider
    private lateinit var mPoi: Poi
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private var map: GoogleMap? = null
    private var mainBundle: Bundle? = null

    private var launchTimer: CountDownTimer? = null
    private var totalDuration by Delegates.notNull<Long>()
    private var myUri: Uri? = null

    var actualTime = ""
    var aboutPoi = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        popUpPoisDetailBinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_pois_detail, container, false)
        return popUpPoisDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPoi = poiProvider.getPoi()
        setUi()
        setDbButtonListener()
        initObservers()
    }


    private fun initObservers() {
        viewModel.readPoiObserver.collectInLifeCycle(viewLifecycleOwner) {
            when (it.city) {
                null -> setButtonSave()
                else -> setButtonDelete()
            }
        }

        viewModel.viewModelScope.launch { viewModel.readPoi(mPoi.name) }

        viewModel.imageFlow.collectInLifeCycle(viewLifecycleOwner) {bitmap->
            popUpPoisDetailBinding?.photoPopup?.setImageBitmap(bitmap)
        }

        viewModel.iconFlow.collectInLifeCycle(viewLifecycleOwner) {bitmap->
            popUpPoisDetailBinding?.iconPopup?.setImageBitmap(bitmap)
        }

        viewModel.loaderEvent.collectInLifeCycle(viewLifecycleOwner) { onLoaderEvent(it) }

        viewModel.mediaPlayer.collectInLifeCycle(viewLifecycleOwner){mediaPlayer->
            totalDuration = mediaPlayer.duration.toLong()
            viewModel.remainingTime.value = totalDuration.getTimeResult()
        }

        viewModel.remainingTime.observe(viewLifecycleOwner){
            actualTime = it
            popUpPoisDetailBinding?.frg = updateFrg()
            Handler(Looper.getMainLooper()).postDelayed({ context?.let {
                viewModel.loaderEvent.value = LoaderEvent.HideLoading
            } }, 1500)
        }
    }

    private fun onLoaderEvent(event: LoaderEvent) = when (event) {
        LoaderEvent.ShowLoading -> progressDialog.start(getString(R.string.loader_text))
        LoaderEvent.HideLoading -> {
             progressDialog.stop()
             popUpPoisDetailBinding?.soundLayout?.visibility = View.VISIBLE
        }
    }


    private fun setButtonSave() {
        popUpPoisDetailBinding?.icnSave?.setBackgroundResource(R.drawable.baseline_save_24)
        setDbButtonListener(false)
    }

    private fun setButtonDelete() {
        popUpPoisDetailBinding?.icnSave?.setBackgroundResource(R.drawable.ic_delete)
        setDbButtonListener(true)
    }

    private fun setDbButtonListener(isSave: Boolean = false) {
        popUpPoisDetailBinding?.icnSave?.setOnClickListener {
            when (isSave) {
                true -> deleteBottomSheet()
                false -> saveBottomSheet()
            }
        }
    }

    private fun deleteBottomSheet() = bottomSheet.apply {
        setValues(
            getString(R.string.bottom_sheet_tittle_delete),
            getString(R.string.bottom_sheet_message_delete),
            R.drawable.ic_delete
        )
        showBottomSheet()
        acceptBtn().setOnClickListener {
            viewModel.deleteLocalPoi(mPoi.name)
            setButtonSave()
            closeBottomSheet()
        }
        cancelBtn().setOnClickListener { closeBottomSheet() }
    }

    private fun saveBottomSheet() = bottomSheet.apply {
        setValues(
            getString(R.string.bottom_sheet_tittle),
            getString(R.string.bottom_sheet_message_save),
            R.drawable.baseline_save_24
        )
        showBottomSheet()
        acceptBtn().setOnClickListener {
            viewModel.insertLocalPoi(mPoi)
            setButtonDelete()
            closeBottomSheet()
        }
        cancelBtn().setOnClickListener { closeBottomSheet() }
    }

    private fun setUi() {
        aboutPoi = mPoi.city +" , "+ mPoi.district
        popUpPoisDetailBinding?.titlePopup?.text = mPoi.name
        popUpPoisDetailBinding?.streetPopup?.text = mPoi.description
        popUpPoisDetailBinding?.let { loadMapPopUp(it) }
        viewModel.loaderEvent.value = LoaderEvent.ShowLoading
        myUri = Uri.parse(mPoi.audio)

        context?.let { viewModel.loadImageFromUrl(it, viewModel.imageFlow, mPoi.image.toString()) }
        context?.let { viewModel.loadImageFromUrl(it, viewModel.iconFlow, mPoi.categoryIcon.toString()) }
        context?.let { context -> myUri?.let { uri -> viewModel.loadMediaPlayer(uri, context) } }
    }

    private fun updateFrg() = this

    fun buttonPlay() {
        launchTimer = totalDuration.countDown(viewModel.remainingTime) { buttonStop() }
        viewModel.mediaPlayer.value.start()

        popUpPoisDetailBinding?.apply {
            tvPass.visibility = View.VISIBLE
            playBtn.visibility = View.GONE
            stopBtn.visibility = View.VISIBLE
        }
    }

    fun buttonStop() {
        viewModel.mediaPlayer.value.stop()
        myUri?.let { context?.let { it1 -> viewModel.loadMediaPlayer(it, it1) } }
        launchTimer?.cancel()

        popUpPoisDetailBinding?.apply {
            playBtn.visibility = View.VISIBLE
            stopBtn.visibility = View.GONE
            popUpPoisDetailBinding?.frg = updateFrg()
        }
    }


    fun closePopUp() {
        when (viewModel.popUpDirection) {
            DIRECTION.GO_TO_LIST -> citiesNavigation.goToList(viewModel, parentFragmentManager)
            DIRECTION.GO_TO_MAP -> citiesNavigation.goToMap(viewModel, parentFragmentManager)
            DIRECTION.GO_TO_CAROUSEL -> citiesNavigation.goToCarousel(viewModel, parentFragmentManager)
        }
        buttonStop()
    }

    private fun loadMapPopUp(popUpBinding: PopUpPoisDetailBinding) {
        popUpBinding.mapPopup.apply {
            onCreate(mainBundle)
            onResume()
        }
        popUpBinding.mapPopup.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.let { map = it }
        googleMap.uiSettings.isZoomControlsEnabled = true //Zoom in/out

        val zoomLevel = 15f
        mPoi.let {
            context.let {
                map?.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            mPoi.latitude?.toDouble() ?: 0.0,
                            mPoi.longitude?.toDouble() ?: 0.0
                        )
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
            }
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mPoi.latitude?.toDouble() ?: 0.0,
                        mPoi.longitude?.toDouble() ?: 0.0
                    ), zoomLevel
                )
            )
        }
    }

    companion object {
        const val POI_ID = "DetailFragment"
    }
}