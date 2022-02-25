package com.example.tvmazeapp.domain.usecases

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.tvmazeapp.R
import com.example.tvmazeapp.di.PreferencesModule
import com.example.tvmazeapp.presentation.views.CustomSettingsActivity
import com.example.tvmazeapp.utils.Security
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@UninstallModules(PreferencesModule::class)
@HiltAndroidTest
class SetSecurityPinTest {

    @Module
    @InstallIn(SingletonComponent::class)
    class PreferencesTestModule{

        @Singleton
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("test", Context.MODE_PRIVATE)
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Rule(order = 1) @JvmField
    var mActivityCustomSettingsRule: ActivityScenarioRule<CustomSettingsActivity> = ActivityScenarioRule(
        CustomSettingsActivity::class.java)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    fun isSecurityOff(): Boolean {
        return sharedPreferences.getBoolean("security", false)
    }

    fun checkPin(pin: String): Boolean{
        return Security.md5(pin).equals(sharedPreferences.getString("pin", ""))
    }

    @Test
    fun testAppWithSecurityPinActivated(){
        mActivityCustomSettingsRule.scenario.moveToState(Lifecycle.State.RESUMED)
        mActivityCustomSettingsRule.scenario.recreate()

        val pin = "1234"

        if (!isSecurityOff()){
            Espresso.onView(ViewMatchers.withId(R.id.switchSecurity)).perform(click())
        }

        Espresso.onView(withId(R.id.etPin)).perform(ViewActions.replaceText(""),
            typeText(pin), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.btSave)).perform(click())

        Assert.assertTrue(checkPin(pin))

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