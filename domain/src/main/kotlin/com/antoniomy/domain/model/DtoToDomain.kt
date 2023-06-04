package com.antoniomy.domain.model

import com.antoniomy.data.model.DistrictDto
import com.antoniomy.data.model.PoisDto

fun DistrictDto.toDomain(): District = District(name = name, pois = pois?.toDomain())

fun List<PoisDto>.toDomain(): List<Poi> {

    val poiList = mutableListOf<Poi>()

    for (i in indices) {
        poiList.add(
            i, Poi(
                image = this[i].image?.url,
                latitude = this[i].latitude,
                longitude = this[i].longitude,
                categoryIcon = this[i].category?.icon?.url,
                categoryMarker = this[i].category?.marker?.url,
                name = this[i].name,
                description = this[i].description,
                audio = this[i].audio?.url,
            )
        )
    }
    return poiList
}

fun Poi.toData(): PoisDto = PoisDto(
    imageLocal = this.image,
    latitude = this.latitude,
    longitude = this.longitude,
    categoryIcon = this.categoryIcon,
    categoryMarker = this.categoryMarker,
    name = this.name,
    description = this.description,
    audioLocal =this.audio,
    city = this.city,
    district = this.district
)

