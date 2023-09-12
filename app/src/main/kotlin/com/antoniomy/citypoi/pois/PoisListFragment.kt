package com.antoniomy.citypoi.pois

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.carousel.CarouselFragment
import com.antoniomy.citypoi.databinding.FragmentDistrictListBinding
import com.antoniomy.citypoi.main.collectInLifeCycle
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import kotlin.system.exitProcess

class PoisListFragment(
    private val mDistrict: District? = null,
    private var cityName: String? = null,
    private val urlID: Int = 0,
    private val poisViewModel: PoisViewModel
) : Fragment() {

    private lateinit var fragmentDistrictListBinding: FragmentDistrictListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDistrictListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_district_list, container, false)
        return fragmentDistrictListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setUI()
        poisViewModel.fragmentPoisListBinding = fragmentDistrictListBinding
       // context?.let { poisViewModel.getDistrictMocked(it) }
    }


    private fun setUI() {
        //Top bar title
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = getString(R.string.pois_default_tittle)

        poisViewModel.selectedCity =
            if (cityName == "") getString(R.string.pois_default_tittle) else cityName.toString()

        //Back arrow
        view?.findViewById<View>(R.id.headerBack)?.apply {
            //replaceFragment(DistrictsFragment(poisViewModel), parentFragmentManager)
            setBackgroundResource(R.drawable.baseline_close_24)

            setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }
        }

        view?.findViewById<View>(R.id.saved_pois)?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                replaceFragment(
                    CarouselFragment(poisViewModel),
                    (context as AppCompatActivity).supportFragmentManager, CarouselFragment.POI_ID)
            }
        }

        context?.let { poisViewModel.getDistrictMocked(it) }
        //poisViewModel.getDistrict("$urlID")
    }

    private fun initObservers() {

        when (mDistrict) {
            null -> {
                poisViewModel.fetchDistricts.collectInLifeCycle(viewLifecycleOwner) { it ->
                    setTittleFromAdapter(isEmpty = true)

                    it.let {
                        poisViewModel.retrieveDistrict = it
                        setDistrictListRecyclerViewAdapter(it)
                        if (it.name != null) {
                            setTittleFromAdapter(
                                it.name.toString(),
                                it.pois?.size.toString()
                            )
                        }
                        fragmentDistrictListBinding.poisVM = poisViewModel
                    }
                }
            }

            else -> {
                poisViewModel.retrieveDistrict = mDistrict
                setDistrictListRecyclerViewAdapter(mDistrict)
                setTittleFromAdapter(
                    mDistrict.name.toString(),
                    mDistrict.pois?.size.toString(),
                )
                fragmentDistrictListBinding.poisVM = poisViewModel
            }
        }

        poisViewModel.errorResponse.collectInLifeCycle(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            //context?.let { poisViewModel.getDistrictMocked(it) }
        }
    }


    private fun setDistrictListRecyclerViewAdapter(mDistrict: District) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvPois) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let {
            PoisListAdapter(
                poisViewModel,
                mDistrict,
                parentFragmentManager
            )
        }
    }


    private fun setTittleFromAdapter(
        tittle: String = "",
        count: String = "",
        isEmpty: Boolean = false
    ) {

        when (isEmpty) {
            true -> showLoading()
            false -> hideLoading(tittle, count)
        }
    }


    private fun showLoading() {
        poisViewModel.apply {
            districtTittle.value = context?.getString(R.string.loading)
            poisCount.value = ""
        }
        fragmentDistrictListBinding.apply {
            progressBar.visibility = View.VISIBLE
            mapLayout.visibility = View.GONE
            rvPois.visibility = View.GONE
        }
    }

    private fun hideLoading(tittle: String, count: String) {
        poisViewModel.apply {
            districtTittle.value = tittle.uppercase()
            poisCount.value = count
        }
        fragmentDistrictListBinding.apply {
            progressBar.visibility = View.GONE
            mapLayout.visibility = View.VISIBLE
            rvPois.visibility = View.VISIBLE
        }
    }
    companion object {
        const val POI_ID = "PoisListFragment"
    }
}