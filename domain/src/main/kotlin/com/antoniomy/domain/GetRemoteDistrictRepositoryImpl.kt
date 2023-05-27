package com.antoniomy.domain

import android.util.Log
import com.antoniomy.data.DistrictRemoteDataSource
import com.antoniomy.data.model.CategoryDto
import com.antoniomy.data.model.DistrictDto
import com.antoniomy.data.model.MultimediaDto
import com.antoniomy.domain.model.Category
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Multimedia
import com.antoniomy.domain.model.Pois
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class GetRemoteDistrictRepositoryImpl : GetRemoteDistrictRepository {

    private val mapperFlow = MutableStateFlow(District())
    private val remoteDataSource =  DistrictRemoteDataSource() //TODO pasar a hilt
    private var remoteDistrict =remoteDataSource.retrieveDistrictDto

    override fun retrieveDataStatus(urlID: String): MutableStateFlow<Boolean> {

        if(remoteDataSource.getDistrictList(urlID).value.name!=null){
            Log.d("retrieveDATA->", "true")
            return MutableStateFlow(true)
        }
      else {
            Log.d("retrieveDATA->", "False")
            return   MutableStateFlow(false)
        }
    }

    override fun getRemoteDistrict(urlID: String): MutableStateFlow<DistrictDto> = remoteDataSource.getDistrictList(urlID)
    override fun districtRemoteToDistrictMapper(): MutableStateFlow<District>  {

        Log.d("districtMAPPPER","dentroooo")
        val districtDto = remoteDistrict.value
        val poiList = mutableListOf<Pois>()
        val poiListSize = districtDto.pois?.size ?: 0

        CoroutineScope(IO).launch {

            for (i in 0..poiListSize) {
                val retrieveDistrict = districtDto.pois?.get(i)
                poiList.add(
                    i, Pois(
                        likesCount = retrieveDistrict?.likesCount,
                        eventsCount = retrieveDistrict?.eventsCount,
                        newsCount = retrieveDistrict?.newsCount,
                        id = retrieveDistrict?.id,
                        image = retrieveDistrict?.image?.toMultimedia(),
                        galleryImages = retrieveDistrict?.galleryImages?.let { toListMultimedia(it) },
                        latitude = retrieveDistrict?.latitude,
                        longitude = retrieveDistrict?.longitude,
                        category = retrieveDistrict?.categoryDto?.toCategory(),
                        premium = retrieveDistrict?.premium,
                        name = retrieveDistrict?.name,
                        description = retrieveDistrict?.description,
                        video = retrieveDistrict?.video?.toMultimedia(),
                        audio = retrieveDistrict?.audio?.toMultimedia(),
                        likeIt = retrieveDistrict?.likeIt
                    )
                )
            }

            val mDistrict = District(
                poisCount = districtDto.poisCount,
                id = districtDto.id,
                name = districtDto.name,
                image = districtDto.image?.toMultimedia(),
                galleryImages = districtDto.galleryImages?.let { toListMultimedia(it) },
                coordinates = districtDto.coordinates,
                video = districtDto.video?.toMultimedia(),
                audio = districtDto.audio?.toMultimedia(),
                pois = poiList
            )

            mapperFlow.value = mDistrict

            Log.d("mDistrict", mDistrict.toString())
        }

        return mapperFlow


    }

    inline fun <reified T : Any> Any.mapTo(): T =
        GsonBuilder().create().run { fromJson(toJson(this@mapTo), T::class.java) }

    private fun CategoryDto.toCategory(): Category =
        mapTo<Category>().copy(
            id = id,
            icon = icon?.toMultimedia(),
            marker = marker?.toMultimedia(),
            markerIcon = markerIcon?.toMultimedia(),
            name = "$name"
        )

    private fun MultimediaDto.toMultimedia(): Multimedia =
        mapTo<Multimedia>().copy(
            name = "$name",
            description = "$description",
            width = "$width",
            height = "$height",
            size = size,
            id = id,
            url = "$url",
        )

    private fun toListMultimedia(listMultimediaDto: List<MultimediaDto>): List<Multimedia> {
        val multimediaList = mutableListOf<Multimedia>()
        for (i in 0..listMultimediaDto.size) {
            multimediaList.add(i, listMultimediaDto[i].toMultimedia())
        }
        return multimediaList
    }

}


