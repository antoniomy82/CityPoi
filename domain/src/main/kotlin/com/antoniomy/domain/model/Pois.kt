package com.antoniomy.domain.model

import com.antoniomy.data.model.MultimediaRemote

data class Pois(
    var likesCount: Int? = null,
    var eventsCount: Int? = null,
    var newsCount: Int? = null,
    var id: Int? = null,
    var image: Multimedia? = null,
    var galleryImages: List<Multimedia>? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var category: Category? = null,
    var premium: Boolean? = null,
    var name: String? = null,
    var description: String? = null,
    var video: Multimedia? = null,
    var audio: Multimedia? = null,
    var likeIt: Boolean? = null
)

