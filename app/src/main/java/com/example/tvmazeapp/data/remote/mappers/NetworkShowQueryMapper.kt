package com.example.tvmazeapp.data.remote.mappers

import com.example.tvmazeapp.data.remote.dtos.NetworkShowQuery
import com.example.tvmazeapp.domain.entities.Show
import javax.inject.Inject

class NetworkShowQueryMapper
@Inject constructor(){
    fun mapFromEntityList(entities: List<NetworkShowQuery>): List<Show>{
        return entities.map { NetworkShowMapper().mapFromEntity(it.show) }
    }
}