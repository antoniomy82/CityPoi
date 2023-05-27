package com.antoniomy.citypoi.districtlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.AdapterPoisDistrictListBinding
import com.antoniomy.citypoi.detail.DetailFragment
import com.antoniomy.citypoi.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
//import com.antoniomy.data.model.District
import com.antoniomy.domain.model.District
import com.bumptech.glide.Glide

class PoisDistrictListAdapter(private val poisVm: PoisViewModel, private val mDistrict: District, private val fm: FragmentManager) :
    RecyclerView.Adapter<PoisDistrictListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_pois_district_list,
            parent, false)
    )


    //Binding each element with object element
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterPoisDistrictListBinding.apply {
            poisVm = poisVm
            namePoi.text = mDistrict.pois?.get(position)?.name

            // if( mDistrict.pois?.get(position)?.likesCount==null) holder.adapterPoisDistrictListBinding.likeQty.text = "0"
            // else holder.adapterPoisDistrictListBinding.likeQty.text = mDistrict.pois?.get(position)?.likesCount.toString()
        }

        holder.adapterPoisDistrictListBinding.root.setOnClickListener {
            poisVm.popUpLocation = 0
            replaceFragment(mDistrict.pois?.get(position)?.let { it1 ->
                DetailFragment(it1, poisVm) }, fm )
        }

        //Set image
        if (mDistrict.pois?.get(position)?.image?.url != null) Glide.with(holder.itemView).load(mDistrict.pois?.get(position)?.image?.url).into(holder.adapterPoisDistrictListBinding.imagePoi)

    }

    override fun getItemCount() = mDistrict.pois?.size ?:0

    class ViewHolder(val adapterPoisDistrictListBinding: AdapterPoisDistrictListBinding) : RecyclerView.ViewHolder(adapterPoisDistrictListBinding.root)

}
