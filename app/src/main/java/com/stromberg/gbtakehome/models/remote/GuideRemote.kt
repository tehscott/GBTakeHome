package com.stromberg.gbtakehome.models.remote

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class GuideRemote(
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

    companion object {
        fun generateTestGuide(): GuideRemote {
            return GuideRemote(
                startDate = UUID.randomUUID().toString(),
                endDate = UUID.randomUUID().toString(),
                name = UUID.randomUUID().toString(),
                url = UUID.randomUUID().toString(),
                venue = Venue(
                    city = UUID.randomUUID().toString(),
                    state = UUID.randomUUID().toString(),
                ),
                icon = UUID.randomUUID().toString(),
            )
        }
    }
}

data class GuidesRemote(
    @SerializedName("data")
    val guides: List<GuideRemote>
)