package com.example.user.animedownloader.Engines;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by user on 1/20/2016.
 */
public class AnimeRyuanime {

    int type = 0;       // 1: myvidstream    2: mp4engine    //3: fs1.mp4engine
    String url;
    String downloadLink;
    boolean isType3 = false;
    Context context;
    ProgressDialog mProgressDialog;

   public AnimeRyuanime(Context context)
   {
        this.context = context;

   }



    public void loadDataAsync(String url) {
        this.url = url;
       // url = "http://www.ryuanime.com/watch/anime/dubbed/seraph-of-the-end-battle-in-nagoya-episode-4";
          new GetDownloadLinkAsync().execute(url);
    }



    private class GetDownloadLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog

            if (mProgressDialog != null)
                mProgressDialog = null;
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Load Data");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            Document doc;

            try {
                doc = Jsoup.connect(url[0]).get();

                Elements link = doc.select("[SRC]");
                String videoUrl = "";
                for (Element scr : link) {
                    if (scr.attr("src").contains("myvidstream")) {
                        videoUrl = scr.attr("src");
                        type = 1;
                    } else if (scr.attr("src").contains("mp4engine")) {
                        videoUrl = scr.attr("src");
                        type = 2;
                    }
                }
                if (videoUrl.isEmpty()) return "";
                doc = Jsoup.connect(videoUrl).get();


                Elements jstext = doc.select("div[id=player_code]");
                Document script = Jsoup.parseBodyFragment(jstext.html());
                Elements javascript = script.select("script[type=text/javascript]");

                String temp = javascript.last().toString();


                int i = 0;
                String id = "";
                for (String a : temp.split("\\|")) {
                    if (i == 0) {
                        ++i;
                        continue;
                    }
                    if (a.length() > id.length())
                        id = a;
                    if(a.compareTo("fs1")==0)
                        isType3 = true;
                    Log.d("Check split:",a);

                }


                return id;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog

            mProgressDialog.dismiss();



            if (type == 1) {
                String template = "http://srv5.myvidstream.net:182/d/";
                String dataType = ".mp4";
                String name = "AnimeDownloader";
                downloadLink = template + result + "/" + name + dataType + "?start=0";

            } else if(isType3){
                String template = "http://fs1.mp4engine.com:182/d/";
                String dataType = ".mp4";
                String name = "AnimeDownloader";
                downloadLink = template + result + "/" + name + dataType;
            }
            else
            {
                String template = "http://mp4engine.com:182/d/";
                String dataType = ".mp4";
                String name = "AnimeDownloader";
                downloadLink = template + result + "/" + name + dataType;
            }

            if (result == "")
                downloadLink = "Fail!";


               Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
                context.startActivity(intent);


        }
    }




}
