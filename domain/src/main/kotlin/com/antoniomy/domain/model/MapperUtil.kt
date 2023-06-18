package com.antoniomy.domain.model

import com.antoniomy.data.model.DistrictDto
import com.antoniomy.data.model.PoisDto

fun DistrictDto.toDomain(): District = District(name = name, pois = pois?.toDomain())

fun List<PoisDto>.toDomain(): List<Poi> {

    val poiList = mutableListOf<Poi>()

    for (i in indices) {
        poiList.add(
            i, Poi(
                image = if(this[i].image?.url==null) this[i].imageLocal else this[i].image?.url ,
                latitude = this[i].latitude,
                longitude = this[i].longitude,
                categoryIcon = if(this[i].category?.icon?.url ==null) this[i].categoryIcon else this[i].category?.icon?.url,
                categoryMarker = if(this[i].category?.marker?.url == null) this[i].categoryMarker else this[i].category?.marker?.url,
                name = this[i].name,
                description = this[i].description,
                audio = if(this[i].audio?.url == null) this[i].audioLocal else this[i].audio?.url,
                city = this[i].city,
                district = this[i].district
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

