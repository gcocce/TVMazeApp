package com.example.tvmazeapp.data.remote.mappers


import com.example.tvmazeapp.data.remote.dtos.NetworkEpisode
import com.example.tvmazeapp.data.remote.dtos.NetworkImage
import com.example.tvmazeapp.data.remote.dtos.NetworkSchedule
import com.example.tvmazeapp.data.remote.dtos.NetworkShow
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.domain.entities.ShowImage
import com.example.tvmazeapp.domain.entities.ShowSchedule
import com.example.tvmazeapp.domain.mappers.EntityMapper
import javax.inject.Inject

class NetworkEpisodeMapper
@Inject constructor(): EntityMapper<NetworkEpisode, Episode> {
    override fun mapFromEntity(entity: NetworkEpisode): Episode {
        return Episode(
            id=entity.id,
            image = ShowImage(entity.image?.medium ?: "", entity.image?.original ?: ""),
            name = entity.name,
            summary = entity.summary,
            number = entity.number,
            season = entity.season
        )
    }

    override fun mapToEntity(domainModel: Episode): NetworkEpisode {
        return NetworkEpisode(
            id = domainModel.id,
            image = NetworkImage(domainModel.image.medium, domainModel.image.original),
            name = domainModel.name,
            summary = domainModel.summary,
            number = domainModel.number,
            season = domainModel.season
        )
    }

    fun mapFromEntityList(entities: List<NetworkEpisode>): List<Episode>{
        return entities.map { mapFromEntity(it) }
    }

}