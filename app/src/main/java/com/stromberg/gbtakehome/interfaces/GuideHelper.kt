package com.stromberg.gbtakehome.interfaces

import com.stromberg.gbtakehome.models.local.Guide
import kotlinx.coroutines.flow.Flow

interface GuideHelper {
    suspend fun getGuides(): Flow<List<Guide>?>
}