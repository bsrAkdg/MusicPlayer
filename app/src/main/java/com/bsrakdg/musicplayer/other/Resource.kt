package com.bsrakdg.musicplayer.other

// out is for super class
// example: if T is Number then Integer, Double, etc can be used for T
data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data)
        fun <T> error(message: String, data: T?) = Resource(Status.ERROR, data, message)
        fun <T> loading(data: T?) = Resource(Status.LOADING, data)
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING
}