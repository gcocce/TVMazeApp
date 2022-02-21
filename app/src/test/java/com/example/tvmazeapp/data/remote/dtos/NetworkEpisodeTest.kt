package com.example.tvmazeapp.data.remote.dtos

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class NetworkEpisodeTest {
    @Test
    fun testNetworkEpisodeEncodeOnImageNull() {
        val encoded = """{"id": 1, "name": "test1", "season": 2, "number": 1,"summary": "some summary"}""".trimMargin()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkEpisode::class.java)

        adapter.fromJson(encoded)!!.run {
            assertThat(id, equalTo(1))
            assertThat(image, equalTo(null))
        }
    }

    @Test
    fun testNetworkEpisodeDencodeOnImageNull() {
        val netImage = NetworkImage("", "")
        val decoded = NetworkEpisode(id = 1, name = "test1", season = 2, number = 1,
            summary = "some summary", image = null)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(NetworkEpisode::class.java)

        assertThat(adapter.toJson(decoded), equalTo("""{"id":1,"name":"test1","season":2,"number":1,"summary":"some summary"}"""))
    }

}

