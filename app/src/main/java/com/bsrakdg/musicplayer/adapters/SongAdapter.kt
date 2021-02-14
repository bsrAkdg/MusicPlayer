package com.bsrakdg.musicplayer.adapters

import com.bsrakdg.musicplayer.data.entities.Song
import com.bsrakdg.musicplayer.databinding.ListItemBinding
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class SongAdapter
@Inject
constructor(
    private val glide: RequestManager
) : BaseSongAdapter() {

    override fun onBind(binding: ListItemBinding, song: Song) {
        binding.apply {
            tvPrimary.text = song.title
            tvSecondary.text = song.subtitle
            glide.load(song.imageUrl).into(ivItemImage)

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}