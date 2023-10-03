package com.nextop.ratelist.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.nextop.ratelist.R
import com.nextop.ratelist.launchFragmentInHiltContainer
import com.nextop.ratelist.ui.exchangerate.ExchangeRateFragment
import dagger.hilt.android.testing.HiltAndroidTest

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExchangeRateFragmentDisplayTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSample() {
        launchFragmentInHiltContainer<ExchangeRateFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.currency_rate_title_tv)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            ))

        Espresso.onView(ViewMatchers.withId(R.id.currency_rates_rec)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            ))

        Espresso.onView(ViewMatchers.withId(R.id.latest_update_date_tv)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )

    }
}