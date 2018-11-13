package com.example.dude.androapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView webView ;

    Browser webClient;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webClient.onActivityResult(requestCode,resultCode,intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        webView = (WebView)findViewById(R.id.mainweb);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
      //  webView.getSettings().setPluginState(WebSettings.PluginState.ON);


        webClient = new Browser() {
            @Override
            public void startActivityForResultInner(Intent intent, int requestCode) {
                startActivityForResult(intent,requestCode);
            }

        };
        webView.setWebChromeClient(webClient);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

        });

        webView.loadUrl("https://m.vk.com");
        //webView.loadUrl("https://google.com");





    }
}
