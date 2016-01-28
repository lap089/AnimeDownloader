package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import com.example.user.animedownloader.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 1/28/2016.
 */
public class AnimeAnimetycoon {



    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    android.content.Context context;
    ProgressDialog mProgressDialog;
    String title = "AnimeDownloader";
    public AnimeAnimetycoon(Context context)
    {
        this.context = context;
        downloadOptions = new ArrayList<>();
        linkList = new ArrayList<>();

    }


    public void loadDataAsync(String url) {
        this.url = url;
        if (mProgressDialog != null)
            mProgressDialog = null;
        mProgressDialog = new ProgressDialog(context);
        // Set progressdialog title
        mProgressDialog.setTitle("Load Data");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        // Show progressdialog
        mProgressDialog.show();
        // url = "http://www.ryuanime.com/watch/anime/dubbed/seraph-of-the-end-battle-in-nagoya-episode-4";
        new GetDownloadLinkAsync().execute(url);
    }



    private class GetDownloadLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog


        }

        @Override
        protected String doInBackground(String... url) {


            try {
                int firstindex = 0,lastindex = 0;

                Document doc;
             //   String url = "http://www.animetycoon.net/outlaw-star-episode-4/";

                doc = Jsoup.connect(url[0]).get();
                title = doc.select("title").text();
                Elements text_centers = doc.select("div[class=entry-content text-center margin-bottom-10]");
                for(Element text_center: text_centers)
                {
                    String script =  text_center.select("script").first().toString();
                    firstindex = script.indexOf("iframe src") + 12;
                    lastindex= firstindex;
                    while(script.charAt(lastindex) != '\"') ++lastindex;
                    String videolink = script.substring(firstindex,lastindex);
             //       Println(videolink);



                    doc = Jsoup.connect(videolink).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();


                    if(videolink.contains("justmp4")){
                        videolink = videolink.replace("justmp4", "embed.justmp4");
                        String value = doc.select("input").attr("value");
                        //   Println(value);
                        doc = Jsoup.connect(videolink)
                                .data("id",value)
                                .post();

                        //    Println(doc);
                        Element video_wrap = doc.select("div[id=video_wrap]").first();

                        //     Println(video_wrap);

                        String videowrap = video_wrap.toString();

                        firstindex =  videowrap.indexOf("file:");
                        while(firstindex!=-1){
                            firstindex += 7;
                            lastindex = firstindex;
                            while(videowrap.charAt(lastindex) != '\'') ++lastindex;
                            //       Println(videowrap.substring(firstindex,lastindex));
                            downloadLink = videowrap.substring(firstindex,lastindex);

                            firstindex = videowrap.indexOf("label:",lastindex) + 8;
                            lastindex = firstindex;
                            while(videowrap.charAt(lastindex) != '\'') ++lastindex;
                            String lable = videowrap.substring(firstindex,lastindex);
                            //       Println(videowrap.substring(firstindex,lastindex));
                            linkList.add(downloadLink);
                            downloadOptions.add(title + " - Justmp4(HD)" + " - " + lable);

                            firstindex = videowrap.indexOf("file:",lastindex);
                        }

                    }

                    else if(videolink.contains("uploadcrazy"))
                    {
                        script = doc.toString();
                        firstindex =  script.indexOf("file:") + 7;
                        lastindex = firstindex;
                        while(script.charAt(lastindex) != '\'') ++lastindex;
                        downloadLink = script.substring(firstindex,lastindex);
                        linkList.add(downloadLink);
                        downloadOptions.add(title + " - uploadcrazy");

                       // Println(script.substring(firstindex,lastindex));
                    }
                    else if(videolink.contains("vidkai"))
                    {
                        Element source = doc.select("source").first();
                        downloadLink = source.attr("src");
                        linkList.add(downloadLink);
                        downloadOptions.add(title + " - vidkai" );
                     //   Println(source.attr("src"));
                    }





                }



            return "Success";
//  Println(video_wrap);

            } catch (IOException ex) {

            }
            return "Fail";
        }





        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog

            mProgressDialog.dismiss();


            String[] Names = new String[downloadOptions.size()];
            Names = downloadOptions.toArray(Names);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select links: ");
            builder.setItems(Names, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    downloadLink = linkList.get(item);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
                    context.startActivity(intent);

                }

            });

            builder.setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();

            MainActivity.webView.setVisibility(View.GONE);

            if (result == "Fail")
                downloadLink = "";



        }
    }



}
