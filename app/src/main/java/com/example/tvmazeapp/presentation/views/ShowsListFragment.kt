package com.example.tvmazeapp.presentation.views

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowListBinding
import com.example.tvmazeapp.databinding.ShowCardviewBinding
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.adapters.ShowsAdapter
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * A Fragment representing a list of Pings. This fragment
 * has different presentations for handset and larger screen devices. On
 * handsets, the fragment presents a list of items, which when touched,
 * lead to a {@link ItemDetailFragment} representing
 * item details. On larger screens, the Navigation controller presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class ShowsListFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var recyclerViewAdapter: ShowsAdapter

    private var _binding: FragmentShowListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = binding.showListRecyclerView

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
                                                       //show_item_detal is an action
                itemView.findNavController().navigate(R.id.show_item_detail)
            }
        }

        /**
         * Context click listener to handle Right click events
         * from mice and trackpad input to provide a more native
         * experience on larger screen devices
         */
        val onContextClickListener = View.OnContextClickListener { v ->
            val item = v.tag as Show

            Timber.d("Context click of item " + item.id)

            viewModel.setSelectedShow(item)

            true
        }

        recyclerViewAdapter = ShowsAdapter(onClickListener,onContextClickListener)

        recyclerView?.adapter = recyclerViewAdapter
        recyclerView?.layoutManager = GridLayoutManager(context,2);

        viewModel.shows.observe(this, Observer<ArrayList<Show>>{ shows ->
            Timber.d("%s List of Shows changed... ", TVMazeApp().TAG)
            //for (s in shows){Timber.d("%s Show %s", TVMazeApp().TAG, s.name)}

            recyclerViewAdapter?.shows = shows
        })

        viewModel.progressLoadingShows.observe(this, Observer<Boolean>{ progress ->

            _binding?.progressLoadingShowList?.let {
                //if (_binding?.progress?.visibility == View.VISIBLE){
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