package com.stromberg.gbtakehome.utils

import com.stromberg.gbtakehome.models.local.Guide
import com.stromberg.gbtakehome.models.remote.GuideRemote

fun GuideRemote.toDomain(): Guide =
    Guide(
        startDate = this.startDate,
        endDate = this.endDate,
        name = this.name,
        url = this.url,
        venue = this.venue?.toDomain(),
        icon = this.icon,
    )

fun GuideRemote.Venue.toDomain(): Guide.Venue =
    Guide.Venue(
        city = this.city,
        state = this.state,
    )