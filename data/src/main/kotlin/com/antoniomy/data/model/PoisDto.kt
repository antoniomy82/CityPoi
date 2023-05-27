package com.antoniomy.data.model

data class PoisDto(
    var likesCount: Int? = null,
    var eventsCount: Int? = null,
    var newsCount: Int? = null,
    var id: Int? = null,
    var image: MultimediaDto? = null,
    var galleryImages: ArrayList<MultimediaDto>? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var categoryDto: CategoryDto? = null,
    var premium: Boolean? = null,
    var name: String? = null,
    var description: String? = null,
    var video: MultimediaDto? = null,
    var audio: MultimediaDto? = null,
    var likeIt: Boolean? = null
)

