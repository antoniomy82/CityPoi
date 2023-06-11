package com.antoniomy.citypoi.carousel

import com.antoniomy.domain.model.Poi

data class CarouselModel(
    val carouselCards: List<Poi>,
    val actionPrimaryButton: (() -> Unit)? = null,
    val primaryButtonText: Int? = null, //Default name : Next
    val actionSecondaryButton: (() -> Unit)? = null,
    val secondaryButtonText: Int? = null, //Default name : Skip
)