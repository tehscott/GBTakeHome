package com.stromberg.gbtakehome.network

import com.stromberg.gbtakehome.models.remote.GuidesRemote
import retrofit2.Call
import retrofit2.http.GET

interface GuideService {
    @GET("upcomingGuides/")
    fun getGuides(): Call<GuidesRemote>

    companion object {
        const val BASE_URL = "https://guidebook.com/service/v2/"
    }
}