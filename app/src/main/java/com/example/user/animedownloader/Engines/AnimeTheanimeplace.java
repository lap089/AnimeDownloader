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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 1/28/2016.
 */
public class AnimeTheanimeplace {



    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    Context context;
    ProgressDialog mProgressDialog;
    String title;
    public AnimeTheanimeplace(Context context)
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

                int firstindex, lastindex;
                Document doc;
            //    String url = "http://theanimeplace.co/dokkoida-episode-8-english-dubbed/";
                doc = Jsoup.connect(url[0]).get();
                title = doc.select("title").text();
                Elements server = doc.select("section[id=ccr-left-section]");
                Elements scripts = server.select("script[type=text/javascript]");

                String serverurl = "empty";
                for(Element script: scripts)
                {
                    serverurl = script.toString();
                    if(script.toString().contains("server_url")){
                        firstindex = serverurl.indexOf("http://") + 7;
                        lastindex = firstindex;
                        while(serverurl.charAt(lastindex)!='.') ++lastindex;
                        serverurl = serverurl.substring(firstindex,lastindex);
                      //  Println(serverurl);
                        break;
                    }
                }


                String blast = doc.select("iframe").attr("src");

                blast = blast.replace("www.",serverurl+ ".") + "&submit=Click+here+to+Watch%21";
                blast = blast.replace("ubox.php","ubox_frame.php");
                blast = blast.replace("play.php", "play_frame.php");
                blast = blast.replace("u=", "s_id=").replace("f=", "v=");
                blast = blast.replace("width=","w=").replace("height=","h=");
          //      Println(blast);
                Log.d("Check blast",blast);
                doc = Jsoup.connect(blast).get();
                Element video_frame = doc.select("div[id=video_frame]").first();
                String script = video_frame.toString();
                firstindex = script.indexOf("encodeURI") + 11;
                lastindex = firstindex;
                while(script.charAt(lastindex)!='\'') ++lastindex;
                String videoLinkLast = script.substring(firstindex,lastindex);

                downloadLink = "http://"+serverurl+".blastvideo.ws" + videoLinkLast;
                linkList.add(downloadLink);
                downloadOptions.add(title);
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
