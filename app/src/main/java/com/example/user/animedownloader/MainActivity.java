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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button downloadButton;
    EditText input;
  public static  WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.down);
        input = (EditText) findViewById(R.id.input);
        webView = (WebView) findViewById(R.id.web);
        webView.setVisibility(View.VISIBLE);


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



//        String url = "http://www.watchcartoononline.com/aki-sora-yume-no-naka-episode-1-english-subbed";
//        new AnimeWatchcartoononline(this).loadDataAsync(url);



        downloadButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Input link please!", Toast.LENGTH_SHORT).show();
                    return;
                }

                      EngineManager engineManager = new EngineManager(MainActivity.this, input.getText().toString());
                        engineManager.execute();


         //       String url = "http://www.watchcartoononline.com/angel-tales-episode-2-english-dubbed";
         //       new AnimeWatchcartoononline(MainActivity.this).loadDataAsync(url);


//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
//
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
