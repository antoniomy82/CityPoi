package com.antoniomy.domain.model

data class Poi(
    var image: String? = null, //URL
    var latitude: Float? = null,
    var longitude: Float? = null,
    var categoryIcon: String? = null, //ICON Y MARKER
    var categoryMarker: String? = null, //ICON Y MARKER
    var name: String? = null,
    var description: String? = null,
    var audio: String? = null,  //URL
    var city: String? = null,
    var district: String? = null
)

