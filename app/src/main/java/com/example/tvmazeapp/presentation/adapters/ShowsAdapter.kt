package com.example.tvmazeapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvmazeapp.R
import com.example.tvmazeapp.databinding.ShowCardviewBinding
import com.example.tvmazeapp.domain.entities.Show


class ShowsAdapter(
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {

    var shows: List<Show> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShowCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shows[position]
        holder.titleView.text = item.name

        Glide.with(holder.imageView.context)
            .load(item.image.medium)
            .error(R.drawable.ic_broken_image_24)
            .skipMemoryCache(true)
            .centerInside()
            .thumbnail(0.5f)
            .into(holder.imageView)

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = shows.size

    inner class ViewHolder(binding: ShowCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val titleView: TextView = binding.title
        val imageView: ImageView = binding.poster
    }
}