package com.example.tvmazeapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmazeapp.databinding.ItemSeasonBinding
import com.example.tvmazeapp.domain.entities.SeasonList

class SeasonsAdapter(
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<SeasonsAdapter.ViewHolder>() {

    var seasonList: SeasonList = SeasonList(emptyList())
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val season = seasonList.seasons[position]
        holder.titleView.text = season.name

        val episodesAdapter = EpisodesAdapter(onClickListener)
        holder.episodesRecyclerView.adapter = episodesAdapter
        episodesAdapter.episodeList = season.episodes
    }

    override fun getItemCount() = seasonList.seasons.size

    inner class ViewHolder(binding: ItemSeasonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val titleView: TextView = binding.seasonTitle
        val episodesRecyclerView = binding.seasonEpisodesRecyclerview

    }

}

