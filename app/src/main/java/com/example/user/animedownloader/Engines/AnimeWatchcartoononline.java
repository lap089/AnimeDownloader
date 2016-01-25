package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.animedownloader.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2016.
 */
public class AnimeWatchcartoononline {


    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    ArrayList<String> videoLink;
    android.content.Context context;
    ArrayList<GetDownloadLinkAsync> getDownloadLinkAsyncsList;

    ProgressDialog mProgressDialog;

    int num = 1;
    int videoLinkCount = 0;
    String title = "AnimeDownloader";
    public AnimeWatchcartoononline(Context context)
    {
        this.context = context;
        downloadOptions = new ArrayList<>();
        linkList = new ArrayList<>();
        videoLink = new ArrayList<>();
        getDownloadLinkAsyncsList = new ArrayList<>();
        initialWebView();
    }


    public void initialWebView()
    {
        MainActivity.webView.getSettings().setJavaScriptEnabled(true);
        MainActivity.webView.getSettings().setLoadsImagesAutomatically(true);
        MainActivity.webView.addJavascriptInterface(new MyJavaScriptInterface(context), "HtmlViewer");
        MainActivity.webView.setVisibility(View.VISIBLE);

        MainActivity.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, final String url) {
                view.loadUrl("javascript:(function(){document.getElementById('yuklen').click();})()");

                view.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {

                        view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    }
                });

            }

        });


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
        new GetVideoLinkAsync().execute(url);
    }


    private class GetVideoLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           mProgressDialog.setMessage("Get video links");
            // Create a progressdialog

        }

        @Override
        protected String doInBackground(String... url) {


            Document doc;
            try {
                doc = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();

            Elements tabs = doc.select("div[class=postTabs_divs postTabs_curr_div]");
            Element name = doc.select("title").first();
             title = name.text();

            Element tab =  tabs.first();
            Elements links = tab.select("iframe");

                for( Element link : links)
                {
                   videoLink.add(link.attr("src"));
                    Log.d("Check videolink:", link.attr("src"));
                }

                return "Success";
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Fail";

        }





        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog


            if (result == "Fail") {
                downloadLink = "";
                return;
            }

            if(videoLinkCount < videoLink.size()) {
               MainActivity.webView.loadUrl(videoLink.get(videoLinkCount));
                ++videoLinkCount;
            }


        }
    }




    private class GetDownloadLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
         //   mProgressDialog.setMessage("Get download link ");

        }

        @Override
        protected String doInBackground(String... html) {

            Log.d("Check doinbackground", "run");
            Document doc;
            doc = Jsoup.parseBodyFragment(html[0]);

            Elements embeds = doc.select("embed");
            if(embeds.size() == 0){
                Elements scripts =  doc.select("script");
                String scriptString = "";

                for(Element script : scripts)
                {
                    if(script.toString().contains("file"))
                    {
                        scriptString = script.toString();
                     break;
                    }
                }

                int firstindex = scriptString.indexOf("file") + 7;
                int lastindex = firstindex;
                while(scriptString.charAt(lastindex) != '\"')
                    ++lastindex;
                String stream = scriptString.substring(firstindex,lastindex);
             //   stream = stream.replaceFirst("http://l","http://media");
                linkList.add(stream);
                Log.d("Check downloadlink1",stream);
                downloadOptions.add(title + "-" + num);
            }
            else{
                Element embed = embeds.first();
                String flashvar =  embed.attr("flashvars");
                String stream = "";
                boolean full = false;
                for(String s : flashvar.split("&"))
                    if(s.contains("flv") || s.contains("mp4") || s.startsWith("e="))
                    {
                        if(s.startsWith("e=")){
                        stream = stream + "&" + s;
                        break;
                         }
                        else stream = s;

                    }
                stream = stream.substring(5);
                linkList.add(stream);
                Log.d("Check downloadlink2", stream);
                downloadOptions.add(title + "-" + num);
            }

            ++num;

            Log.d("Check num:",num + "");

            return "Success";

        }





        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog

            if (result == "Fail")
                downloadLink = "";

            if(videoLinkCount < videoLink.size())
            {
                Log.d("Check showHTML:", "yes" + "");
                MainActivity.webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        view.loadUrl("javascript:(function(){document.getElementById('yuklen').click();})()");

                        view.setWebViewClient(new WebViewClient() {
                            public void onPageFinished(WebView view, String url) {

                                view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                            }
                        });

                    }

                });
                MainActivity.webView.loadUrl(videoLink.get(videoLinkCount));
                ++videoLinkCount;
            //    getDownloadLinkAsyncsList.add(getDownloadLinkAsync);
            }
            else {
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
            }





        }
    }



    public boolean isFinished()
    {
        for (GetDownloadLinkAsync getDownloadLinkAsync : getDownloadLinkAsyncsList)
            if(getDownloadLinkAsync.getStatus() == AsyncTask.Status.RUNNING)
                return false;
        return true;
    }



    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
       //          new android.support.v7.app.AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
       //                  .setPositiveButton(android.R.string.ok, null).setCancelable(true).create().show();


            new GetDownloadLinkAsync().execute(html);




            //   Log.d("Check Html: ", html);
        }

    }


}
