package com.antoniomy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
@Entity(tableName = "Poi")
data class PoisDto(
    @ColumnInfo(name = "IMAGE_LOCAL") var imageLocal: String? = null, //URL
    @ColumnInfo(name = "LATITUDE") var latitude: Float? = null,
    @ColumnInfo(name = "LONGITUDE") var longitude: Float? = null,
    @ColumnInfo(name = "CATEGORY_ICON") var categoryIcon: String? = null, //ICON Y MARKER
    @ColumnInfo(name = "CATEGORY_MARKER") var categoryMarker: String? = null, //ICON Y MARKER
    @PrimaryKey @ColumnInfo(name = "NAME") var name: String = "",
    @ColumnInfo(name = "DESCRIPTION") var description: String? = null,
    @ColumnInfo(name = "AUDIO_LOCAL") var audioLocal: String? = null,  //URL
    @ColumnInfo(name = "CITY") var city: String? = null,
    @ColumnInfo(name = "DISTRICT") var district: String? = null,


){
    @Ignore var image: MultimediaDto? = null //URL
    @Ignore var category: CategoryDto? = null //ICON Y MARKER
    @Ignore var audio: MultimediaDto? = null  //URL
}

