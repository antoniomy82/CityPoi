package com.antoniomy.citypoi.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Poi
import com.antoniomy.domain.repository.local.LocalRepository
import com.antoniomy.domain.repository.remote.RemoteRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PoisViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    var fetchDistricts = MutableStateFlow(District())
    var fetchPois = MutableStateFlow(listOf<Poi>())
    var loaderEvent = MutableStateFlow<LoaderEvent>(LoaderEvent.ShowLoading)
    var readPoiObserver = MutableStateFlow(Poi())

    //Toolbar values
    var toolbarSubtitle =""
    var toolbarTitle= ""
    var poisCount : String = "0"

    var imageFlow = MutableStateFlow<Bitmap?>(null)
    var iconFlow = MutableStateFlow<Bitmap?>(null)

    var retrieveDistrict: District? = null
    var popUpDirection: DIRECTION = DIRECTION.GO_TO_LIST

    //Media counter
    val remainingTime = MutableLiveData<String>()
    var mediaPlayer= MutableStateFlow(MediaPlayer())

/*
    fun getDistrict(urlId: String) = viewModelScope.launch {
        remoteRepository.getDistrictList(urlId).collect { fetchDistricts.value = it }
    }
*/
    fun getDistrictMocked(context: Context) = viewModelScope.launch {
        remoteRepository.getMockedList(context).collect {
            fetchDistricts.value = it
        }
    }

    fun getSavedPois() = viewModelScope.launch { localRepository.fetchPoiList().collect { fetchPois.value = it } }

    //Local DB
    fun insertLocalPoi(mPoi: Poi) = localRepository.insertPoi(mPoi)

    fun deleteLocalPoi(name: String) = localRepository.deletePoi(name)

    suspend fun readPoi(name: String) = viewModelScope.launch {
        localRepository.readPoi(name).collect {
            readPoiObserver.value = it
        }
    }

    fun loadImageFromUrl(context: Context, mImageFlow :MutableStateFlow<Bitmap?>, url:String)=
        viewModelScope.launch(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                       mImageFlow.value = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }


    fun loadMediaPlayer(audioUri: Uri, context: Context) =
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlayer.value = MediaPlayer.create(context, audioUri)
        }
}

enum class DIRECTION { GO_TO_LIST, GO_TO_MAP, GO_TO_CAROUSEL }

sealed class LoaderEvent {
    data object ShowLoading : LoaderEvent()
    data object HideLoading : LoaderEvent()
}