package com.example.tvmazeapp.presentation.views

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowEpisodesBinding
import com.example.tvmazeapp.domain.entities.Episode
import com.example.tvmazeapp.domain.entities.SeasonList
import com.example.tvmazeapp.presentation.adapters.SeasonsAdapter
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ShowEpisodesFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var seasonsAdapter: SeasonsAdapter

    private var _binding: FragmentShowEpisodesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.error.observe(this, Observer<String>{ message ->
            Timber.d("viewModel.error.observe")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowEpisodesBinding.inflate(inflater, container, false)
        val rootView = binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Episode

            Timber.d("%s Episode %s Number %s", item.name, item.number, TVMazeApp().TAG)

            viewModel.setSelectedEpisode(item)

            if (itemDetailFragmentContainer != null) {
                // layout configuration (layout, layout-sw600dp)
                itemDetailFragmentContainer.findNavController().navigate(R.id.episode_detail_fragment)
            } else {
                itemView.findNavController().navigate(R.id.episode_detail_fragment)
            }
        }

        val recyclerView: RecyclerView? = binding.episodeListRecyclerView

        seasonsAdapter = SeasonsAdapter(onClickListener)
        recyclerView?.adapter = seasonsAdapter

        val seasonList = SeasonList(emptyList())
        seasonsAdapter.seasonList = seasonList

        viewModel.episodes.observe(this, Observer<List<Episode>>{ episodes ->
            episodes?.let {
                loadEpisodes(seasonsAdapter, it)
            }
        })

         viewModel.progressLoadingEpisodes.observe(this, Observer<Boolean>{ progress ->
            _binding?.progressLoadingEpisodeList?.let {
                progress?.let {
                    if (progress){
                        binding.progressLoadingEpisodeList?.visibility = View.VISIBLE
                    }else{
                        binding.progressLoadingEpisodeList?.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.selectedShow.value?.let {
            viewModel.loadEpisodes(it.id)
            binding.showTitle.text = it.name
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}