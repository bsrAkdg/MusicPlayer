package com.bsrakdg.musicplayer.other

open class Event<out T>(
    private val data: T
) {

    var hasBeenHandled = false
        private set

    // just first time handle data
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    // sometimes you need to data when if it has been handled
    fun peekContent() = data
}