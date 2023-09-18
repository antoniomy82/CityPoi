package com.antoniomy.citypoi.common


import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.antoniomy.citypoi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


fun FragmentManager.replaceFragment(fragment: Fragment, tag: String) {
    try {
        this.commit { replace(R.id.frame_container, fragment, tag) }
    } catch (e: Exception) {
        Log.e("__replaceFragment", e.toString())
    }
}


fun Marker.loadIcon(context: Context, url: String?) {

    Glide.with(context)
        .asBitmap()
        .load(url)
        .listener(object : RequestListener<Bitmap> {

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return try {
                    resource?.let { setIcon(BitmapDescriptorFactory.fromBitmap(it)) }
                    true
                } catch (e: Exception) {
                    Log.e("GlideError", e.toString())
                    false
                }
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).submit()
}


fun Long.getTimeResult() = "${(this / 1000 / 60).toString().padStart(2, '0')}:" +
        "${(this / 1000 % 60).toString().padStart(2, '0')} "


/** A sample use: viewModel.event.onEach(::renderEvent).launchInLifeCycle(viewLifecycleOwner) */
inline fun <reified T> Flow<T>.collectInLifeCycle(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(lifecycleOwner.lifecycle).onEach(
            collector
        ).collect()
    }
}