package com.exoextensiontest.audio.cache

import android.app.Notification
import com.exoextensiontest.R
import com.exoextensiontest.audio.DownloadUtils
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationUtil

class AudioDownloadService : DownloadService(
    DOWNLOAD_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_CHANNEL_ID, R.string.download_channel_name
) {
    override fun getDownloadManager(): DownloadManager? {
        return DownloadUtils.getDownloadManager(this)
    }

    override fun getForegroundNotification(taskStates: Array<out DownloadManager.TaskState>?): Notification {
        return DownloadNotificationUtil.buildProgressNotification(
            this,
            R.drawable.exo_icon_play,
            DOWNLOAD_CHANNEL_ID,
            null,
            null,
            taskStates
        )
    }

    override fun getScheduler(): Scheduler? {
        return null
    }

    companion object {
        const val DOWNLOAD_CHANNEL_ID = "download_channel"
        const val DOWNLOAD_NOTIFICATION_ID = 2
    }
}