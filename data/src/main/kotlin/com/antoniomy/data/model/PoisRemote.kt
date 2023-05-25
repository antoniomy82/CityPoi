package com.antoniomy.data.model

import com.antoniomy.domain.model.CategoryRemote

data class PoisRemote(
    var likesCount: Int? = null,
    var eventsCount: Int? = null,
    var newsCount: Int? = null,
    var id: Int? = null,
    var image: MultimediaRemote? = null,
    var galleryImages: List<MultimediaRemote>? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var category: CategoryRemote? = null,
    var premium: Boolean? = null,
    var name: String? = null,
    var description: String? = null,
    var video: MultimediaRemote? = null,
    var audio: MultimediaRemote? = null,
    var likeIt: Boolean? = null
)

