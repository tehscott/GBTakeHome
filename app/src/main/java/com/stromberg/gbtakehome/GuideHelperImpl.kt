package com.stromberg.gbtakehome

import com.stromberg.gbtakehome.interfaces.GuideHelper
import com.stromberg.gbtakehome.models.local.Guide
import com.stromberg.gbtakehome.network.GuideService
import com.stromberg.gbtakehome.utils.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GuideHelperImpl @Inject constructor(
    private val guideService: GuideService
) : GuideHelper {
    override suspend fun getGuides(): Flow<List<Guide>?> {
        var guides: List<Guide>? = null

        runCatching {
            guideService.getGuides().execute().let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        runCatching {
                            guides = responseBody.guides.map { it.toDomain() }
                        }
                    }
                }
            }
        }.getOrNull()

        return flowOf(guides)
    }
}