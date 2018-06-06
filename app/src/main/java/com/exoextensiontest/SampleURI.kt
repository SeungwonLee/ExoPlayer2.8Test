package com.exoextensiontest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat


class SampleURI {
    companion object {
        const val HTTP_MP4_URI = "https://html5demos.com/assets/dizzy.mp4"
        const val AD_TAG_URI =
            "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostlongpod&cmsid=496&vid=short_tencue&correlator="
        val samples = arrayOf(Audio.Track1, Audio.Track2, Audio.Track3)

        fun getMediaDescription(context: Context, sample: Audio): MediaDescriptionCompat {
            val extras = Bundle()
            val bitmap = getBitmap(context, sample.bitmapRes)
            extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
            extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
            return MediaDescriptionCompat.Builder()
                .setMediaId(sample.mediaId)
                .setIconBitmap(bitmap)
                .setTitle(sample.title)
                .setDescription(sample.description)
                .setExtras(extras)
                .build()
        }

        fun getBitmap(context: Context, @DrawableRes bitmapResource: Int): Bitmap {
            return (context.resources.getDrawable(bitmapResource) as BitmapDrawable).bitmap
        }
    }

    enum class Audio(
        var uri: Uri,
        var mediaId: String,
        var title: String,
        var description: String,
        var bitmapRes: Int
    ) {
        Track1(
            Uri.parse("http://storage.googleapis.com/automotive-media/Jazz_In_Paris.mp3"),
            "audio_1",
            "Jazz in Paris",
            "Jazz for the masses",
            R.drawable.album_art_1
        ),
        Track2(
            Uri.parse("http://storage.googleapis.com/automotive-media/The_Messenger.mp3"),
            "audio_2",
            "The messenger",
            "Hipster guide to London",
            R.drawable.album_art_2
        ),
        Track3(
            Uri.parse("http://storage.googleapis.com/automotive-media/Talkies.mp3"),
            "audio_3",
            "Talkies",
            "If it talks like a duck and walks like a duck.",
            R.drawable.album_art_3
        ),
        ;
    }
}
