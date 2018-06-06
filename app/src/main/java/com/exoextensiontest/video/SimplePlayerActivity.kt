package com.exoextensiontest.video

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.exoextensiontest.R
import com.exoextensiontest.SampleURI.Companion.HTTP_MP4_URI
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class SimplePlayerActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_player)

        playerView = findViewById(R.id.player_view)
    }


    override fun onStart() {
        super.onStart()

        // initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        playerView.player = player

        // load the HTTP url
        val dataSource = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exo-demo"))
        val mediaSource =
            ExtractorMediaSource.Factory(dataSource).createMediaSource(Uri.parse(HTTP_MP4_URI))

        player?.prepare(mediaSource)
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()

        playerView.player = null
        player?.release()
        player = null
    }
}
