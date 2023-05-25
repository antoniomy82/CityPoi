package com.antoniomy.domain

import com.antoniomy.data.DistrictRemoteDataSource
import com.antoniomy.data.model.MultimediaRemote
import com.antoniomy.domain.model.CategoryRemote
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.DistrictRemote
import com.antoniomy.domain.model.Multimedia
import com.antoniomy.domain.model.Pois
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GetRemoteDistrictRepositoryImpl
@Inject constructor(private val remoteDataSource: DistrictRemoteDataSource) : GetRemoteDistrictRepository {

    override fun getRemoteDistrict(urlID: String): MutableStateFlow<District> = districtRemoteToDistrictMapper(remoteDataSource.getDistrictList(urlID).value)


    private fun districtRemoteToDistrictMapper(districtRemote: DistrictRemote):MutableStateFlow<District>{

        val imageMultimedia = Multimedia(
            name = districtRemote.image?.name,
            description = districtRemote.image?.description,
            width = districtRemote.image?.width,
            height = districtRemote.image?.height,
            size = districtRemote.image?.size,
            id = districtRemote.image?.id,
            url = districtRemote.image?.url
        )

        val videoMultimedia = Multimedia(
            name = districtRemote.video?.name,
            description = districtRemote.video?.description,
            width = districtRemote.video?.width,
            height = districtRemote.video?.height,
            size = districtRemote.video?.size,
            id = districtRemote.video?.id,
            url = districtRemote.video?.url
        )

        val audioMultimedia = Multimedia(
            name = districtRemote.audio?.name,
            description = districtRemote.audio?.description,
            width = districtRemote.audio?.width,
            height = districtRemote.audio?.height,
            size = districtRemote.audio?.size,
            id = districtRemote.audio?.id,
            url = districtRemote.audio?.url
        )

        val galleryImagesMultimedia = mutableListOf<Multimedia>()
        val galleryImgSize= districtRemote.galleryImages?.size ?:0
            for (i in 0 .. galleryImgSize){
                galleryImagesMultimedia.add(i ,
                    Multimedia(
                        name =  districtRemote.galleryImages?.get(i)?.name,
                        description = districtRemote.galleryImages?.get(i)?.description,
                        width = districtRemote.galleryImages?.get(i)?.width,
                        height = districtRemote.galleryImages?.get(i)?.height,
                        size = districtRemote.galleryImages?.get(i)?.size,
                        id = districtRemote.galleryImages?.get(i)?.id,
                        url = districtRemote.galleryImages?.get(i)?.url
                    ))
            }



        val poiList = mutableListOf<Pois>()
        val poiListSize = districtRemote.pois?.size ?:0
            for(i in 0 .. poiListSize){

                val poiImageMultimedia = Multimedia(
                    name = districtRemote.pois?.get(i)?.image?.name,
                    description = districtRemote.pois?.get(i)?.image?.description,
                    width = districtRemote.pois?.get(i)?.image?.width,
                    height = districtRemote.pois?.get(i)?.image?.height,
                    size = districtRemote.pois?.get(i)?.image?.size,
                    id = districtRemote.pois?.get(i)?.image?.id,
                    url = districtRemote.pois?.get(i)?.image?.url
                )

                val poiVideoMultimedia = Multimedia(
                    name = districtRemote.pois?.get(i)?.video?.name,
                    description = districtRemote.pois?.get(i)?.video?.description,
                    width = districtRemote.pois?.get(i)?.video?.width,
                    height = districtRemote.pois?.get(i)?.video?.height,
                    size = districtRemote.pois?.get(i)?.video?.size,
                    id = districtRemote.pois?.get(i)?.video?.id,
                    url = districtRemote.pois?.get(i)?.video?.url
                )

                val poiAudioMultimedia = Multimedia(
                    name = districtRemote.pois?.get(i)?.audio?.name,
                    description = districtRemote.pois?.get(i)?.audio?.description,
                    width = districtRemote.pois?.get(i)?.audio?.width,
                    height = districtRemote.pois?.get(i)?.audio?.height,
                    size = districtRemote.pois?.get(i)?.audio?.size,
                    id = districtRemote.pois?.get(i)?.audio?.id,
                    url = districtRemote.pois?.get(i)?.audio?.url
                )

                poiList.add(i , Pois(
                    likesCount = districtRemote.pois?.get(i)?.likesCount,
                    eventsCount = districtRemote.pois?.get(i)?.eventsCount,
                    newsCount = districtRemote.pois?.get(i)?.newsCount,
                    id = districtRemote.pois?.get(i)?.id,
                    image = poiImageMultimedia,
                    galleryImages = null,
                    latitude = districtRemote.pois?.get(i)?.latitude,
                    longitude = districtRemote.pois?.get(i)?.longitude,
                    category = null,
                    premium = districtRemote.pois?.get(i)?.premium,
                    name = districtRemote.pois?.get(i)?.name,
                    description = districtRemote.pois?.get(i)?.description,
                    video = poiVideoMultimedia,
                    audio= poiAudioMultimedia,
                    likeIt = districtRemote.pois?.get(i)?.likeIt
                ))
            }

        return  MutableStateFlow( District(
            poisCount = districtRemote.poisCount,
            id = districtRemote.id,
            name = districtRemote.name,
            image = imageMultimedia,
            galleryImages = galleryImagesMultimedia,
            coordinates = districtRemote.coordinates,
            video = videoMultimedia,
            audio = audioMultimedia,
            pois = poiList))
    }
}

