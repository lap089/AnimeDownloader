package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 1/21/2016.
 */
public class AnimeNova {




    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    android.content.Context context;
    ProgressDialog mProgressDialog;
    String title = "File";
    public AnimeNova(Context context)
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
             //   String url = "http://www.animenova.org/33-eyes-4";
                //   url = "http://www.animenova.org/kuroko-no-basket-saikou-no-present-desu-special";

                doc = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                Elements name = doc.select("h1[class=generic]");
                title = name.first().text();
                Elements vmargins =  doc.select("div[class=vmargin]");
                for(Element vmargin : vmargins)
                {
                    String embedLink = vmargin.children().first().getElementsByAttribute("src").attr("src");


                    doc = Jsoup.connect(embedLink).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                    if(!embedLink.contains("yucache.net")){
                        Elements flow =   doc.select("script");
                        String script =flow.last().toString();
                        int firstindex = script.indexOf("eval");
                        int lastindex = script.indexOf("{}))") + 4 ;
                        //  Println(firstindex + "-" + lastindex);
                        //  Println(script.substring(firstindex,lastindex));


                        org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();

                        // Turn off optimization to make Rhino Android compatible
                        rhino.setOptimizationLevel(-1);
                        Scriptable scope = rhino.initStandardObjects();

                        String codeConverted =    rhino.evaluateString(scope, script.substring(firstindex+4,lastindex), "JavaScript", 1, null).toString();

                   //     ScriptEngineManager factory = new ScriptEngineManager();
                   //     ScriptEngine engine = factory.getEngineByName("JavaScript");

                       // String codeConverted =  engine.eval(script.substring(firstindex+4,lastindex)).toString();
                        //   Println(codeConverted);

                        firstindex = codeConverted.indexOf("addClip({url:\"") + 14;
                        lastindex = firstindex;
                        while(codeConverted.charAt(lastindex)!='\"')
                            ++lastindex;

                        downloadLink = codeConverted.substring(firstindex, lastindex);


                    }
                    else if(embedLink.contains("yucache.net"))
                    {
                        Elements content = doc.select("meta[property=og:video]");
                        downloadLink = content.attr("content").toString();
                        downloadLink = downloadLink.replace("/video.flv", ".flv").replace("cdn", "om102.cdn");

                    }
                    else continue;

                    
                    linkList.add(downloadLink);
                    downloadOptions.add(title);

                }


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
