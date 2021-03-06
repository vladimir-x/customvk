package com.example.dude.dvk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    WebView webView;

    Browser webClient;

    HashMap<String, byte[]> cache = new HashMap<>();

    CookieManager manager = CookieManager.getInstance();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webClient.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        webView = (WebView) findViewById(R.id.mainweb);
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
                return true;
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                try {


                    String urlStr = request.getUrl().toString();

                    //if(urlStr.contains(".js") ) { System.out.println(">> !!!!!!!!!!!!!!!!!!!! " + urlStr); }

                    if (urlStr.contains("common.js")) {

                        URL url = new URL(urlStr);

                        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                        //urlConnection.setDoOutput(true);


                        for (String k : request.getRequestHeaders().keySet()) {
                            urlConnection.setRequestProperty(k, request.getRequestHeaders().get(k));
                        }

                        urlConnection.setRequestMethod(request.getMethod());

                        if (!cache.containsKey(urlStr)){

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            String jsContent = readIS(in);

                            cache.put(urlStr,patchCommonJs_flag_subscribe2(jsContent).getBytes());
                        }


                        String ct = urlConnection.getContentType();
                        String ce = urlConnection.getContentEncoding();

                        return new WebResourceResponse(ct, ce, new ByteArrayInputStream(cache.get(urlStr)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return super.shouldInterceptRequest(view, request);
            }

        });
        webView.loadUrl("https://m.vk.com");


        /*
        // таймер для отладки
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String c = manager.getCookie("m.vk.com");
                int st = c.indexOf("remixaudio_background_play_time_=");
                System.out.println(">> control:"  + c.substring(st));


            }
        }, 2000, 3000);
        */



    }

    /**
     * В функцию инициализации плеера подставляет true вместо  "window.audioSubscribe"
     *
     * Время remixaudio_background_play_time_ не тикает. Треки не паузятся.
     * @param input
     * @return
     */
    public String patchCommonJs_flag_subscribe2(String input){

        int marker = input.indexOf("remixaudio_background_play_time_");


        int st_2 =   input.substring(0,marker).lastIndexOf("window.audioSubscribe");
        int en = st_2 + "window.audioSubscribe".length();

        String A = input.substring(0,st_2);
        String B = input.substring(en);

        StringBuilder sb = new StringBuilder();
        sb.append(A);
        sb.append("true");
        sb.append(B);

        return sb.toString();
    }



    /**
     * Вырезает таймер.
     * Время remixaudio_background_play_time_ не тикает. Треки не паузятся.
     * @param input
     * @return
     */
    public String patchCommonJs_cut_timer(String input){
        int marker = input.indexOf("remixaudio_background_play_time_");

        int start_f_1 = input.indexOf("onTick",marker);
        int start_f_2 = input.indexOf("{",start_f_1);

        int end_mark = input.indexOf("onEnd",start_f_1);

        int end_f_1 =  end_mark;
        while (input.charAt(end_f_1)!='}'){
            end_f_1--;
        }


        String A = input.substring(0,start_f_2+1);
        String B = input.substring(end_f_1);

        StringBuilder sb = new StringBuilder();
        sb.append(A);
        sb.append(B);
        return sb.toString();

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


    private static boolean catchIt = true;

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