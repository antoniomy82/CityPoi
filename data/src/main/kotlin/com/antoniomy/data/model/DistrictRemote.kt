package com.antoniomy.domain.model

import com.antoniomy.data.model.MultimediaRemote
import com.antoniomy.data.model.PoisRemote

data class DistrictRemote (
    var poisCount: Int? = null,
    var id: Int? = null,
    var name: String? = null,
    var image: MultimediaRemote? = null,
    var galleryImages: List<MultimediaRemote>? = null,
    var coordinates: String? = null,
    var video: MultimediaRemote? = null,
    var audio: MultimediaRemote? = null,
    var pois: List<PoisRemote>? = null
)
