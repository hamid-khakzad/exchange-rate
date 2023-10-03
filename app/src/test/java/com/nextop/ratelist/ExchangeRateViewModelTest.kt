import androidx.lifecycle.Observer
import com.nextop.ratelist.data.local.ExchangeRate
import com.nextop.ratelist.data.remote.ExchangeRateResponse
import com.nextop.ratelist.data.repository.ExchangeRateRepository
import com.nextop.ratelist.ui.exchangerate.ExchangeRateViewModel
import com.nextop.ratelist.util.NetworkUtil
import com.nextop.ratelist.util.Resource
import com.nextop.ratelist.util.StringResourcesProvider
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class ExchangeRateViewModelTest {


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var viewModel: ExchangeRateViewModel
    private val stringResourcesProvider = mockk<StringResourcesProvider>(relaxed = true)
    private val exchangeRateRepository = mockk<ExchangeRateRepository>()
    private val networkUtil = mockk<NetworkUtil>()

    @Before
    fun setUp() {
        viewModel = ExchangeRateViewModel(
            stringResourcesProvider,
            exchangeRateRepository,
            networkUtil
        )
    }

    @Test
    fun `given network error, when getExchangeRate, then post network error`() {
        runBlocking {
            // Given
            val errorObserver = mockk<Observer<Resource<ExchangeRateResponse>>>(relaxed = true)
            viewModel.exchangeRate.observeForever(errorObserver)
            coEvery { networkUtil.hasInternetConnection() } returns false

            // When
            viewModel.getExchangeRate()

            // Then
            val errorMsg = "No internet connection" // Replace with your actual error message
            verify { errorObserver.onChanged(Resource.Error(errorMsg)) }
            confirmVerified(errorObserver)
        }
    }

    @Test
    fun `given network success and data fetch success, when getExchangeRate, then post success`() {
        runBlocking {
            // Given
            val successObserver =
                mockk<Observer<Resource<ExchangeRateResponse>>>(relaxed = true)
            viewModel.exchangeRate.observeForever(successObserver)
            val mockedResponse = mockk<Response<ExchangeRateResponse>>(relaxed = true)
            val data = ExchangeRateResponse(
                listOf(
                    ExchangeRate("EURUSD", 0.64039843048),
                    ExchangeRate("GBPJPY", 0.480755037),
                    ExchangeRate("USDCAD", 1.10475367813),
                    ExchangeRate("JPYAED", 0.123376524),
                    ExchangeRate("JPYSEK", 0.18337620324)

                )
            )
            coEvery { networkUtil.hasInternetConnection() } returns true
            coEvery { exchangeRateRepository.getExchangeRates() } returns mockedResponse
            every { mockedResponse.isSuccessful } returns true
            every { mockedResponse.body() } returns data

            // When
            viewModel.getExchangeRate()

            // Then
            verify { successObserver.onChanged(Resource.Success(data)) }
            confirmVerified(successObserver)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }
}