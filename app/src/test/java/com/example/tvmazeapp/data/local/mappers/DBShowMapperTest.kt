package com.example.tvmazeapp.data.local.mappers

import com.example.tvmazeapp.data.local.entities.DBShow
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule
import junit.framework.TestCase
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Test

class DBShowMapperTest : TestCase(){

    @Test
    fun testShowToDBShow() {

        val show = Show(id=1, url="tvmaze", name="tvmaze", status="available", type="tvmaze", listOf("Horror", "Fiction"),
        language = "English", summary = "tvmaze", schedule = ShowSchedule("10pm", listOf("Monday", "Tuesday")),
            image = ShowImage("medium", "original"))

        val dbShow = DBShow(id = 1, url="tvmaze", name="tvmaze", status = "available", type = "tvmaze", genres = "Horror#Fiction",
        language = "English", summary = "tvmaze", schedule_time = "10pm", schedule_days = "Monday#Tuesday",
        image_medium = "medium", image_original = "original")

        MatcherAssert.assertThat(dbShow, IsEqual.equalTo(DBShowMapper().mapToEntity(show)))
    }

    @Test
    fun testDBShowToShow() {
        val show = Show(id=1, url="tvmaze", name="tvmaze", status="available", type="tvmaze", listOf("Horror", "Fiction"),
            language = "English", summary = "tvmaze", schedule = ShowSchedule("10pm", listOf("Monday", "Tuesday")),
            image = ShowImage("medium", "original"))

        val dbShow = DBShow(id = 1, url="tvmaze", name="tvmaze", status = "available", type = "tvmaze", genres = "Horror#Fiction",
            language = "English", summary = "tvmaze", schedule_time = "10pm", schedule_days = "Monday#Tuesday",
            image_medium = "medium", image_original = "original")

        MatcherAssert.assertThat(show, IsEqual.equalTo(DBShowMapper().mapFromEntity(dbShow)))
    }
}