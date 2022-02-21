package com.example.tvmazeapp.data.remote.mappers

import com.example.tvmazeapp.data.local.entities.DBShow
import com.example.tvmazeapp.data.local.mappers.DBShowMapper
import com.example.tvmazeapp.data.remote.dtos.NetworkImage
import com.example.tvmazeapp.data.remote.dtos.NetworkSchedule
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule
import junit.framework.TestCase
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Test

class NetworkShowMapperTest : TestCase(){

    @Test
    fun testShowToNetworkShow() {

        val show = Show(id=1, url="tvmaze", name="tvmaze", status="available", type="tvmaze", listOf("Horror", "Fiction"),
            language = "English", summary = "tvmaze", schedule = ShowSchedule("10pm", listOf("Monday", "Tuesday")),
            image = ShowImage("medium", "original")
        )

        val networkShow = NetworkShow(id = 1, url="tvmaze", name="tvmaze", status = "available", type = "tvmaze", genres = listOf("Horror", "Fiction"),
            language = "English", summary = "tvmaze", schedule = NetworkSchedule("10pm", listOf("Monday","Tuesday")),
            image = NetworkImage("medium", "original")
        )

        MatcherAssert.assertThat(networkShow, IsEqual.equalTo(NetworkShowMapper().mapToEntity(show)))
    }

    @Test
    fun testNetworkShowToShow() {
        val show = Show(id=1, url="tvmaze", name="tvmaze", status="available", type="tvmaze", listOf("Horror", "Fiction"),
            language = "English", summary = "tvmaze", schedule = ShowSchedule("10pm", listOf("Monday", "Tuesday")),
            image = ShowImage("medium", "original")
        )

        val networkShow = NetworkShow(id = 1, url="tvmaze", name="tvmaze", status = "available", type = "tvmaze", genres = listOf("Horror", "Fiction"),
            language = "English", summary = "tvmaze", schedule = NetworkSchedule("10pm", listOf("Monday","Tuesday")),
            image = NetworkImage("medium", "original")
        )

        MatcherAssert.assertThat(show, IsEqual.equalTo(NetworkShowMapper().mapFromEntity(networkShow)))
    }
}