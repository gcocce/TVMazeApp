package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual

class NetworkShowTest : TestCase() {
    fun testNetworkShowEncodeOnImageNull() {
        val encoded = """{"id": 1, "url":"", "name": "test1", "status":"", "type":"", "genres":["Terror"], "language":"English","summary": "some summary", "schedule":{"time":"10am","days":["monday"]}}""".trimMargin()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkShow::class.java)

        adapter.fromJson(encoded)!!.run {
            MatcherAssert.assertThat(image, IsEqual.equalTo(null))
        }
    }

    fun testNetworkShowDecodeOnImageNull() {
        val schedule= NetworkSchedule("10am", listOf("monday"))
        val decoded = NetworkShow(
            id = 1, name = "test1", genres = listOf("Terror"), status = "", type = "", language = "English",
            summary = "some summary", image = null, schedule = schedule, url = "url")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkShow::class.java)

        MatcherAssert.assertThat(
            adapter.toJson(decoded),
            IsEqual.equalTo("""{"id":1,"url":"url","name":"test1","status":"","type":"","genres":["Terror"],"language":"English","summary":"some summary","schedule":{"time":"10am","days":["monday"]}}""".trimMargin()))
    }


    fun testNetworkShowEncodeOnScheduleNull() {
        val encoded = """{"id": 1, "url":"", "name": "test1", "status":"", "type":"", "genres":["Terror"], "language":"English","summary": "some summary", "image":{"medium":"", "original":""}}""".trimMargin()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkShow::class.java)

        adapter.fromJson(encoded)!!.run {
            MatcherAssert.assertThat(schedule, IsEqual.equalTo(null))
        }
    }

    fun testNetworkShowDecodeOnScheduleNull() {
        val netImage = NetworkImage("", "")
        val decoded = NetworkShow(
            id = 1, name = "test1", genres = listOf("Terror"), status = "", type = "", language = "English",
            summary = "some summary", image = netImage, schedule = null, url = "url")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkShow::class.java)

        MatcherAssert.assertThat(
            adapter.toJson(decoded),
            IsEqual.equalTo("""{"id":1,"url":"url","name":"test1","status":"","type":"","genres":["Terror"],"language":"English","summary":"some summary","image":{"medium":"","original":""}}""".trimMargin())
        )
    }

}