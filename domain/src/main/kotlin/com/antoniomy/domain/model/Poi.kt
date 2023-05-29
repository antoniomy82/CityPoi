package com.antoniomy.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Poi")
data class Poi(
    @ColumnInfo(name = "IMAGE") var image: String? = null, //URL
    @ColumnInfo(name = "LATITUDE") var latitude: Float? = null,
    @ColumnInfo(name = "LONGITUDE") var longitude: Float? = null,
    @ColumnInfo(name = "CATEGORY_ICON") var categoryIcon: String? = null, //ICON Y MARKER
    @ColumnInfo(name = "CATEGORY_MARKER") var categoryMarker: String? = null, //ICON Y MARKER
    @PrimaryKey @ColumnInfo(name = "NAME") var name: String? = null,
    @ColumnInfo(name = "DESCRIPTION") var description: String? = null,
    @ColumnInfo(name = "AUDIO") var audio: String? = null,  //URL
    @ColumnInfo(name = "CITY") var city: String? = null,
    @ColumnInfo(name = "DISTRICT") var district: String? = null
)

