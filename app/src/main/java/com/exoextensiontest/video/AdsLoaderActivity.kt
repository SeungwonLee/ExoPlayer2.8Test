package com.exoextensiontest.video

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.exoextensiontest.R
import com.exoextensiontest.SampleURI
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class AdsLoaderActivity : AppCompatActivity() {

    private var adsLoader: ImaAdsLoader? = null
    private lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_loader)

        playerView = findViewById(R.id.ad_player_view)

        // ads loader contains information about which ad have already been played, we don’t show the same ads multiple times.
        adsLoader = ImaAdsLoader(this, Uri.parse(SampleURI.AD_TAG_URI))
    }

    override fun onStart() {
        super.onStart()

        // initialize the player
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        playerView.player = player

        // load the HTTP url
        val dataSource = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exo-demo"))
        val mediaSource =
            ExtractorMediaSource.Factory(dataSource).createMediaSource(
                Uri.parse(
                    SampleURI.HTTP_MP4_URI
                )
            )

        val adsMediaSource =
            AdsMediaSource(mediaSource, dataSource, adsLoader, playerView.overlayFrameLayout)

        player?.prepare(adsMediaSource)
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()

        playerView.player = null
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()

        adsLoader = null
    }
}