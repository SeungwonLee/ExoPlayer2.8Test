package com.exoextensiontest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.exoextensiontest.audio.AudioServiceActivity
import com.exoextensiontest.audio.cache.CacheAudioServiceActivity
import com.exoextensiontest.video.AdsLoaderActivity
import com.exoextensiontest.video.SimplePlayerActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.video_player_test).setOnClickListener {
            startActivity(Intent(this, SimplePlayerActivity::class.java))
        }

        findViewById<Button>(R.id.video_ads_test).setOnClickListener {
            startActivity(Intent(this, AdsLoaderActivity::class.java))
        }

        findViewById<Button>(R.id.audio_test).setOnClickListener {
            startActivity(Intent(this, AudioServiceActivity::class.java))
        }

        findViewById<Button>(R.id.audio_download_test).setOnClickListener {
            startActivity(Intent(this, CacheAudioServiceActivity::class.java))
        }

    }

}
