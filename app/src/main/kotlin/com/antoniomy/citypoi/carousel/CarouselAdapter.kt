package com.antoniomy.citypoi.carousel

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.antoniomy.citypoi.R
import com.antoniomy.citypoi.common.PoiProvider
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.viewmodel.DIRECTION
import com.antoniomy.citypoi.viewmodel.PoisViewModel
import com.antoniomy.domain.model.Poi
import com.bumptech.glide.Glide

class CarouselAdapter(
    private val itemList: List<Poi>,
    context: Context,
    private val viewModel: PoisViewModel,
    private val citiesNavigation: CitiesNavigation,
    private val poiProvider: PoiProvider
) : PagerAdapter() {

    override fun getCount() = itemList.size
    private val mContext = (context as ContextWrapper).baseContext as AppCompatActivity //For hilt cast issue

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            layoutInflater.inflate(R.layout.carousel_options_view_item, container, false)
        val imageCarousel = (view.findViewById<View>(R.id.image_carousel_card) as ImageView)
        val title = view.findViewById<TextView>(R.id.title_carousel_card)
        val description = view.findViewById<TextView>(R.id.description_carousel_card)
        val city = view.findViewById<TextView>(R.id.city_name_carousel)
        val district = view.findViewById<TextView>(R.id.name_district_carousel)
        val categoryIcn = view.findViewById<View>(R.id.category_icon_carousel) as ImageView


        if (itemList[position].image != null) itemList[position].image.let {
            Glide.with(mContext).load(it).into(imageCarousel)
        }
        if (itemList[position].image == null) imageCarousel.visibility = View.GONE

        title.text = itemList[position].name
        title.contentDescription = itemList[position].name
        city.text = itemList[position].city
        district.text = itemList[position].district
        itemList[position].categoryIcon.let { Glide.with(mContext).load(it).into(categoryIcn) }

        if (itemList[position].description == null) description.visibility = View.GONE
        else {
            description.text = itemList[position].description
            description.contentDescription = itemList[position].description
        }
        view.setOnClickListener {
            viewModel.popUpDirection = DIRECTION.GO_TO_CAROUSEL
            poiProvider.setPoi(itemList[position])
            citiesNavigation.goToDetail(viewModel,mContext.supportFragmentManager )
        }

        Log.d(
            "POI--> ",
            " image: " + itemList[position].image + "\n latitude: " + itemList[position].latitude + "\n longitude:" + itemList[position].longitude + "\n categoryIcon:" + itemList[position].categoryIcon + "\n categoryMarker:" + itemList[position].categoryMarker+"\n name:" +
                    itemList[position].name + "\n description " + itemList[position].description + "\n audio:"+itemList[position].audio + "\n city:" + itemList[position].city + "\n district " + itemList[position].district
        )
        (container as ViewPager).addView(view, 0)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}

