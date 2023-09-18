package com.antoniomy.citypoi.detail

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.common.CustomBottomSheet
import com.antoniomy.citypoi.common.CustomProgressDialog
import com.antoniomy.citypoi.common.collectInLifeCycle
import com.antoniomy.citypoi.common.getTimeResult
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.navigation.CitiesNavigationImpl
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class DetailFragment(private val mPoi: Poi, private val viewModel: PoisViewModel) : Fragment(), OnMapReadyCallback {

    private var popUpPoisDetailBinding: PopUpPoisDetailBinding? = null
    private val bottomSheet by lazy { CustomBottomSheet(requireContext()) }
    private var citiesNavigation = CitiesNavigationImpl() //TODO
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    //Media player
    val remainingTime = MutableLiveData<String>()
    private var totalDuration by Delegates.notNull<Long>()
    private var mediaPlayer: MediaPlayer? = null
    private var myUri: Uri? = null
    private var launchTimer: CountDownTimer? = null

    private var timeValue: String = ""
    private var map: GoogleMap? = null
    private var mainBundle: Bundle? = null

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

        setUi()
        setDbButtonListener()
        initObservers()
        launchReadPoi()
    }


    private fun launchReadPoi() = viewModel.viewModelScope.launch { viewModel.readPoi(mPoi.name) }

    private fun initObservers() {
        viewModel.readPoiObserver.collectInLifeCycle(viewLifecycleOwner) {
            when (it.city) {
                null -> setButtonSave()
                else -> setButtonDelete()
            }
        }

        viewModel.imageFlow.collectInLifeCycle(viewLifecycleOwner) {
            popUpPoisDetailBinding?.photoPopup?.setImageBitmap(it)
        }

        viewModel.iconFlow.collectInLifeCycle(viewLifecycleOwner) {
            popUpPoisDetailBinding?.iconPopup?.setImageBitmap(it)
            Handler(Looper.getMainLooper()).postDelayed({ setMedia() }, 200)
        }

        viewModel.loaderEvent.collectInLifeCycle(viewLifecycleOwner) { onLoaderEvent(it) }
    }

    private fun onLoaderEvent(event: PoisViewModel.LoaderEvent) = when (event) {
        PoisViewModel.LoaderEvent.ShowLoading -> progressDialog.start("Cargando, por favor espere")
        PoisViewModel.LoaderEvent.HideLoading -> progressDialog.stop()
    }


    private fun setButtonSave() {
        popUpPoisDetailBinding?.icnSave?.setBackgroundResource(R.drawable.heart_icon)
        setDbButtonListener(false)
    }

    private fun setButtonDelete() {
        popUpPoisDetailBinding?.icnSave?.setBackgroundResource(R.drawable.baseline_close_24)
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
            R.drawable.baseline_close_24
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


    //Set the POI detail in a popup
    private fun setUi() {
        popUpPoisDetailBinding?.titlePopup?.text = mPoi.name
        popUpPoisDetailBinding?.streetPopup?.text = mPoi.description
        viewModel.iconCategory = mPoi.categoryIcon //TODO
        viewModel.selectedPoi = mPoi //TODO
        popUpPoisDetailBinding?.let { loadMapPopUp(it) } //TODO
        viewModel.loaderEvent.value = PoisViewModel.LoaderEvent.ShowLoading

        context?.let { viewModel.renderImage(it, viewModel.imageFlow, mPoi.image.toString()) }
        context?.let { viewModel.renderImage(it, viewModel.iconFlow, mPoi.categoryIcon.toString()) }
    }

    private fun setMedia() {
        myUri = Uri.parse(mPoi.audio) // initialize Uri here
        mediaPlayer = MediaPlayer.create(context, myUri)
        totalDuration = mediaPlayer?.duration?.toLong() ?: 0
        timeValue = totalDuration.getTimeResult()
        remainingTime.value = timeValue
        if (timeValue != "null") popUpPoisDetailBinding?.soundLayout?.visibility = View.VISIBLE
        popUpPoisDetailBinding?.frg = this //Update the view with dataBinding
        viewModel.loaderEvent.value = PoisViewModel.LoaderEvent.HideLoading
    }

    fun updateFrg() = this

    private fun mediaProgress(totalDuration: Long): CountDownTimer {
        val timer = object : CountDownTimer(totalDuration, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime.value = millisUntilFinished.getTimeResult()
                popUpPoisDetailBinding?.frg = updateFrg()
            }

            override fun onFinish() = buttonStop()
        }
        timer.start()
        return timer
    }

    fun buttonPlay() {
        launchTimer = mediaProgress(totalDuration)
        mediaPlayer?.start()

        popUpPoisDetailBinding?.apply {
            tvPass.visibility = View.VISIBLE
            playBtn.visibility = View.GONE
            stopBtn.visibility = View.VISIBLE
        }
    }

    fun buttonStop() {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(popUpPoisDetailBinding?.root?.context, myUri)
        launchTimer?.cancel()

        popUpPoisDetailBinding?.apply {
            tvPass.text = timeValue
            playBtn.visibility = View.VISIBLE
            stopBtn.visibility = View.GONE
            popUpPoisDetailBinding?.frg = updateFrg()
        }
    }


    fun closePopUp() {
        Toast.makeText(context, "Volviendo a POIs...", Toast.LENGTH_SHORT).show()
        when (viewModel.popUpDirection) {
            PoisViewModel.DIRECTION.GO_TO_LIST -> citiesNavigation.goToList(viewModel, parentFragmentManager)
            PoisViewModel.DIRECTION.GO_TO_MAP -> citiesNavigation.goToMap(viewModel, parentFragmentManager)
        }
        buttonStop()
    }

    //Map into PopUp
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
        viewModel.selectedPoi.let {
            context.let {
                map?.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            viewModel.selectedPoi?.latitude?.toDouble() ?: 0.0,
                            viewModel.selectedPoi?.longitude?.toDouble() ?: 0.0
                        )
                    )
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
            }
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        viewModel.selectedPoi?.latitude?.toDouble() ?: 0.0,
                        viewModel.selectedPoi?.longitude?.toDouble() ?: 0.0
                    ), zoomLevel
                )
            )
        }
    }

    companion object {
        const val POI_ID = "DetailFragment"
    }
}