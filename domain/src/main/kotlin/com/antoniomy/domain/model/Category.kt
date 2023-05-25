package com.antoniomy.domain.model

import com.antoniomy.data.model.MultimediaRemote

data class Category(
    var id: Int? = null,
    var icon: Multimedia? = null,
    var marker: Multimedia? = null,
    var markerIcon: Multimedia? = null,
    var name: String? = null
)
