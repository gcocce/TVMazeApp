package com.example.tvmazeapp.presentation.views

import android.content.ClipData
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowDetailBinding
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ShowsListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class ShowDetailFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    /**
     * The placeholder content this fragment is presenting.
     */
    private var showId: String? = null

    lateinit var titleTextView: TextView

    private var _binding: FragmentShowDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            showId = clipDataItem.text.toString()
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                showId = it.getString(ARG_ITEM_ID)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        titleTextView = binding.showTitle

        //updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedShow.observe(this, Observer<Show>{ show ->
            Timber.d("%s detail show %s %s %s", show.name, show.language, show.summary, TVMazeApp().TAG)

            updateContent()

            viewModel.loadEpisodes(show.id)
        })

        viewModel.episodes.observe(this, Observer<List<Episode>>{ episodes ->
            for( e in episodes){
                Timber.d("%s detail episode %s season %s number %s", TVMazeApp().TAG, e.name, e.season, e.number)
            }
        })
    }

    private fun updateContent() {
        val show = viewModel.selectedShow.value

        if(show!=null && binding!=null){

            binding.showTitle.text = show.name

            binding.genres?.text = show.genres.joinToString(" ")

            binding.schedule?.text = show.schedule.time + " "+ show.schedule.days.joinToString(" ")

            binding.summary?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(show.summary)
            }

            binding.detailPoster?.let {
                Glide.with(it.context)
                    .load(show.image.original)
                    .error(R.drawable.ic_broken_image_24)
                    .skipMemoryCache(true)
                    .centerInside()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                            binding.posterProgress?.setVisibility(View.GONE)
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            binding.posterProgress?.setVisibility(View.GONE)
                            return false
                        }
                    })
                    .into(it)
            }



        }

        // Show the placeholder content as text in a TextView.
        //item?.let {itemDetailTextView.text = it.details}
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}