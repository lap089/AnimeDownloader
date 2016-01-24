package com.example.user.animedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        webView.setVisibility(View.GONE);




       // new AnimeKissanime(this).loadDataAsync("https://kissanime.to/Anime/Umishou/Episode-004?id=41080");





        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(input.getText().toString().isEmpty())
            {
                Toast.makeText(MainActivity.this,"Input link please!",Toast.LENGTH_SHORT).show();
                return;
            }

             EngineManager engineManager = new EngineManager(MainActivity.this,input.getText().toString());
                engineManager.execute();



//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
//
//                startActivity(intent);

            }
        });







    }



}
