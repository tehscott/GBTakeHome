package com.stromberg.gbtakehome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.stromberg.gbtakehome.models.local.Guide
import com.stromberg.gbtakehome.models.remote.GuideRemote
import com.stromberg.gbtakehome.models.remote.GuidesRemote
import com.stromberg.gbtakehome.network.GuideService
import com.stromberg.gbtakehome.utils.toDomain
import com.stromberg.gbtakehome.views.GuideListViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GuideListViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: GuideListViewModel

    @Mock private lateinit var mockService: GuideService
    @Mock private lateinit var mockObserver: Observer<List<Guide>?>
    private val mainDispatcher = StandardTestDispatcher()
    private val ioDispatcher = StandardTestDispatcher()

    private val goodGuidesRemote = listOf(
        GuideRemote.generateTestGuide(),
        GuideRemote.generateTestGuide(),
        GuideRemote.generateTestGuide(),
        GuideRemote.generateTestGuide(),
        GuideRemote.generateTestGuide(),
    )
    private val goodGuides = goodGuidesRemote.map { it.toDomain() }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(mainDispatcher)

        mockService = mock(GuideService::class.java, RETURNS_DEEP_STUBS)

        viewModel = GuideListViewModel(
            GuideRepository(
                GuideHelperImpl(mockService)
            )
        )

        viewModel.guides.observeForever(mockObserver)
    }

    @After
    fun close() {
        viewModel.guides.removeObserver(mockObserver)
        Dispatchers.shutdown()
    }

    @Test
    fun test_guides_posted() {
        val mockResponse = Response.success<GuidesRemote>(GuidesRemote(goodGuidesRemote))
        `when`(mockService.getGuides().execute()).thenReturn(mockResponse)

        viewModel.fetchGuides(ioDispatcher)

        mainDispatcher.scheduler.advanceUntilIdle()
        ioDispatcher.scheduler.advanceUntilIdle()

        verify(mockObserver).onChanged(goodGuides)

        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun test_null_response() {
        val mockResponse = Response.success<GuidesRemote>(null)
        `when`(mockService.getGuides().execute()).thenReturn(mockResponse)

        viewModel.fetchGuides(ioDispatcher)

        mainDispatcher.scheduler.advanceUntilIdle()
        ioDispatcher.scheduler.advanceUntilIdle()

        // Posted null, which means an error
        verify(mockObserver).onChanged(null)

        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun test_exception() {
        `when`(mockService.getGuides().execute()).thenThrow()

        viewModel.fetchGuides(ioDispatcher)

        mainDispatcher.scheduler.advanceUntilIdle()
        ioDispatcher.scheduler.advanceUntilIdle()

        // Posted null, which means an error
        verify(mockObserver).onChanged(null)

        verifyNoMoreInteractions(mockObserver)
    }

    @Test
    fun test_http_error() {
        val mockResponse = Response.error<GuidesRemote>(400, mock())
        `when`(mockService.getGuides().execute()).thenReturn(mockResponse)

        viewModel.fetchGuides(ioDispatcher)

        mainDispatcher.scheduler.advanceUntilIdle()
        ioDispatcher.scheduler.advanceUntilIdle()

        // Posted null, which means an error
        verify(mockObserver).onChanged(null)

        verifyNoMoreInteractions(mockObserver)
    }
}