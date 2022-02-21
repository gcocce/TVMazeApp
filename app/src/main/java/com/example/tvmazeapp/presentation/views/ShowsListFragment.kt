package com.example.tvmazeapp.presentation.views

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowListBinding
import com.example.tvmazeapp.domain.entities.Show
import com.example.tvmazeapp.presentation.adapters.ShowsAdapter
import com.example.tvmazeapp.presentation.viewmodels.ShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.view.MenuInflater
import android.content.Intent
import android.widget.SearchView
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class ShowsListFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private lateinit var recyclerViewAdapter: ShowsAdapter

    private var itemDetailFragmentContainer: View? = null

    private var _binding: FragmentShowListBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.error.observe(this, { message ->
            Timber.d("viewModel.error.observe")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        viewModel.message.observe(this, { message ->
            Timber.d("viewModel.message.observe")
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.showListRecyclerView

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        itemDetailFragmentContainer = view.findViewById(R.id.item_detail_nav_container)

        /** Click Listener to trigger navigation based on if you have
         * a single pane layout or two pane layout
         */
        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Show

            viewModel.setSelectedShow(item)
            Timber.d("%s item %s %s %s", item.name, item.language, item.summary, TVMazeApp().TAG)

            if (itemDetailFragmentContainer != null) {
                // layout configuration (layout, layout-sw600dp)
                itemDetailFragmentContainer?.findNavController()
                    ?.navigate(R.id.fragment_item_detail)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail)
            }
        }

        recyclerViewAdapter = ShowsAdapter(onClickListener)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = GridLayoutManager(context,2)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.nextPage()
                }
            }
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

        viewModel.shows.observe(this, { shows ->
            Timber.d("%s List of Shows changed... ShowsListFragment ", TVMazeApp().TAG)
            recyclerViewAdapter?.shows = shows
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.show_option_menu, menu)

        val searchView = SearchView((activity as ShowsActivity).supportActionBar?.themedContext ?: context)

        menu.findItem(R.id.menu_item_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_ALWAYS)
            actionView = searchView
        }

        searchView.isIconifiedByDefault = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Timber.d("searchView onQueryTextSubmit %s", query)
                viewModel.searchShowRemote(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Timber.d("searchView onQueryTextChange")
                return false
            }
        })
        searchView.setOnClickListener {view ->  }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("%s ItemSelected: %s", TVMazeApp().TAG, item)

        when (item.itemId) {
            R.id.menu_item_settings -> {
                //val intent = Intent(activity, SettingsActivity::class.java)
                val intent = Intent(activity, CustomSettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_item_full_list -> {
                viewModel.switchToFullList()
            }
            R.id.menu_item_favorites -> {
                findNavController().navigate(R.id.show_favorites_fragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}