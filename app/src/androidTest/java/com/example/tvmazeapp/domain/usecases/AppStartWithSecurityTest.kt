package com.example.tvmazeapp.domain.usecases

import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.tvmazeapp.R
import com.example.tvmazeapp.presentation.views.SplashActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import javax.inject.Inject


@HiltAndroidTest
class AppStartWithSecurityTest {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this) // 5

    @Rule(order = 1) @JvmField
    var mActivitySplashRule: ActivityScenarioRule<SplashActivity> = ActivityScenarioRule(SplashActivity::class.java)

    @Before
    fun setUp() {
        hiltAndroidRule.inject() // 6
    }

    fun setSecurityOn(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("security", true)
        editor.commit()
    }

    fun setSecurityOff(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("security", false)
        editor.commit()
    }

    @Test
    fun testAppWithSecurityPinActivated(){
        setSecurityOn()
        mActivitySplashRule.scenario.recreate()
        mActivitySplashRule.scenario.moveToState(Lifecycle.State.RESUMED)

        sleep(3000)

        Espresso.onView(withId(R.id.imageViewLogoLock)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testAppWithSecurityPinDeactivated(){
        setSecurityOff()
        mActivitySplashRule.scenario.recreate()
        mActivitySplashRule.scenario.moveToState(Lifecycle.State.RESUMED)

        sleep(3000)

        Espresso.onView(withId(R.id.show_list_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}




