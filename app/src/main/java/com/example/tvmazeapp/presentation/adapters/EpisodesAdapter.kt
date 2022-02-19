package com.example.tvmazeapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmazeapp.databinding.ItemEpisodeBinding
import com.example.tvmazeapp.domain.entities.Episode

class EpisodesAdapter(
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<EpisodesAdapter.ViewHolder>() {

    var episodeList: List<Episode> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodeList[position]
        holder.titleView.text = episode.name
        holder.numberView.text = episode.number.toString()

        with(holder.itemView) {
            tag = episode
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = episodeList.size

    inner class ViewHolder(binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val titleView: TextView = binding.episodeTitle
        val numberView: TextView = binding.episodeNumber
    }
}

