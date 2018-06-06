package com.exoextensiontest.audio

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.exoextensiontest.R
import com.exoextensiontest.SampleURI
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class AudioPlayerService : Service() {

    private var player: SimpleExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    override fun onCreate() {
        super.onCreate()

        // initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "AudioDemo"))

        // concat the sources from the media list
        val concatenatingMediaSource = ConcatenatingMediaSource();
        SampleURI.samples.forEach {
            val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it.uri)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        // prepare the player with the media sources
        player?.apply {
            prepare(concatenatingMediaSource)
            playWhenReady = true
        }

        // set the adapter to rebuilt the media item
        playerNotificationManager =
                PlayerNotificationManager.createWithNotificationChannel(this,
                    PLAYBACK_CHANNEL_ID,
                    R.string.channel_name,
                    PLAYBACK_NOTIFICATION_ID,
                    object : PlayerNotificationManager.MediaDescriptionAdapter {
                        override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                            return PendingIntent.getActivity(
                                this@AudioPlayerService,
                                0,
                                Intent(this@AudioPlayerService, AudioServiceActivity::class.java),
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }

                        override fun getCurrentContentText(player: Player?): String? {
                            return SampleURI.samples[player?.currentWindowIndex ?: 0].description
                        }

                        override fun getCurrentContentTitle(player: Player?): String {
                            return SampleURI.samples[player?.currentWindowIndex ?: 0].title
                        }

                        override fun getCurrentLargeIcon(
                            player: Player?,
                            callback: PlayerNotificationManager.BitmapCallback?
                        ): Bitmap? {
                            return SampleURI.getBitmap(
                                this@AudioPlayerService,
                                SampleURI.samples[player?.currentWindowIndex ?: 0].bitmapRes
                            )
                        }

                    })

        playerNotificationManager?.setNotificationListener(object :
            PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }

            override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
                startForeground(notificationId, notification)
            }
        })

        playerNotificationManager?.setPlayer(player)

        // provide an info to the lock screen
        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG)
        mediaSession.isActive = true
        playerNotificationManager?.setMediaSessionToken(mediaSession.sessionToken)
        mediaSessionConnector = MediaSessionConnector(mediaSession)

        // the timeline is the internal representation of the playlist
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player?,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return SampleURI.getMediaDescription(
                    this@AudioPlayerService,
                    SampleURI.samples[windowIndex]
                )
            }
        })

        // connect the player to sync the state
        mediaSessionConnector.setPlayer(player, null)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaSession.release()
        mediaSessionConnector.setPlayer(null, null)
        playerNotificationManager?.setPlayer(null)
        player?.release()
        player = null
    }

    companion object {
        const val PLAYBACK_CHANNEL_ID = "playback_channel"
        const val PLAYBACK_NOTIFICATION_ID = 1
        const val MEDIA_SESSION_TAG = "audio_demo"
    }
}