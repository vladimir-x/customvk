package com.example.dude.dvk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    Browser webClient;

    HashMap<String, byte[]> cache = new HashMap<>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webClient.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.dude.dvk.R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        webView = (WebView) findViewById(com.example.dude.dvk.R.id.mainweb);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setJavaScriptEnabled(true);
        //  webView.getSettings().setPluginState(WebSettings.PluginState.ON);


        webClient = new Browser() {
            @Override
            public void startActivityForResultInner(Intent intent, int requestCode) {
                startActivityForResult(intent, requestCode);
            }

        };
        webView.setWebChromeClient(webClient);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }


            //@Override
            public WebResourceResponse __shouldInterceptRequest(WebView view, WebResourceRequest request) {
                try {

                    String urlStr = request.getUrl().toString();
                    /*
                    if (urlStr.contains(".css?") || urlStr.contains(".js?") || urlStr.contains(".jpg?")) {
                        if (cache.containsKey(urlStr)) {
                            return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream(cache.get(urlStr)));
                        }
                    }*/
                    URL url = new URL(urlStr);

                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();


                    CookieManager cookieManager = CookieManager.getInstance();
                    urlConnection.setDoOutput(true);


                    for (String k : request.getRequestHeaders().keySet()) {
                        urlConnection.setRequestProperty(k, request.getRequestHeaders().get(k));
                    }

                    //urlConnection.setRequestProperty("CRM_USER_AGENT", "crm_app");
                    urlConnection.setRequestMethod(request.getMethod());

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                    // data = new java.util.Scanner(in).useDelimiter("\\A").next();

                    // System.out.println("Data:" + data);


                    String ct = urlConnection.getContentType();
                    String ce = urlConnection.getContentEncoding();

                    urlConnection.disconnect();
/*
                    if (urlStr.contains(".css?") || urlStr.contains(".js?") || urlStr.contains(".jpg?")) {

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        cache.put(urlStr,)
                        if (cache.containsKey(urlStr)) {
                            return new WebResourceResponse("text/html", "UTF-8", new ByteArrayInputStream(cache.get(urlStr)));
                        }
                    }*/

                    if (catchIt) {
                        catchIt = false;
                        String cr = readIS(in);
                        System.out.println(cr);
                        in = new ByteArrayInputStream(cr.getBytes());
                    }
                    return new WebResourceResponse(ct, ce, in);

                } catch (Exception e) {
                    e.printStackTrace();

                    return super.shouldInterceptRequest(view, request);
                }
            }

        });
        webView.loadUrl("https://m.vk.com");

        //webView.loadUrl("https://google.com");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
        // your code.
    }


    private static boolean catchIt = false;

    public void butHello_Click(View v) {

        catchIt = true;
        webView.loadUrl("https://vk.com/al_audio.php?act=section&al=1&is_layer=0&owner_id=75615781&section=all");


        //Intent myIntent = new Intent(this, MusicList.class);
        //startActivity(myIntent);
    }


    private static String readIS(InputStream is) {

        StringBuilder sb = new StringBuilder();
        byte buff[] = new byte[1024];

        try {
            int n = 0;
            while ((n = is.read(buff)) > 0) {
                sb.append(new String(buff, 0, n, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
