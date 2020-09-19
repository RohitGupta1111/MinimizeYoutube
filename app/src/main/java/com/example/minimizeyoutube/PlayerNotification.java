package com.example.minimizeyoutube;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

public class PlayerNotification {

    public static final String CHANNEL_ID = "WebViewChannel";

    public static final String ACTION_NEXT = "PlayNextInYoutube";
    public static final String ACTION_PREVIOUS = "PlayPrevioudInYoutube";
    public static final String ACTION_PLAY = "PlayInYoutube";
    private Notification notification;

    private Context mServiceContext,mActivityContext;
    private MediaSessionCompat mediaSessionCompat = null;
    private PendingIntent playPreviousIntent = null;
    private PendingIntent playNextIntent = null;
    private PendingIntent playIntent = null;
    private PendingIntent activityIntent = null;
    PlayerNotification(Context mServiceContext) {
        this.mServiceContext = mServiceContext;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = mServiceContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public Notification createNotification () {

        Bitmap icon = BitmapFactory.decodeResource(mServiceContext.getResources(),R.drawable.sample_thumbnail);

        if(mediaSessionCompat == null) {
            mediaSessionCompat = new MediaSessionCompat(mServiceContext,"tag");
        }

        if(playPreviousIntent == null) {
            Intent i = new Intent(mServiceContext,PendingPlayerBroadcast.class).setAction(ACTION_PREVIOUS);
            playPreviousIntent = PendingIntent.getBroadcast(mServiceContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(playIntent == null) {
            Intent i = new Intent(mServiceContext,PendingPlayerBroadcast.class).setAction(ACTION_PLAY);
            playIntent = PendingIntent.getBroadcast(mServiceContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(playNextIntent == null) {
            Intent i = new Intent(mServiceContext,PendingPlayerBroadcast.class).setAction(ACTION_NEXT);
            playNextIntent = PendingIntent.getBroadcast(mServiceContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        if(activityIntent == null) {
            Intent i = new Intent(mServiceContext, MainActivity.class);
            activityIntent = PendingIntent.getActivity(mServiceContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        }

        notification = new NotificationCompat.Builder(mServiceContext,CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                            .setContentTitle("XYZ Video")
                            .setContentText("Video Playing")
                            .setLargeIcon(icon)
                            .setOnlyAlertOnce(true)
                            .setShowWhen(false)
                            .addAction(R.drawable.ic_baseline_skip_previous_24,"previous",playPreviousIntent)
                            .addAction(R.drawable.ic_baseline_play_arrow_24,"play",playIntent)
                            .addAction(R.drawable.ic_baseline_skip_next_24,"next",playNextIntent)
                             .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                  .setShowActionsInCompactView(0,1,2)
                                  .setMediaSession(mediaSessionCompat.getSessionToken()))
                             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                             .setContentIntent(activityIntent)
                             .build();
        return notification;
    }
}
