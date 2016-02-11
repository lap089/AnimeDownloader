package com.example.user.animedownloader;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button downloadButton;
    EditText input;
    TextView supportedList;
  public static  WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.down);
        input = (EditText) findViewById(R.id.input);
        webView = (WebView) findViewById(R.id.web);
        supportedList = (TextView) findViewById(R.id.supported_list);
        webView.setVisibility(View.GONE);


        supportedList.setText(EngineManager.getSupportedSites());


//        MainActivity.webView.getSettings().setJavaScriptEnabled(true);
//        MainActivity.webView.getSettings().setLoadsImagesAutomatically(true);
//        MainActivity.webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");


//        MainActivity.webView.setWebViewClient(new WebViewClient() {
//                                                  @Override
//                                                  public void onPageFinished(WebView view, final String url) {
//                                                      view.loadUrl("javascript:(function(){document.getElementById('yuklen').click();})()");
//
//                                                      view.setWebViewClient(new WebViewClient() {
//                                                          public void onPageFinished(WebView view, String url) {
//
//                                                              view.loadUrl("javascript:window.HtmlViewer.showHTML" +
//                                                                      "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
//                                                          }
//                                                      });
//
//
//                                                      //   Log.d("Check jsoup:", url);
//                                                  }
//                                              }
//        );






        downloadButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input link please!", Toast.LENGTH_SHORT).show();
                    return;
                }



            String  url = "https://kissanime.to/Anime/Tayutama/Episode-005?id=39759";
                         EngineManager engineManager = new EngineManager(MainActivity.this,url);
                        engineManager.execute();


//                String url = "http://justdubsonline.net/please-teacher-ova#ep1-tab";
//                new AnimeJustdubsonline(MainActivity.this).loadDataAsync(url);

//                String downloadLink = "http://hentaiplanet.info/RAW/Rance_01_Hikari_wo_Motomete_The_Animation_Episode_3_RAW.mp4";
//                downloadLink = "http://videos02.hentaiupload.com/videos/9f01795347f1059abf22f4a39fbde729/565afe92.mp4";
//                downloadLink = "http://37.48.81.2/B/BoyMeetsHaremTheAnimationVol.1.mp4?st=600JDtsOiH321Pyg6K4DKg&e=1454477171";
//                downloadLink = "http://cdn.oose.io/i0quBBG300pB4se0vs03jQEoTj628TTUj6fjc7YBItHV7H2ExTj5Yy1gNMdlYOrV/video.mp4";
//                downloadLink = "http://37.48.81.2/B/BoyMeetsHaremTheAnimationVol.1.mp4?st=kjklBkRuvsx4d-2tZn1wqA&e=1454486324";
//                downloadLink = "http://hentaiplanet.info/archive/Kodomo no Jikan - 07 (Raw) [6E400530].mp4";
//                downloadLink = "http://hentaiplanet.info/archive/Kodomo%20no%20Jikan%20-%2007%20(Raw)%20%5B6E400530%5D.mp4";
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
//                startActivity(intent);

            }
        });









    }




    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
                 new android.support.v7.app.AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                         .setPositiveButton(android.R.string.ok, null).setCancelable(true).create().show();


               Log.d("Check Html: ", html);
        }

    }


}
