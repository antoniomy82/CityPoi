package com.antoniomy.data.model

data class DistrictDto (
    var poisCount: Int? = null,
    var id: Int? = null,
    var name: String? = null,
    var image: MultimediaDto? = null,
    var galleryImages: ArrayList<MultimediaDto>? = null,
    var coordinates: String? = null,
    var video: MultimediaDto? = null,
    var audio: MultimediaDto? = null,
    var pois: List<PoisDto>? = null
)
