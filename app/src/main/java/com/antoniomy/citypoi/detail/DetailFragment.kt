package com.antoniomy.citypoi.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi


class DetailFragment(private val mPoi: Poi, private val viewModel: PoisViewModel) : Fragment() {

    private var popUpPoisDetailBinding: PopUpPoisDetailBinding?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popUpPoisDetailBinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_pois_detail, container, false)
        return popUpPoisDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.popUpBinding =popUpPoisDetailBinding
        popUpPoisDetailBinding?.let { viewModel.popUpDetail(mPoi, it) }
         initLocalDbOptions()
    }

    private fun initLocalDbOptions() {
        //Save
        popUpPoisDetailBinding?.icnSave?.setOnClickListener {
           viewModel.insertLocalPoi(mPoi)
        }
    }

}