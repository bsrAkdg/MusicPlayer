package com.bsrakdg.musicplayer.exoplayer

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.bsrakdg.musicplayer.exoplayer.callbacks.MusicPlayerNotificationListener
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

private const val SERVICE_TAG = "Music Service"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer : SimpleExoPlayer

    // don't use main thread to play music
    private val serviceJob = Job()
    // cancellation job
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob) // merge these two together for cancelling service scope

    // Allows interaction with media controllers, volume keys, media buttons, and transport controls.
    private lateinit var mediaSession : MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private lateinit var musicNotificationManager: MusicNotificationManager

    override fun onCreate() {
        super.onCreate()

        // when clicked notification, open activity
        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        // We can use to get information about this media session
        // MediaBrowserServiceCompat class has the property media session token or something like that just session token :
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }

        // Represents an ongoing session.
        // We need to assign the token of our just created media session to our service (MusicService)
        sessionToken = mediaSession.sessionToken

        // create notification manager for handling notification state
        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {

        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)

    }

    override fun onDestroy() {
        super.onDestroy()
        // when service is destroyed, cancel coroutine
        serviceScope.cancel()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}