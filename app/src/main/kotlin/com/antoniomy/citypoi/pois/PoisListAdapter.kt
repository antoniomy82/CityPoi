package com.antoniomy.citypoi.pois

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.AdapterPoisDistrictListBinding
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import com.bumptech.glide.Glide

class PoisListAdapter(private val poisVm: PoisViewModel, private val mDistrict: District, private val fm: FragmentManager) :
    RecyclerView.Adapter<PoisListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_pois_district_list, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterPoisDistrictListBinding.apply {
            poi = mDistrict.pois?.get(position)
        }

        holder.adapterPoisDistrictListBinding.root.setOnClickListener {
            poisVm.popUpLocation = 0
            mDistrict.pois?.get(position)?.let { it1 -> DetailFragment(it1, poisVm) }
                ?.let { it2 -> replaceFragment(it2, fm , DetailFragment.POI_ID) }
        }

        //Set image
        if (mDistrict.pois?.get(position)?.image != null) Glide.with(holder.itemView).load(mDistrict.pois?.get(position)?.image).into(holder.adapterPoisDistrictListBinding.imagePoi)
        if (mDistrict.pois?.get(position)?.categoryIcon != null) Glide.with(holder.itemView).load(mDistrict.pois?.get(position)?.categoryIcon).into(holder.adapterPoisDistrictListBinding.imageCategory)


    }

    override fun getItemCount() = mDistrict.pois?.size ?:0

    class ViewHolder(val adapterPoisDistrictListBinding: AdapterPoisDistrictListBinding) : RecyclerView.ViewHolder(adapterPoisDistrictListBinding.root)

}
