package com.exoextensiontest.audio

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.exoextensiontest.R
import com.exoextensiontest.SampleURI
import com.google.android.exoplayer2.util.Util


class AudioServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_service)

        val intent = Intent(this, AudioPlayerService::class.java)
        Util.startForegroundService(this, intent)

        val listView: ListView = findViewById(R.id.list_view)
        listView.adapter =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, SampleURI.samples)
    }
}
