package com.antoniomy.data.model

data class CategoryDto(
    var id: Int? = null,
    var icon: MultimediaDto? = null,
    var marker: MultimediaDto? = null,
    var markerIcon: MultimediaDto? = null,
    var name: String? = null
)
