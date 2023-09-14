package com.antoniomy.citypoi.districts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.carousel.CarouselFragment
import com.antoniomy.citypoi.common.replaceFragment
import com.antoniomy.citypoi.databinding.FragmentHomeDistrictBinding
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import kotlin.system.exitProcess

class DistrictsFragment(private val poisViewModel: PoisViewModel) : Fragment() {

    private var fragmentHomeDistrictBinding: FragmentHomeDistrictBinding? = null

    private val madName = "Madrid"
    private val sevName = "Sevilla"
    private val bcnName = "Barcelona"

    private val homeCities: List<DistrictsListModel> = listOf(
        DistrictsListModel(madName, "Lavapíes", R.mipmap.ic_madrid_round, 1),
        DistrictsListModel(madName, "Centro", R.mipmap.ic_madrid_round, 2),
        DistrictsListModel(madName, "Malasaña", R.mipmap.ic_madrid_round, 3),
        DistrictsListModel(madName, "Chueca", R.mipmap.ic_madrid_round, 5),
        DistrictsListModel(madName, "Huertas", R.mipmap.ic_madrid_round, 6),
        DistrictsListModel(sevName, "Alfalfa, Casco Antiguo", R.mipmap.ic_sevilla_round, 7),
        DistrictsListModel(sevName, "Arenal, Museo", R.mipmap.ic_sevilla_round, 8),
        DistrictsListModel(sevName, "Macarena, S.Luis, S.Vicente", R.mipmap.ic_sevilla_round, 9),
        DistrictsListModel(sevName, "Santa Cruz, Juderia", R.mipmap.ic_sevilla_round, 10),
        DistrictsListModel(sevName, "Triana, Los Remedios", R.mipmap.ic_sevilla_round, 11),
        DistrictsListModel(bcnName, "Barceloneta, Poble Nou", R.mipmap.ic_barcelona_round, 12),
        DistrictsListModel(bcnName, "El born, Ribera", R.mipmap.ic_barcelona_round, 13),
        DistrictsListModel(bcnName, "Gótico", R.mipmap.ic_barcelona_round, 14),
        DistrictsListModel(bcnName, "Raval Poble Sec", R.mipmap.ic_barcelona_round, 15),
        DistrictsListModel(bcnName, "Eixample, Gracia", R.mipmap.ic_barcelona_round, 16)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeDistrictBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_district, container, false)
        return fragmentHomeDistrictBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeDistrictBinding?.poisVM = poisViewModel

        setUI()
        setHomeRecyclerViewAdapter()
    }

    private fun setUI() {
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = context?.getString(R.string.home_title)

        view?.findViewById<View>(R.id.saved_pois)?.apply {
            visibility = View.VISIBLE
            setOnClickListener {parentFragmentManager.replaceFragment(CarouselFragment(poisViewModel), POI_ID)}
        }
        view?.findViewById<View>(R.id.headerBack)?.apply {
            setBackgroundResource(R.drawable.baseline_close_24)

            setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }
        }
        poisViewModel.poisCount = homeCities.size.toString()  //Set counter
    }

    private fun setHomeRecyclerViewAdapter() {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvHome) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let { DistricsAdapter( homeCities, it,  poisViewModel) }
    }

    companion object {
        const val POI_ID = "DistrictsFragment"
    }
}