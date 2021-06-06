package com.applications.toms.depormas


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InitialEmptyStateRecorded {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_COARSE_LOCATION")

    @Test
    fun initialEmptyStateRecorded() {
        val materialTextView = onView(
            allOf(withId(R.id.onboardingOneBtn), withText("Siguiente"),
            childAtPosition(
            childAtPosition(
            withClassName(`is`("android.widget.FrameLayout")),
            0),
            3),
            isDisplayed()))
        materialTextView.perform(click())
        
        val materialTextView2 = onView(
            allOf(withId(R.id.onboardingTwoBtn), withText("Siguiente"),
            childAtPosition(
            childAtPosition(
            withClassName(`is`("android.widget.FrameLayout")),
            0),
            3),
            isDisplayed()))
        materialTextView2.perform(click())
        
        val materialTextView3 = onView(
            allOf(withId(R.id.onboardingThreeBtn), withText("Finalizar"),
            childAtPosition(
            childAtPosition(
            withClassName(`is`("android.widget.FrameLayout")),
            0),
            3),
            isDisplayed()))
        materialTextView3.perform(click())
        
        val recyclerView = onView(
            allOf(withId(R.id.sportRecycler),
            childAtPosition(
            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
            0)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        
        val textView = onView(
            allOf(withId(R.id.emptyStateMsg), withText("No se encontraron eventos"),
            withParent(withParent(withId(R.id.myNavHostFragment))),
            isDisplayed()))
        textView.check(matches(withText("No se encontraron eventos")))
    }
    
    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
