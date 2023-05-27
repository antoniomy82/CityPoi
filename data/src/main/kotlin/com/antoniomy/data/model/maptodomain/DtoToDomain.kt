package com.antoniomy.data.model.maptodomain

import com.antoniomy.data.model.CategoryDto
import com.antoniomy.data.model.DistrictDto
import com.antoniomy.data.model.MultimediaDto
import com.antoniomy.data.model.PoisDto
import com.antoniomy.domain.model.Category
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.Multimedia
import com.antoniomy.domain.model.Pois
import com.google.gson.GsonBuilder

inline fun <reified T : Any> Any.mapTo(): T = GsonBuilder().create().run { fromJson(toJson(this@mapTo), T::class.java) }

fun toPoisList(listPoisDto: List<PoisDto>): List<Pois> {

    val poiList = mutableListOf<Pois>()

    for (i in 0..listPoisDto.size) {
        val fetchList = listPoisDto[i]
        poiList.add(
            i, Pois(
                likesCount = fetchList.likesCount,
                eventsCount = fetchList.eventsCount,
                newsCount = fetchList.newsCount,
                id = fetchList.id,
                image = fetchList.image?.toDomain(),
                galleryImages = fetchList.galleryImages?.let { toListMultimedia(it) },
                latitude = fetchList.latitude,
                longitude = fetchList.longitude,
                category = fetchList.categoryDto?.toDomain(),
                premium = fetchList.premium,
                name = fetchList.name,
                description = fetchList.description,
                video = fetchList.video?.toDomain(),
                audio = fetchList.audio?.toDomain(),
                likeIt = fetchList.likeIt
            )
        )
    }
    return poiList
}

fun CategoryDto.toDomain(): Category =
    mapTo<Category>().copy(
        id = id,
        icon = icon?.toDomain(),
        marker = marker?.toDomain(),
        markerIcon = markerIcon?.toDomain(),
        name = "$name"
    )

fun MultimediaDto.toDomain(): Multimedia =
    mapTo<Multimedia>().copy(
        name = "$name",
        description = "$description",
        width = "$width",
        height = "$height",
        size = size,
        id = id,
        url = "$url",
    )

fun toListMultimedia(listMultimediaDto: List<MultimediaDto>): List<Multimedia> {
    val multimediaList = mutableListOf<Multimedia>()
    for (i in 0..listMultimediaDto.size) {
        multimediaList.add(i, listMultimediaDto[i].toDomain())
    }
    return multimediaList
}

fun DistrictDto.toDomain(): District =
    mapTo<District>().copy(
        poisCount = poisCount,
        id = id,
        name = name,
        image = image?.toDomain(),
        galleryImages = galleryImages?.let { toListMultimedia(it) },
        coordinates = coordinates,
        video = video?.toDomain(),
        audio = audio?.toDomain(),
        pois = pois?.let { toPoisList(it) }
    )