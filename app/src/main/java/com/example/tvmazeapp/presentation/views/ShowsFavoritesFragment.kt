package com.example.tvmazeapp.presentation.views

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.adapters.ShowsAdapter
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.example.tvmazeapp.databinding.FragmentShowFavoritesBinding

@AndroidEntryPoint
class ShowsFavoritesFragment : Fragment() {
    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var recyclerViewAdapter: ShowsAdapter

    private var _binding: FragmentShowFavoritesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.showFavoritesRecyclerView

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Show

            viewModel.setSelectedShow(item)
            Timber.d("%s item %s %s %s", item.name, item.language, item.summary, TVMazeApp().TAG)

            if (itemDetailFragmentContainer != null) {
                // layout configuration (layout, layout-sw600dp)
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail)
            } else {
                itemView.findNavController().navigate(R.id.action_to_item_detail_fragment)
            }
        }

        recyclerViewAdapter = ShowsAdapter(onClickListener)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = GridLayoutManager(context,2)

        viewModel.favorites.value?.let {
            Timber.d("%s Favorites size %s", TVMazeApp().TAG, it.size)
            recyclerViewAdapter.shows = it
        }

        viewModel.favorites.observe(this, { favorites ->
            Timber.d("%s List of Favorites changed... ", TVMazeApp().TAG)
            recyclerViewAdapter?.shows = favorites
        })

        viewModel.progressLoadingShows.observe(this, { progress ->
            _binding?.progressLoadingShowList?.let {
                progress?.let {
                    if (progress){
                        binding.progressLoadingShowList?.visibility = View.VISIBLE
                    }else{
                        binding.progressLoadingShowList?.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}