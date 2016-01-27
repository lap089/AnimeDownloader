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

              //  String url = "http://www.animepalm.net/afro-samurai-episode-1-english-dubbed/";
              //  url = "http://www.animepalm.net/agent-aika-episode-2-english-dubbed/";
                         EngineManager engineManager = new EngineManager(MainActivity.this,input.getText().toString());
                        engineManager.execute();

//                String url = "http://justdubsonline.net/please-teacher-ova#ep1-tab";
//                new AnimeJustdubsonline(MainActivity.this).loadDataAsync(url);
//
//                String downloadLink = "https://r12---sn-npo7en7d.googlevideo.com/videoplayback?id=d4b6c8428d7a23b6&itag=22&source=picasa&requiressl=yes&mm=30&mn=sn-npo7en7d&ms=nxu&mv=m&nh=IgpwcjAyLnNpbjAzKgkxMjcuMC4wLjE&pl=24&mime=video/mp4&lmt=1437166660540831&mt=1453888309&ip=115.78.64.41&ipbits=8&expire=1453917172&sparams=ip,ipbits,expire,id,itag,source,requiressl,mm,mn,ms,mv,nh,pl,mime,lmt&signature=09A8D98F447BFA029E3CA235C48E8933BA16E955.719865C49F0AA4FDA358765AE14BEFC1B75FDAA3&key=ck2";
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
