package com.example.minimizeyoutube;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PendingPlayerBroadcast extends BroadcastReceiver {
    private static final String TAG = "PendingPlayerBroadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        context.sendBroadcast(new Intent("HANDLE_TRACKS").putExtra("actionName",intent.getAction()));
    }
}
