package com.antoniomy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Poi")
data class PoisDto(
    @ColumnInfo(name = "IMAGE") var image: MultimediaDto? = null, //URL
    @ColumnInfo(name = "LATITUDE") var latitude: Float? = null,
    @ColumnInfo(name = "LONGITUDE") var longitude: Float? = null,
    @ColumnInfo(name = "CATEGORY") var category: CategoryDto? = null, //ICON Y MARKER
    @PrimaryKey @ColumnInfo(name = "NAME") var name: String? = null,
    @ColumnInfo(name = "DESCRIPTION") var description: String? = null,
    @ColumnInfo(name = "AUDIO") var audio: MultimediaDto? = null,  //URL
    @ColumnInfo(name = "CITY") var city: String? = null,
    @ColumnInfo(name = "DISTRICT") var district: String? = null
)

