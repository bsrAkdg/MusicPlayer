package com.bsrakdg.musicplayer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bsrakdg.musicplayer.R
import com.bsrakdg.musicplayer.adapters.SongAdapter
import com.bsrakdg.musicplayer.databinding.FragmentHomeBinding
import com.bsrakdg.musicplayer.other.Status
import com.bsrakdg.musicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var songAdapter: SongAdapter

    // memory leak
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupRecyclerView()
        subscribeToObservers()

        songAdapter.setItemClickListener { song ->
            mainViewModel.playOrToggleSong(song)
        }
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = songAdapter
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        songAdapter.submitList(songs)
                    }
                }
                Status.ERROR -> {
                    binding.allSongsProgressBar.isVisible = false
                }
                Status.LOADING -> {
                    binding.allSongsProgressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvAllSongs.adapter = null
        _binding = null
    }
}