package com.example.tvmazeapp.utils

object UtilTest {

    @JvmStatic
    fun getRandomString(length: Int) : String {
        val charset = ('A'..'Z')

        return List(length) { charset.random() }
            .joinToString("")
    }
}