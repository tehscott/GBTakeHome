package com.stromberg.gbtakehome

import com.stromberg.gbtakehome.interfaces.GuideHelper
import javax.inject.Inject

class GuideRepository @Inject constructor(
    private val guideHelper: GuideHelper
) {
    suspend fun getGuides() = guideHelper.getGuides()
}