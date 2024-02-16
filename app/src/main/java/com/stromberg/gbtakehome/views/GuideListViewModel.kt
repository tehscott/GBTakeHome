package com.stromberg.gbtakehome.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stromberg.gbtakehome.GuideRepository
import com.stromberg.gbtakehome.models.local.Guide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideListViewModel @Inject constructor(
    private val guideRepository: GuideRepository
) : ViewModel() {
    val guides: MutableLiveData<List<Guide>?> by lazy {
        MutableLiveData<List<Guide>?>()
    }

    fun fetchGuides(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = CoroutineScope(dispatcher).launch {
        guideRepository.getGuides()
            .onEach { value ->
                guides.postValue(value)
            }
            .collect()
    }
}