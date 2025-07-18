package com.jiyeon.easypicktrip.response.geocode

data class GeocodeResponseItem(
    val name: String,
    val local_names: LocalNames,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String
)