package com.example.tvmazeapp.presentation.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvmazeapp.R
import com.example.tvmazeapp.databinding.ShowCardviewBinding
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import timber.log.Timber
import java.io.File


class ShowsAdapter(
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }

            setOnLongClickListener { v ->
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                val clipItem = ClipData.Item(item.name)
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )

                if (Build.VERSION.SDK_INT >= 24) {
                    v.startDragAndDrop(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                } else {
                    v.startDrag(
                        dragData,
                        View.DragShadowBuilder(v),
                        null,
                        0
                    )
                }
            }
        }
    }

    override fun getItemCount() = shows.size

    inner class ViewHolder(binding: ShowCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val titleView: TextView = binding.title
        val imageView: ImageView = binding.poster
    }

}

