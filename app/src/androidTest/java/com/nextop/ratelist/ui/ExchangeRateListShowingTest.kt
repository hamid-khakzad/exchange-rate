package com.nextop.ratelist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.nextop.ratelist.AndroidMainCoroutinesRule
import com.nextop.ratelist.R
import com.nextop.ratelist.data.local.ExchangeRate
import com.nextop.ratelist.ui.exchangerate.ExchangeRateAdapter
import com.nextop.ratelist.ui.exchangerate.ExchangeRateFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class ExchangeRateListShowingTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var coroutinesRule = AndroidMainCoroutinesRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun isExchangeRateListVisible() {
        // Launch fragment
        val fragmentScenario: FragmentScenario<ExchangeRateFragment> = launchFragmentInContainer()

        fragmentScenario.onFragment { fragment ->
            val context = fragment.requireContext()
            //create mock data for exchange rate list
            val data = listOf(
                ExchangeRate("EURUSD", 0.64039843048),
                ExchangeRate("GBPJPY", 0.480755037),
                ExchangeRate("USDCAD", 1.10475367813),
                ExchangeRate("JPYAED", 0.123376524),
                ExchangeRate("JPYSEK", 0.18337620324)
            )
            val exchangeRateRec: RecyclerView = fragment.view?.findViewById(R.id.currency_rates_rec)
                ?: throw IllegalStateException("RecyclerView not found")
            val adapter = ExchangeRateAdapter(context)
            exchangeRateRec.adapter = adapter
            adapter.submitList(data)
        }

        onView(withId(R.id.exchange_rate_fragment)).check(matches(isDisplayed()))
    }

}




















