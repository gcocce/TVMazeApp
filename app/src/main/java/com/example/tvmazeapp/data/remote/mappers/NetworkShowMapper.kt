package com.example.tvmazeapp.data.remote.mappers


import com.example.tvmazeapp.data.remote.dtos.NetworkImage
import com.example.tvmazeapp.data.remote.dtos.NetworkSchedule
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule
import com.example.tvmazeapp.domain.mappers.EntityMapper
import javax.inject.Inject

class NetworkShowMapper
@Inject constructor(): EntityMapper<NetworkShow, Show> {
    override fun mapFromEntity(entity: NetworkShow): Show {
        return Show(
            id=entity.id,
            image = ShowImage(entity.image?.medium ?: "", entity.image?.original ?: ""),
            name = entity.name,
            status = entity.status,
            type = entity.type ?: "",
            url = entity.url,
            genres = entity.genres,
            language = entity.language,
            summary = entity.summary ?: "No summary",
            schedule = ShowSchedule(entity.schedule?.time ?: "", entity.schedule?.days ?: emptyList())
        )
    }

    override fun mapToEntity(domainModel: Show): NetworkShow {
        return NetworkShow(
            id = domainModel.id,
            image = NetworkImage(domainModel.image.medium, domainModel.image.original),
            name = domainModel.name,
            status = domainModel.status,
            type = domainModel.type,
            url = domainModel.url,
            genres = domainModel.genres,
            language = domainModel.language,
            summary = domainModel.summary,
            schedule = NetworkSchedule(domainModel.schedule.time, domainModel.schedule.days)
        )
    }

    fun mapFromEntityList(entities: List<NetworkShow>): List<Show>{
        return entities.map { mapFromEntity(it) }
    }

}