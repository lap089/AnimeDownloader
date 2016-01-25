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
import java.util.ArrayList;

/**
 * Created by user on 1/25/2016.
 */
public class AnimeLand_tv {

    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    Context context;
    ProgressDialog mProgressDialog;
    String title;
    public AnimeLand_tv(Context context)
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
            mProgressDialog.setCanceledOnTouchOutside(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {

            try {
                Document doc;
                //        String url = "http://gogoanime.io/sushi-police-episode-3";

                doc = Jsoup.connect(url[0]).get();
                Elements downloads = doc.select("div[style=float:right;]");

                for(Element download : downloads)
                if(download.text().contains("Download Episode"))
                    downloadLink = "http://animeland.tv" + download.getElementsByTag("a").attr("href");


                Log.d("Check animeland:", downloadLink);

                return "Success";
            }catch(IOException e){
                e.printStackTrace();
            }

            return "Fail";
        }


        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog

            mProgressDialog.dismiss();

            if (result == "Fail") {
                downloadLink = "";
                return;
            }
//
//            String[] Names = new String[downloadOptions.size()];
//            Names = downloadOptions.toArray(Names);
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Select links: ");
//            builder.setItems(Names, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int item) {
//
                //    downloadLink = linkList.get(item);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
                    context.startActivity(intent);
//
//                }
//
//            });
//
//            builder.setCancelable(true);
//            AlertDialog alert = builder.create();
//            alert.show();





        }
    }

}
