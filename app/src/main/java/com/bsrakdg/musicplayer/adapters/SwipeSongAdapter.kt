package com.bsrakdg.musicplayer.adapters

import com.bsrakdg.musicplayer.data.entities.Song
import com.bsrakdg.musicplayer.databinding.ListItemBinding

class SwipeSongAdapter : BaseSongAdapter() {

    override fun onBind(binding: ListItemBinding, song: Song) {
        binding.apply {

            tvPrimary.text = "${song.title} - ${song.subtitle}"

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}