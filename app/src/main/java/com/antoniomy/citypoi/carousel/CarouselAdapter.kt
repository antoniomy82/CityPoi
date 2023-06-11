package com.antoniomy.citypoi.carousel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.antoniomy.citypoi.R
import com.antoniomy.domain.model.Poi
import com.bumptech.glide.Glide

class CarouselAdapter(private val itemList: List<Poi>, private val context: Context) : PagerAdapter() {

    override fun getCount() = itemList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean { return view === `object` }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.carousel_options_view_item, container, false)
        val imageCarousel = (view.findViewById<View>(R.id.image_carousel_card) as ImageView)
        val title = view.findViewById<TextView>(R.id.title_carousel_card)
        val description =  view.findViewById<TextView>(R.id.description_carousel_card)


        if(itemList[position].image !=null) itemList[position].image.let{ Glide.with(context).load(it).into(imageCarousel)}
        if(itemList[position].image == null) imageCarousel.visibility = View.GONE

        title.text = itemList[position].name
        title.contentDescription = itemList[position].name

        if(itemList[position].description ==null) description.visibility = View.GONE
        else {
            description.text = itemList[position].description
            description.contentDescription = itemList[position].description }

        (container as ViewPager).addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}

