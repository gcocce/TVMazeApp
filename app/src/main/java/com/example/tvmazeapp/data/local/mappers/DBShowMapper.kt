package com.example.tvmazeapp.data.local.mappers

import com.example.tvmazeapp.data.local.entities.DBShow
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule
import com.example.tvmazeapp.domain.mappers.EntityMapper
import javax.inject.Inject

class DBShowMapper @Inject constructor(): EntityMapper<DBShow, Show> {
    override fun mapFromEntity(entity: DBShow): Show {
        return Show(
            id = entity.id,
            url = entity.url,
            name = entity.name,
            status = entity.status,
            type = entity.type,
            genres = entity.genres.split("#"),
            language = entity.language,
            summary = entity.summary,
            image = ShowImage(entity.image_medium, entity.image_original),
            schedule = ShowSchedule(entity.schedule_time, entity.schedule_days.split("#"))
        )
    }

    override fun mapToEntity(domainModel: Show): DBShow {
        return DBShow(
            id = domainModel.id,
            url = domainModel.url,
            name = domainModel.name,
            status = domainModel.status,
            type = domainModel.type,
            genres = domainModel.genres.joinToString("#"),
            language = domainModel.language,
            summary = domainModel.summary,
            schedule_time = domainModel.schedule.time,
            schedule_days = domainModel.schedule.days.joinToString("#"),
            image_medium = domainModel.image.medium,
            image_original = domainModel.image.original
        )
    }

    fun mapFromEntityList(entities: List<DBShow>): List<Show>{
        return entities.map { mapFromEntity(it) }
    }
}