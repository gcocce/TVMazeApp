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
import com.example.tvmazeapp.databinding.FragmentShowEpisodesBinding
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.SeasonList
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.adapters.SeasonsAdapter
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
class ShowEpisodesFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var seasonsAdapter: SeasonsAdapter

    /**
     * The placeholder content this fragment is presenting.
     */
    private var showId: String? = null

    lateinit var titleTextView: TextView

    private var _binding: FragmentShowEpisodesBinding? = null

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowEpisodesBinding.inflate(inflater, container, false)
        val rootView = binding.root

        titleTextView = binding.showTitle

        //updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Episode

            //viewModel.setSelectedShow(item)
            Timber.d("%s Episode %s Number %s", item.name, item.number, TVMazeApp().TAG)

            viewModel.setSelectedEpisode(item)

            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController().navigate(R.id.episode_detail_fragment)
            } else {
                itemView.findNavController().navigate(R.id.episode_detail_fragment)
            }

        }

        val recyclerView: RecyclerView? = binding.episodeListRecyclerView

        seasonsAdapter = SeasonsAdapter(onClickListener)
        recyclerView?.adapter = seasonsAdapter

        viewModel.selectedShow.value?.let {
            viewModel.loadEpisodes(it.id)
            binding.showTitle.text = it.name
        }

        viewModel.episodes.observe(this, Observer<List<Episode>>{ episodes ->
            for( e in episodes){
                Timber.d("%s detail episode %s season %s number %s", TVMazeApp().TAG, e.name, e.season, e.number)
            }

            episodes?.let {
                loadEpisodes(seasonsAdapter, it)
            }
        })

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
    }

    private fun loadEpisodes(seasonAdapter: SeasonsAdapter, allEpisodes: List<Episode>){
        val seasonArrayList = ArrayList<SeasonList.Season>()

        var currentSeason = 1
        var episodes = ArrayList<Episode>()
        for(e in allEpisodes){
            if (e.season == currentSeason){
                episodes.add(e)
            }else{
                val title = "Season $currentSeason"
                val season = SeasonList.Season(title, episodes)
                seasonArrayList.add(season)
                currentSeason = e.season
                episodes = ArrayList()
                episodes.add(e)
            }
        }

        val title = "Season $currentSeason"
        val season = SeasonList.Season(title, episodes)
        seasonArrayList.add(season)

        val seasonList = SeasonList(seasonArrayList.toList())
        seasonAdapter.seasonList = seasonList
    }

    private fun updateContent() {
        val show = viewModel.selectedShow.value

        if(show!=null && binding!=null){
            binding.showTitle.text = show.name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}