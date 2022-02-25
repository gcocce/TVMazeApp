package com.example.tvmazeapp.usecases

import android.content.Context
import android.content.SharedPreferences
import com.example.tvmazeapp.TVMazeApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LogPinTest {

    @Before
    fun setUp() {
        val app = RuntimeEnvironment.getApplication() as TVMazeApp
        
    }


    @Test
    fun testSomedhint() {

        //MatcherAssert.assertThat(dbShow, IsEqual.equalTo(DBShowMapper().mapToEntity(show)))
    }


    @Test
    fun testSecurityEncryption(){

        val sharedPreferences: SharedPreferences = RuntimeEnvironment.application
            .getSharedPreferences("you_custom_pref_name", Context.MODE_PRIVATE)


    }


}