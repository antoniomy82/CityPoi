package com.antoniomy.citypoi.pois

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
import com.antoniomy.citypoi.common.PoiProvider
import com.antoniomy.citypoi.common.collectInLifeCycle
import com.antoniomy.citypoi.databinding.FragmentPoiListBinding
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class PoisListFragment(
    private val poisViewModel: PoisViewModel
) : Fragment() {

    private lateinit var fragmentPoiListBinding: FragmentPoiListBinding
    @Inject lateinit var citiesNavigation: CitiesNavigation
    @Inject lateinit var poiProvider: PoiProvider


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPoiListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_poi_list, container, false)
        return fragmentPoiListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setUI()
    }

    private fun setUI() {
        //Top bar title
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = getString(R.string.pois_default_tittle)

        //Back arrow
        view?.findViewById<View>(R.id.headerBack)?.apply {
            setBackgroundResource(R.drawable.baseline_close_24)

            setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }
        }
        //DB Icon
        view?.findViewById<View>(R.id.saved_pois)?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
               citiesNavigation.goToCarousel(poisViewModel, parentFragmentManager)
            }
        }

        context?.let { poisViewModel.getDistrictMocked(it) }

        fragmentPoiListBinding.mapLayout.setOnClickListener {
            citiesNavigation.goToMap(poisViewModel,  parentFragmentManager)
        }
        //poisViewModel.getDistrict("$urlID")
    }

    private fun initObservers() {

        poisViewModel.fetchDistricts.collectInLifeCycle(viewLifecycleOwner) { it ->
            it.let {
                poisViewModel.retrieveDistrict = it
                setPoiRecyclerViewAdapter(it)
                if (it.name != null) {
                    poisViewModel.apply {
                        toolbarSubtitle = it.name.toString().uppercase()
                        poisCount = it.pois?.size.toString()
                        toolbarTitle = getString(R.string.pois_default_tittle)
                    }
                }
                fragmentPoiListBinding.poisVM = poisViewModel
            }
        }
    }


    private fun setPoiRecyclerViewAdapter(mDistrict: District) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvPois) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let {
            PoisListAdapter(
                poisViewModel,
                mDistrict,
                parentFragmentManager,
                citiesNavigation,
                poiProvider
            )
        }
    }


    companion object {
        const val POI_ID = "PoisListFragment"
    }
}