package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 2/12/2016.
 */
public class AnimeGogo_com {


    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    Context context;
    ProgressDialog mProgressDialog;
    String title;
    public AnimeGogo_com(Context context)
    {
        this.context = context;
        linkList = new ArrayList<>();
        downloadOptions = new ArrayList<>();
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
            mProgressDialog.setCanceledOnTouchOutside(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {

            try {
                int firstindex = 0,lastindex = 0;

                Document doc;
               // String url = "http://www.gogoanime.com/active-raid-kidou-kyoushuushitsu-dai-hakkei-episode-6";

                doc = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                title = doc.select("title").text();
                Elements iframes = doc.select("div[class=postcontent]").select("iframe");
                for(Element iframe : iframes)
                {
                    String href = iframe.attr("src");
                 //   Println(href);
                    doc = Jsoup.connect(href).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                    String script = doc.toString();
                    firstindex = script.lastIndexOf("eval(function") + 4;
                    lastindex = script.indexOf("{}))",firstindex) + 4;
                    String eval = script.substring(firstindex,lastindex);
                    org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();

                    // Turn off optimization to make Rhino Android compatible
                    rhino.setOptimizationLevel(-1);
                    Scriptable scope = rhino.initStandardObjects();

                    eval  =    rhino.evaluateString(scope,eval,"JavaScript", 1, null).toString();
                    firstindex = eval.indexOf("addClip") + 14;
                    lastindex = firstindex;
                    while (eval.charAt(lastindex) != '\"') ++lastindex;
                    eval = eval.substring(firstindex,lastindex);
                    Log.d("Check link: ",eval);
                    linkList.add(eval);
                    downloadOptions.add(title);
              //      Println(script.substring(firstindex,lastindex));
                }
                //   Println(iframes.size());

            } catch (IOException ex) {
              ex.printStackTrace();
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

            if (result == "Fail")
                downloadLink = "";



        }
    }


}
