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



               // String url = "http://www.animetycoon.net/canaan-episode-6/";
            //        url = "http://www.animetycoon.net/dagger-of-kamui/";
                         EngineManager engineManager = new EngineManager(MainActivity.this,input.getText().toString());
                        engineManager.execute();


//                String url = "http://justdubsonline.net/please-teacher-ova#ep1-tab";
//                new AnimeJustdubsonline(MainActivity.this).loadDataAsync(url);

//                String downloadLink = "http://us-11.blastvideo.ws/file.php?v_id=737dfa253270d83924bfadc354d3643379fd1e25/Ul51u3T8KY&res=360P";
//                downloadLink = "http://node4.uploadcrazy.net/dl/shura-no-toki03.mp4?st=wOyO0IT-gwBxIq9QkjEVUQ&e=1454011281";
//               downloadLink = "http://www.animefreak.tv/sites/all/modules/custom/anime/stream.php?file=Bubuki+Buranki+Episode+1.mp4&e=h&st=h";
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
