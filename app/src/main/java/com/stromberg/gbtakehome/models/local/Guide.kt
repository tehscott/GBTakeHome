package com.stromberg.gbtakehome.models.local

data class Guide(
    val startDate: String,
    val endDate: String,
    val name: String,
    val url: String,
    val venue: Venue?,
    val icon: String,
) {
    data class Venue(
        val city: String?,
        val state: String?,
    )
}