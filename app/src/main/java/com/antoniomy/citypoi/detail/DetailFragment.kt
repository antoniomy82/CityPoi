package com.antoniomy.citypoi.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.PopUpPoisDetailBinding
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.data.localdb.PoiLocalDAORepo
import com.antoniomy.domain.model.Poi


class DetailFragment(private val mPoi: Poi, private val mVm: PoisViewModel) : Fragment() {

    private var poisViewModel: PoisViewModel? = null
    private var popUpPoisDetailBinding: PopUpPoisDetailBinding?=null

    //Repository variables
    private val localRepository = PoiLocalDAORepo() //TODO hilt



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popUpPoisDetailBinding = DataBindingUtil.inflate(inflater, R.layout.pop_up_pois_detail, container, false)
        return popUpPoisDetailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poisViewModel= mVm
        poisViewModel?.popUpBinding=popUpPoisDetailBinding

        popUpPoisDetailBinding?.let { poisViewModel?.popUpDetail(mPoi, context, it) }

        context?.let { localDbStatus(it) }
    }

    private fun localDbStatus(context: Context) {

        //Save
        popUpPoisDetailBinding?.icnSave?.setOnClickListener {
            if(localRepository.insertPoi(context ,mPoi)) {
                Toast.makeText(context, "Poi Salvado in DB", Toast.LENGTH_LONG).show()
                popUpPoisDetailBinding?.icnSave?.visibility = View.GONE
            }else   Toast.makeText(context, "Poi NO GUARDADO in DB", Toast.LENGTH_LONG).show()
        }
    }

}