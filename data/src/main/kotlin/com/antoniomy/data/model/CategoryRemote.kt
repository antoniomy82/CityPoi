package com.antoniomy.domain.model

import com.antoniomy.data.model.MultimediaRemote

data class CategoryRemote(
    var id: Int? = null,
    var icon: MultimediaRemote? = null,
    var marker: MultimediaRemote? = null,
    var markerIcon: MultimediaRemote? = null,
    var name: String? = null
)
