package com.antoniomy.citypoi.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.main.CustomBottomSheet
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi

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
        initLocalDbOptions()
    }

    private fun initLocalDbOptions() {
        popUpPoisDetailBinding?.icnSave?.setOnClickListener {
            saveAction()
        }
    }

    private fun saveAction() = bottomSheet.apply {
        setValues(
            getString(R.string.bottom_sheet_tittle),
            getString(R.string.bottom_sheet_message_save),
            R.drawable.baseline_save_24
        )
        showBottomSheet()
        acceptBtn().setOnClickListener {
            val setSave = viewModel.insertLocalPoi(mPoi)
            closeBottomSheet()
            Toast.makeText(context, "POI guardado: $setSave", Toast.LENGTH_LONG).show()
        }
        cancelBtn().setOnClickListener { closeBottomSheet() }
    }

    companion object {
        const val POI_ID = "DetailFragment"
    }

}