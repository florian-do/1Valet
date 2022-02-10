package com.do_f.a1valet

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airbnb.epoxy.EpoxyRecyclerView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * set networkType to ROOM before running those test
 */

@RunWith(AndroidJUnit4::class)
class DeviceHomeTest {

    @ExperimentalPagingApi
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun showDevices() {
        onView(withId(R.id.rvFeed)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as EpoxyRecyclerView
            Assert.assertEquals(12, recyclerView.adapter?.itemCount)
        }
    }

    @Test
    fun testFilter() {
        onView(withId(R.id.toolbar_filter)).perform(click())
        onView(withId(R.id.rvFeed)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        onView(withId(R.id.rvFeed)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as EpoxyRecyclerView
            Assert.assertEquals(5, recyclerView.adapter?.itemCount)
        }
    }

    @Test
    fun testSearch() {
        fun typeSearchViewText(text: String): ViewAction {
            return object : ViewAction {
                override fun getDescription(): String {
                    return "Change view text"
                }

                override fun getConstraints(): Matcher<View> {
                    return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
                }

                override fun perform(uiController: UiController?, view: View?) {
                    (view as SearchView).setQuery(text, true)
                }
            }
        }
        onView(withId(R.id.simpleSearchView)).perform(typeSearchViewText("pixel"))
        onView(withId(R.id.rvFeed)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as EpoxyRecyclerView
            Assert.assertEquals(2, recyclerView.adapter?.itemCount)
        }
    }

    @Test
    fun showDevice() {
        onView(withId(R.id.rvFeed)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))
        onView(withId(R.id.device_title)).check(matches(withText("iPhone 13")))
    }
}