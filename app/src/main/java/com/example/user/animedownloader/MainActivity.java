package com.example.user.animedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.animedownloader.Engines.AnimeRyuanime;

public class MainActivity extends AppCompatActivity {
    Button downloadButton;
    EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.down);
        input = (EditText) findViewById(R.id.input);



        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(input.getText().toString().isEmpty())
            {
                Toast.makeText(MainActivity.this,"Input link please!",Toast.LENGTH_SHORT).show();
                return;
            }

             AnimeRyuanime anime =    new AnimeRyuanime(MainActivity.this);
                anime.loadDataAsync(input.getText().toString());


        //         AnimeGogo_tv anime = new AnimeGogo_tv(MainActivity.this);
       // anime.loadDataAsync(input.getText().toString());

       //         AnimeNova anime = new AnimeNova(MainActivity.this);
       //         anime.loadDataAsync(input.getText().toString());


//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
//
//                startActivity(intent);

            }
        });





    }
}
