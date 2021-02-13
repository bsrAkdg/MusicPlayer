package com.bsrakdg.musicplayer.exoplayer

import com.bsrakdg.musicplayer.exoplayer.State.*

/**
 * This class we will on the one hand just make sure to get all songs from our
 * fire store database and also just convert this song format that we have here
 * into a format that we actually need for our service so for our media sessions.
 */
class FirebaseMusicSource {

    // When we download our data from fireStore then this usually takes a little bit of time.
    // So we somehow need a mechanism to check when the source or a music source all of our songs
    // actually finished downloading. And in our service we often need an immediate result for that
    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if(value == STATE_INITIALIZED || value == STATE_ERROR) {
                // no other thread can access this listener at the same time
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED) // return true when music downloading is successfully
                    }
                }
            } else {
                field = value
            }
        }

    // Return boolean that music source is ready or not
    private fun whenReady(action : (Boolean) -> Unit) : Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            // not ready so add list
            onReadyListeners += action
            false
        } else {
            // ready
            action(state == STATE_INITIALIZED)
            true
        }
    }
}

/**
 * Music source can be in.
 */
enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}