package com.example.tvmazeapp.presentation.views

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.tvmazeapp.R
import com.example.tvmazeapp.databinding.FragmentEpisodeDetailBinding
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
class ShowEpisodeDetailFragment : Fragment() {

    val viewModel: ShowsViewModel by activityViewModels()

    private var _binding: FragmentEpisodeDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedEpisode.value?.let {
            binding.showTitle.text = it.name
            updateContent()
        }
    }

    private fun updateContent() {
        val episode = viewModel.selectedEpisode.value

        if(episode!=null && binding!=null){
            binding.showTitle.text = episode.name

            binding.numberSeason.text = "Season ${episode.season} Episode ${episode.number}"

            binding.summary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(episode.summary, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(episode.summary)
            }

            if(episode.image.original!=""){
                binding.detailPoster?.let {
                    Glide.with(it.context)
                        .load(episode.image.original)
                        .error(R.drawable.ic_broken_image_24)
                        .skipMemoryCache(false)
                        .centerInside()
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                                binding.posterEpisodeProgress?.visibility = View.GONE
                                binding.detailPoster?.visibility = View.GONE
                                return false
                            }
                            override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                                binding.posterEpisodeProgress?.visibility = View.GONE
                                Timber.d("onLoadFailed")
                                return false
                            }
                        })
                        .into(it)
                }
            }else{
                binding.posterEpisodeProgress?.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}