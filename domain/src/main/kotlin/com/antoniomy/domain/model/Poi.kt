package com.antoniomy.domain.model

data class Poi(
    var image: String? = null,
    var latitude: Float? = null,
    var longitude: Float? = null,
    var categoryIcon: String? = null,
    var categoryMarker: String? = null,
    var name: String = "",
    var description: String? = null,
    var audio: String? = null,
    var city: String? = null,
    var district: String? = null
)

