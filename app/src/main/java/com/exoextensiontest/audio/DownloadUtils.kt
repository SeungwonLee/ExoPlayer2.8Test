package com.exoextensiontest.audio

import android.content.Context
import com.exoextensiontest.R
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File


class DownloadUtils {
    companion object {
        private var cache: Cache? = null
        private var downloadManager: DownloadManager? = null

        @Synchronized
        fun getCache(context: Context): Cache? {
            if (cache == null) {
                val cacheDirectory = File(context.getExternalFilesDir(null), "downloads")
                cache = SimpleCache(cacheDirectory, NoOpCacheEvictor())
            }
            return cache
        }

        @Synchronized
        fun getDownloadManager(context: Context): DownloadManager? {
            if (downloadManager == null) {
                val actionFile = File(context.externalCacheDir, "actions")
                downloadManager = DownloadManager(
                    getCache(context),
                    DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.application_name))
                    ),
                    actionFile,
                    ProgressiveDownloadAction.DESERIALIZER
                )
            }
            return downloadManager
        }
    }
}