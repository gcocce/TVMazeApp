package com.example.tvmazeapp.domain.usecases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.tvmazeapp.R
import com.example.tvmazeapp.RepeatRule
import com.example.tvmazeapp.RepeatTest
import com.example.tvmazeapp.di.PreferencesModule
import com.example.tvmazeapp.presentation.views.SplashActivity
import com.example.tvmazeapp.utils.Security
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@UninstallModules(PreferencesModule::class)
@HiltAndroidTest
class AppStartWithSecurityPinTest {

    @Module
    @InstallIn(SingletonComponent::class)
    class PreferencesTestModule{

        @Singleton
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("test", MODE_PRIVATE)
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Rule(order = 1) @JvmField
    var mActivitySplashRule: ActivityScenarioRule<SplashActivity> = ActivityScenarioRule(SplashActivity::class.java)

    @Rule @JvmField
    var repeatRule: RepeatRule = RepeatRule()

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    private fun setSecurityPin(pin: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("security", true)
        editor.putString("pin", Security.md5(pin))
        editor.commit()
    }

    @Test
    @RepeatTest(10)
    fun testAppWithSecurityPinActivated(){

        val pin = Random.nextInt(1000,9999).toString()

        setSecurityPin(pin)

        mActivitySplashRule.scenario.moveToState(Lifecycle.State.RESUMED)
        mActivitySplashRule.scenario.recreate()

        sleep(3000)

        for (letter in pin){
            onView(withText(letter.toString())).perform(click())
        }

        onView(withId(R.id.show_list_recycler_view)).check(matches(ViewMatchers.isDisplayed()))
    }
}

/*
onView(withId(R.id.button)).perform(click())

onView(withText("1")).check(matches(isDisplayed()))

onView(withText("Increase")).check(matches(isDisplayed())).check(matches(isEnabled())).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

onView(withId(R.id.editText))
            .perform(ViewActions.replaceText(""),typeText("segundoactivity"), ViewActions.closeSoftKeyboard());

onView(withId(R.id.button2)).perform(click());

onView(withId(R.id.textView2)).check(matches(withText("segundoactivity")));
* */