package com.bsrakdg.musicplayer.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bsrakdg.musicplayer.R
import com.bsrakdg.musicplayer.data.entities.Song
import com.bsrakdg.musicplayer.databinding.FragmentSongBinding
import com.bsrakdg.musicplayer.exoplayer.toSong
import com.bsrakdg.musicplayer.other.Status
import com.bsrakdg.musicplayer.other.Status.SUCCESS
import com.bsrakdg.musicplayer.ui.viewmodel.MainViewModel
import com.bsrakdg.musicplayer.ui.viewmodel.SongViewModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    private lateinit var binding: FragmentSongBinding

    @Inject
    lateinit var glide: RequestManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val songViewModel: SongViewModel by viewModels()

    private var curPlayingSong: Song? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSongBinding.bind(view)
        subscribeToObservers()
    }

    private fun updateTitleAndSongImage(song: Song) {
        binding.tvSongName.text = "${song.title} - ${song.subtitle}"
        glide.load(song.imageUrl).into(binding.ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { songs ->
                            if (curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }

        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }
    }
}