package com.antoniomy.citypoi.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.main.CustomBottomSheet
import com.antoniomy.citypoi.main.collectInLifeCycle
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi
import kotlinx.coroutines.launch


class DetailFragment(private val mPoi: Poi, private val viewModel: PoisViewModel) : Fragment() {

    private var popUpPoisDetailBinding: PopUpPoisDetailBinding? = null
    private val bottomSheet by lazy { CustomBottomSheet(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        popUpPoisDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.pop_up_pois_detail, container, false)
        return popUpPoisDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.popUpBinding = popUpPoisDetailBinding
        popUpPoisDetailBinding?.let { viewModel.popUpDetail(mPoi, it) }
        setDbButtonListener()
        initObserve()
        launchReadPoi()
    }

    private fun launchReadPoi() = viewModel.viewModelScope.launch { viewModel.readPoi(mPoi.name) }

    private fun initObserve() =
        viewModel.readPoiObserver.collectInLifeCycle(viewLifecycleOwner) {
            when (it.city) {
                null -> setButtonSave()
                else -> setButtonDelete()
            }
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

    companion object {
        const val POI_ID = "DetailFragment"
    }

}