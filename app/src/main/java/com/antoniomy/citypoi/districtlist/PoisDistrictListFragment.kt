package com.antoniomy.citypoi.districtlist

//import com.antoniomy.data.DistrictsRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.collectInLifeCycle
import com.antoniomy.citypoi.databinding.FragmentDistrictListBinding
import com.antoniomy.citypoi.homedistrict.HomeDistrictFragment
import com.antoniomy.citypoi.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

//import com.antoniomy.data.model.District

class PoisDistrictListFragment(
    private val mDistrict: District? = null,
    private var cityName: String? = null,
    private val urlID: Int
) : Fragment() {

    private val poisViewModel: PoisViewModel by viewModels()
    private lateinit var fragmentDistrictListBinding: FragmentDistrictListBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDistrictListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_district_list, container, false)
        return fragmentDistrictListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (mDistrict) {
            null -> {
                poisViewModel.isRetrieveData.collectInLifeCycle(viewLifecycleOwner) { isRetrieveData ->
                    if (isRetrieveData.name != null) {
                        setTittleFromAdapter(isEmpty = true)
                        setObserverIntoViewModel(isRetrieveData)
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


        setUI()
        context?.let { poisViewModel.frgMainContext = it }

    }


    private fun setUI() {
        poisViewModel.loadDistrict("$urlID")

        //Top bar title
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = cityName
        poisViewModel.selectedCity = cityName.toString()

        //Back arrow
        view?.findViewById<View>(R.id.headerBack)?.setOnClickListener {
            replaceFragment(HomeDistrictFragment(), parentFragmentManager)
        }

    }

    private fun setObserverIntoViewModel(retrieveDistrict: District) {

        retrieveDistrict.let {
            poisViewModel.retrieveDistrict = it
            setDistrictListRecyclerViewAdapter(it)
            if (it.name != null) {
                setTittleFromAdapter(
                    it.name.toString(),
                    it.pois?.size.toString()
                )
            }
            fragmentDistrictListBinding.poisVM = poisViewModel //update VM content
        }
    }

    private fun setDistrictListRecyclerViewAdapter(mDistrict: District) {
        val recyclerView: RecyclerView = view?.findViewById(R.id.rvPois) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let {
            PoisDistrictListAdapter(
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
}