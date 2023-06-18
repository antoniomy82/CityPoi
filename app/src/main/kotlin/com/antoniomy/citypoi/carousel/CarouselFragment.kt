package com.antoniomy.citypoi.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.databinding.FragmentCarouselBinding
import com.antoniomy.citypoi.home.HomeDistrictFragment
import com.antoniomy.citypoi.main.collectInLifeCycle
import com.antoniomy.citypoi.main.replaceFragment
import com.antoniomy.citypoi.viewmodel.PoisViewModel
class CarouselFragment(private val viewModel: PoisViewModel) : Fragment() {

    private lateinit var binding: FragmentCarouselBinding
    private lateinit var sliderDotsPanel: LinearLayout
    private lateinit var dots: Array<ImageView?>
    private lateinit var viewPager: ViewPager
    var dotsCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = layoutInflater.inflate(R.layout.fragment_carousel, container, false)
        binding = FragmentCarouselBinding.bind(view)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSavedPois()
        setToolbar()
        initObserver()
    }

    private fun initObserver() {
        viewModel.fetchPois.collectInLifeCycle(viewLifecycleOwner) {

            if (it.isNotEmpty()) {

                CAROUSEL_MODEL = CarouselModel(carouselCards = it)

                SELECTED_SLIDE = 0
                setCarouselView()
                setDotsView()
                setButtonsListener()
                setDotsListener()
            }

        }
    }

    private fun setToolbar() {
        val headerTitle = view?.findViewById<View>(R.id.headerTitle) as TextView
        headerTitle.text = context?.getString(R.string.Poi_saved)

        view?.findViewById<View>(R.id.saved_pois)?.visibility = View.GONE

        view?.findViewById<View>(R.id.headerBack)?.apply {
            setOnClickListener {
                replaceFragment(HomeDistrictFragment(viewModel), (context as AppCompatActivity).supportFragmentManager)
            }
        }
    }

    private fun setCarouselView() {
        viewPager = view?.findViewById<View>(R.id.carouselCardsViewPager) as ViewPager
        sliderDotsPanel = view?.findViewById<View>(R.id.slider_dots) as LinearLayout
        val viewPagerAdapter = CarouselAdapter(CAROUSEL_MODEL.carouselCards, requireContext(), viewModel)
        viewPager.adapter = viewPagerAdapter
        viewPager.setPageTransformer(true, CarouselZoomOut())
        dotsCount = viewPagerAdapter.count

        CAROUSEL_MODEL.apply {
            if (primaryButtonText != null) binding.carouselNextBtn.text = getString(primaryButtonText)  //Set customise text
        }

    }

    private fun setDotsView() {
        dots = arrayOfNulls(dotsCount)

        for (i in 0 until dotsCount) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.carousel_non_active_dot))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8, 0, 8, 0)
            sliderDotsPanel.addView(dots[i], params)
        }

        dots[0]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.carousel_active_dot))
    }

    private fun setButtonsListener() {
        with(binding) {
            carouselNextBtn.setOnClickListener {
                when {
                    viewPager.currentItem + 1 == viewPager.adapter?.count -> CAROUSEL_MODEL.actionPrimaryButton?.let { it() }
                    viewPager.currentItem + 1 != viewPager.adapter?.count -> viewPager.currentItem++ // Go to next slide
                }
            }

            carouselBackBtn.setOnClickListener {
                when {
                    viewPager.currentItem - 1 == viewPager.adapter?.count -> CAROUSEL_MODEL.actionSecondaryButton?.let { it() }
                    viewPager.currentItem - 1 != viewPager.adapter?.count -> viewPager.currentItem-- // Go to back slide
                }
            }
        }
    }

    private fun setDotsListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.carousel_non_active_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.carousel_active_dot))
                SELECTED_SLIDE = if (position < dotsCount) position else position - 1

            }

            override fun onPageScrollStateChanged(state: Int) { //Scroll to top when start
                binding.carouselSettingsScrollView.post { binding.carouselSettingsScrollView.fullScroll(binding.carouselSettingsScrollView.top) }
            }
        })
    }


    companion object {
        var SELECTED_SLIDE: Int = 0
        lateinit var CAROUSEL_MODEL: CarouselModel
    }
}