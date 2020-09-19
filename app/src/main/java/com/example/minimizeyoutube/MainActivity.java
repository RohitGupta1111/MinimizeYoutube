package com.example.minimizeyoutube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.minimizeyoutube.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView webView;
    private Button btnPause,btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent i = new Intent(MainActivity.this,ServiceWebView.class);
//        startService(i);
        webView = findViewById(R.id.youtube_webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    String html = URLDecoder.decode(String.valueOf(request.getUrl()),"UTF-8").substring(9);
                    Log.d(TAG, "shouldOverrideUrlLoading: Url" + String.valueOf(request.getUrl()));
                    Log.d(TAG, "shouldOverrideUrlLoading: html" + html);
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "error");
                    e.printStackTrace();
                }

                return false;
            }
        });
        webView.loadUrl("https://www.youtube.com");
//        btnPause.setOnClickListener(this);
//        btnPlay.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        if(v.getId() == R.id.btn_pause) {
//            webView.evaluateJavascript("function() { document.querySelector('.ytp-play-button').click();}()",null);
//        } else {
//            webView.evaluateJavascript("document.querySelector('.player-control-play-pause-icon').click()",null);
//        }
//
//    }
}
