package com.antoniomy.citypoi.common

import com.antoniomy.domain.model.Poi

interface PoiProvider {
    fun setPoi(poi: Poi)
    fun getPoi(): Poi
}

class PoiProviderImpl : PoiProvider {
    private lateinit var mPoi: Poi
    override fun setPoi(poi: Poi) { this.mPoi = poi }
    override fun getPoi() = mPoi
}