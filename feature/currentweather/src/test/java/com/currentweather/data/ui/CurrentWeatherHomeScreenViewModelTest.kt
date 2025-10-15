import app.cash.turbine.test
import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.currentweather.data.repository.SearchRepository
import com.currentweather.ui.CurrentWeatherHomeScreenViewModel
import com.currentweather.ui.ErrorType
import com.currentweather.ui.WeatherUIState
import com.mahshad.common.model.error.RepositoryError
import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import com.mahshad.datasource.model.search.Search
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentWeatherHomeScreenViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var currentWeatherRepository: CurrentWeatherRepository

    @MockK
    lateinit var forecastRepository: ForecastRepository

    @MockK
    lateinit var locationRepository: LocationRepository

    @MockK
    lateinit var searchRepository: SearchRepository
    private val _searchLocation: MutableStateFlow<String> = MutableStateFlow("")
    private val _locationPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _locationEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _requestLocationPermissions: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private lateinit var viewModel: CurrentWeatherHomeScreenViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrentWeatherHomeScreenViewModel(
            currentWeatherRepository,
            forecastRepository,
            locationRepository,
            searchRepository
        )
    }

    @Test
    fun `observeSearchLocation_multipleFastAndRepetitiveQueriesEmitted_onlyLastOneProcessed`() =
        runTest {
            // GIVEN
            coEvery { searchRepository.searchLocation(any()) } returns
                    Result.success(listOf(Search.DEFAULT))
            // WHEN
            viewModel.observeSearchLocation()
            // GIVEN
            viewModel.updateSearchLocation("New")
            testScheduler.advanceTimeBy(30.milliseconds)
            viewModel.updateSearchLocation("New Y")
            testScheduler.advanceTimeBy(30.milliseconds)
            viewModel.updateSearchLocation("New York")
            testScheduler.advanceTimeBy(300.milliseconds)
            viewModel.updateSearchLocation("New York")
            testScheduler.advanceTimeBy(300.milliseconds)
            testScheduler.advanceUntilIdle()
            // THEN
            coVerify(exactly = 1) { searchRepository.searchLocation("New York") }
            coVerify(exactly = 0) { searchRepository.searchLocation("New") }
            coVerify(exactly = 0) { searchRepository.searchLocation("New Y") }
        }

    @Test
    fun `searchLocationApiCall_whenRepositoryReturnsSuccess_thenSearchLocationResultsEmitsNonEmptyList`() =
        runTest {
            // GIVEN
            coEvery { searchRepository.searchLocation("") } returns
                    Result.success(listOf(Search.DEFAULT))
            // THEN
            viewModel.searchLocationResults.test {
                assertEquals(emptyList<String>(), awaitItem())
                cancel()
            }
            // WHEN
            viewModel.searchLocationApiCall("")
            testScheduler.advanceUntilIdle()
            // THEN
            viewModel.searchLocationResults.test {
                assertEquals(listOf(", "), awaitItem())
                cancel()
            }
        }

    @Test
    fun `startLocationWeatherUpdates_whenTrackingOff_thenLocationServicesDisabledError`() =
        runTest {
            // GIVEN
            coEvery { locationRepository.isLocationEnabled() } returns flow { emit(false) }
            coEvery { locationRepository.hasLocationPermissions() } returns true
            // WHEN
            viewModel.startLocationWeatherUpdates()
            testScheduler.advanceUntilIdle()
            // THEN
            viewModel.weatherUIState.test {
                assertEquals(
                    WeatherUIState.Error(errorType = ErrorType.LocationServicesDisabled),
                    awaitItem()
                )
                cancel()
            }
        }

    @Test
    fun `startLocationWeatherUpdates_whenTrackingOnButPermissionOff_thenLocationPermissionDeniedError`() =
        runTest {
            // GIVEN
            coEvery { locationRepository.isLocationEnabled() } returns flow { emit(true) }
            coEvery { locationRepository.hasLocationPermissions() } returns false
            // WHEN
            viewModel.startLocationWeatherUpdates()
            testScheduler.advanceUntilIdle()
            // THEN
            viewModel.weatherUIState.test {
                assertEquals(
                    WeatherUIState.Error(errorType = ErrorType.LocationPermissionDenied),
                    awaitItem()
                )
                cancel()
            }

        }

    @Test
    fun `startLocationWeatherUpdates_whenLocationServiceAndPermissionAreOn_thenWeatherUIStateIsSuccessful`() =
        runTest {
            // GIVEN
            val DEFAULT_LATITUDE = 40.7128
            val DEFAULT_LONGITUDE = -74.0060
            val location = mockk<android.location.Location>(relaxed = true) {
                every { latitude } returns DEFAULT_LATITUDE
                every { longitude } returns DEFAULT_LONGITUDE
                every { provider } returns "mock_provider"
            }
            coEvery { locationRepository.isLocationEnabled() } returns flow { emit(true) }
            coEvery { locationRepository.hasLocationPermissions() } returns true
            coEvery { locationRepository.getLocationUpdates() } returns flow {
                emit(location)
            }
            coEvery { currentWeatherRepository.getCurrentWeather(any(), "no") } returns
                    Result.success(CurrentWeather.DEFAULT)
            coEvery {
                forecastRepository.getForecast(
                    any(),
                    1,
                    false,
                    false
                )
            } returns Forecast.DEFAULT
            // WHEN
            viewModel.startLocationWeatherUpdates()
            testScheduler.advanceUntilIdle()
            // THEN
            coVerify(exactly = 1) { locationRepository.isLocationEnabled() }
            coVerify(exactly = 1) { locationRepository.hasLocationPermissions() }
            viewModel.weatherUIState.test {
                assertEquals(
                    true,
                    awaitItem() is WeatherUIState.Success
                )
            }
            val expectedLocationString = "$DEFAULT_LATITUDE,$DEFAULT_LONGITUDE"
            coVerify(exactly = 1) {
                currentWeatherRepository.getCurrentWeather(
                    eq(expectedLocationString),
                    eq("no")
                )
            }
            coVerify(exactly = 1) {
                forecastRepository.getForecast(
                    eq(expectedLocationString), 1, false, false
                )
            }
        }

    @Test
    fun `startLocationWeatherUpdates_whenRepositoryGivesNetworkError_thenWeatherUIStateIsError`() =
        runTest {
            // GIVEN
            val DEFAULT_LATITUDE = 40.7128
            val DEFAULT_LONGITUDE = -74.0060
            val location = mockk<android.location.Location>(relaxed = true) {
                every { latitude } returns DEFAULT_LATITUDE
                every { longitude } returns DEFAULT_LONGITUDE
                every { provider } returns "mock_provider"
            }
            coEvery { locationRepository.isLocationEnabled() } returns flow { emit(true) }
            coEvery { locationRepository.hasLocationPermissions() } returns true
            coEvery { locationRepository.getLocationUpdates() } returns flow {
                emit(location)
            }
            coEvery { currentWeatherRepository.getCurrentWeather(any(), "no") } returns
                    Result.failure(RepositoryError.NetworkError(404))
            coEvery {
                forecastRepository.getForecast(
                    any(),
                    1,
                    false,
                    false
                )
            } returns Forecast.DEFAULT
            // WHEN
            viewModel.startLocationWeatherUpdates()
            testScheduler.advanceUntilIdle()
            // THEN
            viewModel.weatherUIState.test {
                assertEquals(
                    WeatherUIState.Error(errorType = ErrorType.NetworkError),
                    awaitItem()
                )
            }
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

}
