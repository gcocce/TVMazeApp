package com.example.tvmazeapp.presentation.views

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tvmazeapp.R
import com.example.tvmazeapp.TVMazeApp
import com.example.tvmazeapp.databinding.FragmentShowDetailBinding
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

    var isFavorite = false

    private var _binding: FragmentShowDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.posterProgress.visibility=View.VISIBLE

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated in ShowDetailFragment")

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        binding.button?.let {
            it.setOnClickListener {
                if (itemDetailFragmentContainer != null) {
                    // layout configuration (layout, layout-sw600dp)
                    itemDetailFragmentContainer.findNavController()
                        .navigate(R.id.show_episodes_fragment)
                } else {
                    binding.root.findNavController().navigate(R.id.show_episodes)
                }
            }
        }

        binding.buttonFavorites?.setOnClickListener {
            if (isFavorite){
                viewModel.removeFromFavorites()
            }else{
                viewModel.addShowAsFavorites()
            }
        }

        isFavorite = viewModel.selectedShowIsFavorite?.value ?: false

        updateFavoriteButton(isFavorite)

        viewModel.selectedShowIsFavorite.observe(this, Observer<Boolean> {
            isFavorite = it
            updateFavoriteButton(isFavorite)
        })

        viewModel.selectedShow.observe(this, Observer<Show>{ show ->
            Timber.d("%s detail show %s %s %s", show.name, show.language, show.summary, TVMazeApp().TAG)
            updateContent()
        })

        viewModel.error.observe(this, Observer<String>{ message ->
            if (viewModel.showError?.value == true){
                viewModel.cleanError()
                Timber.d("viewModel.error.observe")
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateFavoriteButton(isFfavorite: Boolean){
        if(isFfavorite){
            binding.buttonFavorites?.text = "Remove from Favorites"
        }else{
            binding.buttonFavorites?.text = "Add to Favorites"
        }
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
                            binding.posterProgress.setVisibility(View.GONE)
                            return false
                        }
                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                            Timber.d("onResourceReady")
                            binding.posterProgress.setVisibility(View.GONE)
                            return false
                        }
                    })
                    .into(it)
            }
        }
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView")
        super.onDestroyView()
        //_binding = null
    }
}