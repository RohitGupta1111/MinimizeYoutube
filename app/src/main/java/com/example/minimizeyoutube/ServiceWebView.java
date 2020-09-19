package com.example.minimizeyoutube;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ServiceWebView extends Service implements WebViewInteraction{
    private static final String TAG = "ServiceWebView";

    public static final String CHANNEL_ID = "WebViewChannel";
    private PlayerNotification playerNotification;
    WindowManager windowManager;
    WebView webView;
    private BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d(TAG, "onReceive: " + intent.getStringExtra("actionName"));
            String action = intent.getStringExtra("actionName");
            switch (action) {
                case PlayerNotification.ACTION_NEXT:
                    onNext();
                    break;
                case PlayerNotification.ACTION_PLAY:
                    onPlay();
                    break;
                case PlayerNotification.ACTION_PREVIOUS:
                    onPrev();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"Service onCreate");
        registerReceiver(broadcastReceiver,new IntentFilter("HANDLE_TRACKS"));
        playerNotification = new PlayerNotification(ServiceWebView.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playerNotification.createNotificationChannel();
        Notification notification = playerNotification.createNotification();
        startForeground(1,notification);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        params.width = displayMetrics.widthPixels;
        params.height = displayMetrics.heightPixels;

        LinearLayout view = new LinearLayout(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        view.addView(webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }
        });
        webView.loadUrl("https://www.youtube.com");
        windowManager.addView(view, params);
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        webView.destroy();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onPlay() {
        webView.evaluateJavascript("document.querySelector('.player-control-play-pause-icon').click()",null);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onNext() {

    }

    @Override
    public void onPrev() {

    }
}
