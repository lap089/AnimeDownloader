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


     //           String url = "http://www.watchcartoononline.com/shomin-sample-episode-11-english-dubbed";
     //           new AnimeWatchcartoononline(MainActivity.this).loadDataAsync(url);
//                String downloadLink = "http://lb.watchanimesub.net/video/Shomin%20Sample%20English%20Dubbed/PHF%20Shomin%20Sample%20-%201x11%20-%20Is%20This%20Not%20the%20Sky.mp4?st=BJn6vuFTLYI1osZ2-eKrJw&e=1453730106";
//downloadLink = "http://media30.watchanimesub.net/video/Youkai%20Watch%20English%20Dubbed/Yo-Kai%20Watch%20-%20026%20-%20Yo-Kai%20Espy-Yo-Kai%20Peckpocket-Komasan%20in%20Love%20Episode%205.mp4?st=e0ix1hUal9GGmo7K7g_nIw&e=1453729528";
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
