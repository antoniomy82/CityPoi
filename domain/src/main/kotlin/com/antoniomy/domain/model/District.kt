package com.antoniomy.domain.model

import com.antoniomy.data.model.MultimediaRemote
import com.antoniomy.data.model.PoisRemote

data class District (
    var poisCount: Int? = null,
    var id: Int? = null,
    var name: String? = null,
    var image: Multimedia? = null,
    var galleryImages: List<Multimedia>? = null,
    var coordinates: String? = null,
    var video: Multimedia? = null,
    var audio: Multimedia? = null,
    var pois: List<Pois>? = null
)
