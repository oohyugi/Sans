package com.yogi.core.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationManagerCompat
import at.huber.youtubeExtractor.VideoMeta
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.yogi.core.R
import com.yogi.core.model.PlayerMetaMdl
import com.yogi.core.model.YtItemsMdl
import com.yogi.core.utils.ExoPlayerManager2


/**
 * Created by oohyugi on 2019-08-26.
 * github: https://github.com/oohyugi
 */
class AudioPlayerService : Service() {
    private var mPlayer: SimpleExoPlayer? = null
    private var mPlayerNotificationManager: PlayerNotificationManager? = null
    private var mMediaSessionConnector: MediaSessionConnector? = null
    private var mediaSession: MediaSessionCompat? = null
    private var exoPlayerManager: ExoPlayerManager2? = null
    var mVideoMeta: VideoMeta? = null

    companion object {
        const val TAG_AUDIO_LIST = "audiolist"
        const val TAG_POSITION_PLAYED = "pos"
        fun startThisService(context: Context, list: List<YtItemsMdl>?, positionPlayed: Int) {
            val intent = Intent(context, AudioPlayerService::class.java)
            intent.putExtra(TAG_AUDIO_LIST, Gson().toJson(list))
            intent.putExtra(TAG_POSITION_PLAYED, positionPlayed)
            Util.startForegroundService(context, intent)

        }
        fun startThisService(context: Context?) {
            val intent = Intent(context, AudioPlayerService::class.java)
            Util.startForegroundService(context, intent)

        }

        fun stopService(context: Context) {
            val intent = Intent(context, AudioPlayerService::class.java)
            context.stopService(intent)
        }
    }


    override fun onCreate() {
        super.onCreate()
        val context = this
        exoPlayerManager = ExoPlayerManager2.with(this)

        Log.wtf("service", "start")


    }

    override fun onBind(intent: Intent?): IBinder? {


        return null
    }

    override fun onDestroy() {

        exoPlayerManager?.mediaSession?.release()
        exoPlayerManager?.mMediaSessionConnector?.setPlayer(null)
        mPlayerNotificationManager?.setPlayer(null)
        mPlayer?.release()
        mPlayer = null
        Log.wtf("service", "stop")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mPlayer = ExoPlayerManager2.with(this).getPlayer()
        displayNotification()
        return START_STICKY
    }


    @SuppressLint("WrongConstant")
    private fun displayNotification() {
        mPlayerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            "CHANNEL ID",
            R.string.application_name,
            R.string.application_name,
            123,
            object : MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return exoPlayerManager?.getTitle()!!
                }

                @Nullable
                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                @Nullable
                override fun getCurrentContentText(player: Player): String? {
                    return exoPlayerManager?.getSubTitle()
                }

                @Nullable
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: BitmapCallback
                ): Bitmap? {

                    var mBitmap: Bitmap? = null
                    Glide.with(this@AudioPlayerService)
                        .asBitmap()
                        .load(exoPlayerManager?.getUrlImg())
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                mBitmap = resource
                            }

                        })

                    return mBitmap
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationStarted(
                    notificationId: Int,
                    notification: Notification
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(notificationId: Int) {
                    stopSelf()
                }
            }
        )
        mPlayerNotificationManager?.setPriority(NotificationManagerCompat.IMPORTANCE_NONE)

        mPlayerNotificationManager?.setPlayer(exoPlayerManager?.getPlayer())

        mediaSession = MediaSessionCompat(this, "mediaSession")
        mediaSession?.isActive = true
        mPlayerNotificationManager?.setMediaSessionToken(mediaSession?.sessionToken)

        mMediaSessionConnector = MediaSessionConnector(mediaSession)
        mMediaSessionConnector?.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return getMediaDescription(exoPlayerManager?.getPlayerMeta()!!)
            }
        })

        mMediaSessionConnector?.setPlayer(exoPlayerManager?.getPlayer())
        mPlayerNotificationManager?.setFastForwardIncrementMs(0)
        mPlayerNotificationManager?.setRewindIncrementMs(0)
        mPlayerNotificationManager?.setUseNavigationActions(false)

    }





    fun getMediaDescription(item: PlayerMetaMdl): MediaDescriptionCompat {
        val extras = Bundle()


        return MediaDescriptionCompat.Builder()
            .setMediaId(item.id)
            .setTitle(item.title)
            .setDescription(item.subTitle)
            .setExtras(extras)
            .build()
    }

}