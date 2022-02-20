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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowDetailBinding
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.SeasonList
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.adapters.SeasonsAdapter
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.NullPointerException

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ShowsListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@AndroidEntryPoint
class ShowDetailFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var seasonsAdapter: SeasonsAdapter

    lateinit var titleTextView: TextView

    private var _binding: FragmentShowDetailBinding? = null

    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)

            val showId = clipDataItem.text.toString()
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreate in ShowDetailFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        Timber.d("onCreateView in ShowDetailFragment")

        titleTextView = binding.showTitle

        binding.posterProgress.visibility=View.VISIBLE

        //updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated in ShowDetailFragment")

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Episode
            Timber.d("%s Episode %s Number %s", item.name, item.number, TVMazeApp().TAG)
        }

        binding.button?.let {
            it.setOnClickListener {
                // Do some work here
                //val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)
                if (itemDetailFragmentContainer != null) {
                    // layout configuration (layout, layout-sw600dp)
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.show_episodes_fragment)
                } else {
                    binding.root.findNavController().navigate(R.id.show_episodes)
                }
            }
        }

        //val recyclerView: RecyclerView? = binding.episodeListRecyclerView

        //seasonsAdapter = SeasonsAdapter(onClickListener)
        //recyclerView?.adapter = seasonsAdapter

        viewModel.selectedShow.observe(this, Observer<Show>{ show ->
            Timber.d("%s detail show %s %s %s", show.name, show.language, show.summary, TVMazeApp().TAG)

            updateContent()

            //viewModel.loadEpisodes(show.id)
        })

        viewModel.error.observe(this, Observer<String>{ message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        /*
        viewModel.episodes.observe(this, Observer<List<Episode>>{ episodes ->
            for( e in episodes){
                Timber.d("%s detail episode %s season %s number %s", TVMazeApp().TAG, e.name, e.season, e.number)
            }

            episodes?.let {
                //loadEpisodes(seasonsAdapter, it)
            }
        })
         */

        /*
        viewModel.progressLoadingEpisodes.observe(this, Observer<Boolean>{ progress ->
            _binding?.progressLoadingEpisodeList?.let {
                //if (_binding?.progress?.visibility == View.VISIBLE){
                progress?.let {
                    if (progress){
                        binding.progressLoadingEpisodeList?.visibility = View.VISIBLE
                    }else{
                        binding.progressLoadingEpisodeList?.visibility = View.GONE
                    }
                }
            }
        })
         */
    }

    private fun loadEpisodes(seasonAdapter: SeasonsAdapter, allEpisodes: List<Episode>){
        val seasonArrayList = ArrayList<SeasonList.Season>()

        var currentSeason = 1
        var episodes = ArrayList<Episode>()
        for(e in allEpisodes){
            if (e.season == currentSeason){
                episodes.add(e)
            }else{
                val season = SeasonList.Season(currentSeason.toString(), episodes)
                seasonArrayList.add(season)
                currentSeason = e.season
                episodes = ArrayList()
            }
        }

        val season = SeasonList.Season(currentSeason.toString(), episodes)
        seasonArrayList.add(season)

        val seasonList = SeasonList(seasonArrayList.toList())
        seasonAdapter.seasonList = seasonList
    }

    private fun updateContent() {
        val show = viewModel.selectedShow.value

        Timber.d("updateContent in ShowDetailFragment")

        if(show!=null && _binding!=null){
            binding.showTitle.text = show.name

            binding.genres.text = show.genres.joinToString(" ")

            binding.schedule.text = show.schedule.time + " "+ show.schedule.days.joinToString(" ")

            binding.summary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(show.summary)
            }

            binding.detailPoster?.let {
                Glide.with(it.context)
                    .load(show.image.original)
                    .error(R.drawable.ic_broken_image_24)
                    .skipMemoryCache(false)
                    .centerInside()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                            if (binding.posterProgress.visibility ==View.VISIBLE){
                                binding.posterProgress.setVisibility(View.GONE)
                            }
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            Timber.d("onResourceReady")
                            if (binding.posterProgress.visibility ==View.VISIBLE){
                                binding.posterProgress.setVisibility(View.GONE)
                            }
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
        Timber.d("onDestroyView")
        super.onDestroyView()
        //_binding = null
    }
}