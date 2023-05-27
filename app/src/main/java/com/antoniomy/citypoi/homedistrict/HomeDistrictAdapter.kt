package com.antoniomy.citypoi.homedistrict

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.AdapterHomeDistrictsBinding
import com.antoniomy.citypoi.districtlist.PoisDistrictListFragment
import com.antoniomy.citypoi.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel

class HomeDistrictAdapter(
    private val districtList: List<CitiesListModel>,
    private val context: Context,
    private val poisViewModel: PoisViewModel
) :
    RecyclerView.Adapter<HomeDistrictAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_home_districts,
            parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterHomeDistrictsBinding.apply {
            cityName.text = districtList[position].cityName
            nameDistrict.text = districtList[position].district
            imagePoi.background = districtList[position].flag?.let { ContextCompat.getDrawable(context, it) }

            root.setOnClickListener {
                replaceFragment(districtList[position].urlId?.let { it1 ->
                    PoisDistrictListFragment(null, districtList[position].cityName, it1, poisViewModel)
                }, (context as AppCompatActivity).supportFragmentManager)
            }
        }
    }

    override fun getItemCount() = districtList.size
    class ViewHolder(val adapterHomeDistrictsBinding: AdapterHomeDistrictsBinding) : RecyclerView.ViewHolder(adapterHomeDistrictsBinding.root)
}
