package com.example.tvmazeapp.presentation.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import timber.log.Timber
import java.io.File

//: RecyclerView.Adapter<PicViewHolder>()
class ShowsAdapter(viewModel: ShowsViewModel)  {

    //var viewModel = viewModel
    /**
     * The videos that our Adapter will show
     */
    var shows: List<Show> = emptyList()
        set(value) {
            field = value

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            //notifyDataSetChanged()
        }

    /*

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicViewHolder {
        val withDataBinding: RecyclerviewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            PicViewHolder.LAYOUT,
            parent,
            false
        )
        return PicViewHolder(withDataBinding)
    }

    override fun getItemCount() = shows.size

    /**
     * Called by RecyclerView to display the data at the specified position. T
     */
    override fun onBindViewHolder(holder: PicViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.pic = shows[position]

            val empty = shows[position].getSrc() == ""

            Timber.d("onBindViewHolder file: %s", shows[position].getSrc())

            val extDir = it.textView.context.getExternalFilesDir(null)
            val file: File = File(extDir, shows[position].getSrc())

            Timber.d("onBindViewHolder file uri %s", file.toUri())

            if (!empty && file.exists()) {

                Glide.with(it.imageView.context)
                    .load(file.toUri())
                    .fitCenter()
                    .thumbnail(0.2f)
                    .into(it.imageView)

                it.textView.text = shows[position].getName()
            }else{
                Timber.d("onBindViewHolder empty file")
                it.textView.text = "Not found"
                it.textView2.text = ""
            }
        }

        holder.itemView.setOnClickListener {
            Timber.d("holder.itemView.setOnClickListener")
        }
    }

     */
}

/*
/**
 * ViewHolder for items. All work is done by data binding.
 */
class PicViewHolder(val viewDataBinding: RecyclerviewItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = com.example.photoclassifier.R.layout.recyclerview_item
    }
}

 */