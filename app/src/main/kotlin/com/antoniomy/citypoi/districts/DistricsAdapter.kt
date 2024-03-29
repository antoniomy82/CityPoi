package com.antoniomy.citypoi.districts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.AdapterHomeDistrictsBinding
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.viewmodel.PoisViewModel

class DistricsAdapter(
    private val districtList: List<DistrictsListModel>,
    private val context: Context,
    private val poisViewModel: PoisViewModel,
    private val citiesNavigation: CitiesNavigation
) :
    RecyclerView.Adapter<DistricsAdapter.ViewHolder>() {

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
                citiesNavigation.goToList(poisViewModel,(context as AppCompatActivity).supportFragmentManager )
            }
        }
    }

    override fun getItemCount() = districtList.size
    class ViewHolder(val adapterHomeDistrictsBinding: AdapterHomeDistrictsBinding) : RecyclerView.ViewHolder(adapterHomeDistrictsBinding.root)
}
