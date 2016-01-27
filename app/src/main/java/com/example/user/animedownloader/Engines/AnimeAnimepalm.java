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
 * Created by user on 1/27/2016.
 */
public class AnimeAnimepalm {

    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    Context context;
    ProgressDialog mProgressDialog;
    String title;
    public AnimeAnimepalm(Context context)
    {
        this.context = context;
        downloadOptions = new ArrayList<>();
        linkList = new ArrayList<>();
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

                doc = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();

                Elements iframes = doc.select("iframe");
                title = doc.select("title").text();
                Log.d("Check name:" , title);
                String linkdown = "";
                for(Element iframe : iframes)
                {
                    linkdown  = iframe.attr("src");
                    if(linkdown.contains("myvidstream") || linkdown.contains("mp4engine") || linkdown.contains("vidup")){
                    //    Println(linkdown);
                        break;
                    }
                }

                doc = Jsoup.connect(linkdown).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                //    Println(doc);
                String script = "empty";
                if(!linkdown.contains("vidup"))
                {
                    Elements scripts = doc.select("div[id=player_code]");
                    // Println(scripts.select("script").last());
                    script = scripts.select("script").last().toString();
                    script = script.substring(script.indexOf("eval(") + 4, script.indexOf("</script>") - 1);

                    org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();

                    // Turn off optimization to make Rhino Android compatible
                    rhino.setOptimizationLevel(-1);
                    Scriptable scope = rhino.initStandardObjects();

                  String  codeConverted  =    rhino.evaluateString(scope,script,"JavaScript", 1, null).toString();
                    Log.d("Check codeConverted",codeConverted);

                    if(linkdown.contains("myvidstream")) {
                        int firstindex = codeConverted.indexOf("'file'") + 8;
                        int lastindex = firstindex;
                        while (codeConverted.charAt(lastindex) != '\'') ++lastindex;
                        downloadLink = codeConverted.substring(firstindex, lastindex);
                        downloadLink = downloadLink.replace("video",title);
                    }
                    else if(linkdown.contains("mp4engine"))
                    {
                        int   firstindex = codeConverted.indexOf("file:") + 6;
                        int   lastindex = firstindex;
                        while(codeConverted.charAt(lastindex)!='\"') ++lastindex;
                        downloadLink = codeConverted.substring(firstindex, lastindex);
                    }

                    Log.d("Check downloadlink",downloadLink);
                }
                else
                {
                    String temp = doc.toString();
                    temp = temp.substring(temp.indexOf("clip:"));
                    int firstindex = temp.indexOf("http://");
                    int lastindex = firstindex;
                    while(temp.charAt(lastindex)!='\"') ++lastindex;
                    downloadLink = temp.substring(firstindex,lastindex);

                }

                downloadOptions.add(title);
                linkList.add(downloadLink);



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
