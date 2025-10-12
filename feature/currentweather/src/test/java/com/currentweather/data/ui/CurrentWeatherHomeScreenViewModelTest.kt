import app.cash.turbine.test
import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.currentweather.data.repository.SearchRepository
import com.currentweather.ui.CurrentWeatherHomeScreenViewModel
import com.mahshad.datasource.model.search.Search
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private lateinit var viewModel: CurrentWeatherHomeScreenViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrentWeatherHomeScreenViewModel(
            currentWeatherRepository,
            forecastRepository,
            locationRepository,
            searchRepository,
            _searchLocation
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `observeSearchLocation_multiple queries are emitted fast and repetitive_only the last one is processed`() =
        runTest {
            // GIVEN
            coEvery { searchRepository.searchLocation(any()) } returns
                    Result.success(listOf(Search.DEFAULT))
            // WHEN
            viewModel.observeSearchLocation()
            // GIVEN
            _searchLocation.emit("New")
            testScheduler.advanceTimeBy(30.milliseconds)
            _searchLocation.emit("New Y")
            testScheduler.advanceTimeBy(30.milliseconds)
            _searchLocation.emit("New York")
            testScheduler.advanceTimeBy(300.milliseconds)
            _searchLocation.emit("New York")
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


}
