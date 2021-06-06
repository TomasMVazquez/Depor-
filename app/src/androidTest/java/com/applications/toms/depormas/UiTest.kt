package com.applications.toms.depormas

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTest

@LargeTest
class UiTest: KoinTest {

    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(
            GrantPermissionRule.grant(
                "android.permission.ACCESS_COARSE_LOCATION"
            )
        )
        .around(ActivityScenarioRule(MainActivity::class.java))

    @Test
    fun clickOnSportShowSelectedSportEvents(){
        onView(withId(R.id.sportRecycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        onView(withId(R.id.emptyStateMsg))
            .check(ViewAssertions.matches(ViewMatchers.withText("No se encontraron eventos")))
    }

}