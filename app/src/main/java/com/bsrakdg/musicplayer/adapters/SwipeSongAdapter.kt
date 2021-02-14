package com.bsrakdg.musicplayer.adapters

import android.view.View
import com.bsrakdg.musicplayer.R
import com.bsrakdg.musicplayer.data.entities.Song
import com.bsrakdg.musicplayer.databinding.SwipeItemBinding

class SwipeSongAdapter : BaseSongAdapter(R.layout.swipe_item) {

    override fun onBind(view: View, song: Song) {

        SwipeItemBinding.bind(view).apply {
            tvPrimary.text = "${song.title} - ${song.subtitle}"

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}