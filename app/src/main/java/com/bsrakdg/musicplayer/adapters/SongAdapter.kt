package com.bsrakdg.musicplayer.adapters

import android.view.View
import com.bsrakdg.musicplayer.R
import com.bsrakdg.musicplayer.data.entities.Song
import com.bsrakdg.musicplayer.databinding.ListItemBinding
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class SongAdapter
@Inject
constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.list_item) {

    override fun onBind(view: View, song: Song) {
        ListItemBinding.bind(view).apply {
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