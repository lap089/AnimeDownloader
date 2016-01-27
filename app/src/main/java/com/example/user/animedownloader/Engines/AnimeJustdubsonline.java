package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.animedownloader.MainActivity;
import com.example.user.animedownloader.R;
import com.rey.material.widget.ProgressView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

/**
 * Created by user on 1/26/2016.
 */
public class AnimeJustdubsonline {


    String url;
    String target;
    String id;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    ArrayList<String> videoLink;
    ArrayAdapter<String> adapterDownloadLink;
    android.content.Context context;
    ListView listViewDownload;
    GetDownloadLinkAsync getDownloadLinkAsync;
    AlertDialog dialog;
    Dialog dialogListDownload;
    ProgressDialog mProgressDialog;
    ProgressView loadingProgress;

    int num = 1;
    int videoLinkCount = 0;
    String title = "AnimeDownloader";
    public AnimeJustdubsonline(Context context)
    {
        this.context = context;
        downloadOptions = new ArrayList<>();
        linkList = new ArrayList<>();
        videoLink = new ArrayList<>();
        getDownloadLinkAsync =  new GetDownloadLinkAsync();
        initialWebView();
    }


    public void initialWebView()
    {
        MainActivity.webView.getSettings().setJavaScriptEnabled(true);
        MainActivity.webView.getSettings().setLoadsImagesAutomatically(true);
      //  MainActivity.webView.clearCache(false);

        MainActivity.webView.addJavascriptInterface(new MyJavaScriptInterface(context), "HtmlViewer");
        MainActivity.webView.setVisibility(View.VISIBLE);

        MainActivity.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, final String url) {

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        view.loadUrl("javascript:(function(){document.getElementById('btn_download').click();})()");
                    }
                }.start();

                view.setWebViewClient(new WebViewClient() {
                    public void onPageFinished( WebView view, String url) {
                                view.loadUrl("javascript:window.HtmlViewer.showHTML" +
                                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");


                    }
                });

            }

        });


    }


    public void loadDataAsync(String url) {
        this.url = url;

        target = url.substring(url.indexOf('#'));
        id = target.substring(1,target.indexOf('-'));

        Log.d("Check target: ", target);


        dialogListDownload = new Dialog(context);
        dialogListDownload.setContentView(R.layout.downloaddialog);
        dialogListDownload.setTitle("Select Link");
        dialogListDownload.setCanceledOnTouchOutside(true);

        dialogListDownload.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getDownloadLinkAsync.cancel(true);
                MainActivity.webView.stopLoading();
            //    MainActivity.webView.pauseTimers();
           MainActivity.webView.setWebViewClient(null);
                MainActivity.webView.setVisibility(View.GONE);
            }
        });

      //  dialogListDownload.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        listViewDownload = (ListView) dialogListDownload.findViewById(R.id.downloadlist);
        loadingProgress = (ProgressView) dialogListDownload.findViewById(R.id.loadingprogress);
        loadingProgress.applyStyle(R.style.LightInColorCircularProgressView);
        loadingProgress.start();
        listViewDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                downloadLink = linkList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(downloadLink), "video/mp4");
                context.startActivity(intent);

            }

        });


        dialog = new SpotsDialog(context, R.style.CustomProgressDialog);
        dialog.show();

        // url = "http://www.ryuanime.com/watch/anime/dubbed/seraph-of-the-end-battle-in-nagoya-episode-4";

      //  MainActivity.webView.loadUrl(url);
    //    MainActivity.webView.getSettings().setJavaScriptEnabled(true);
        new GetVideoLinkAsync().execute(url);
    }


    private class GetVideoLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     //       mProgressDialog.setMessage("Get video links");
            // Create a progressdialog

        }

        @Override
        protected String doInBackground(String... url) {


            Document doc;


            try {
                doc = Jsoup.connect(url[0]).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();

                //   Println(doc);
                Elements data_targets = doc.select("li[class=tab");
                String videosrc = "";
                for(Element data_target: data_targets)
                {
                    //Println(data_target.select("a").attr("data-target"));
                    if(data_target.select("a").attr("data-target").contains(target))
                        videosrc=data_target.select("a").attr("href");
                    else if(data_target.select("a").attr("href").contains("title"))
                        title = data_target.select("a").text();
                }
                videosrc = "http://justdubsonline.net" + videosrc;

                doc = Jsoup.connect(videosrc).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();

                Elements videoRefs = doc.select("div[id~="+id+"-+]");
                for(Element videoRef : videoRefs)
                {
                    String videoref = videoRef.select("iframe").attr("src");


                    if(videoref.contains("flashx"))
                    {
                        videoref =  videoref.replace("embed-", "");
                        videoref = videoref.substring(0,videoref.lastIndexOf('-'));
                    }
               //     Println(videoref);
                    videoLink.add(videoref);
                }

                for(String link : videoLink)
                {
                    if(link.contains("mp4upload"))
                    {
                        doc = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36").get();
                        String script =  doc.select("div[id=player_code]").toString();
                        Log.d("Check script",script);
                        int firstindex = script.indexOf("file") + 8;
                        int lastindex = firstindex;
                        while(script.charAt(lastindex)!='\'') ++lastindex;

                        downloadLink = script.substring(firstindex,lastindex).replace("video.mp4",title+" "+id+".mp4");
                        linkList.add(downloadLink);
                        downloadOptions.add(title +" "+ id + "-mp4upload.com");
                        Log.d("Check script", downloadLink);


                    }
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

            if(downloadOptions.size()!=0) {

                dialog.dismiss();

                String[] arrayValue = new String[downloadOptions.size()];
                arrayValue = downloadOptions.toArray(arrayValue);
                adapterDownloadLink = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, arrayValue);

                    dialogListDownload.show();
                listViewDownload.setAdapter(adapterDownloadLink);
               //     adapterDownloadLink.notifyDataSetChanged();
            }


            for(String link : videoLink)
            {
                if(link.contains("flashx"))
                {
                    Log.d("Check link:",link);
                    MainActivity.webView.loadUrl(link);
                    break;
                }
                else if(!link.contains("mp4upload"))            // Link may be died!
                {
                    loadingProgress.stop();
                    MainActivity.webView.setVisibility(View.GONE);
                }
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
            String eval = "empty";
            Elements scripts = doc.select("script[type=text/javascript]");
            Log.d("Check scripts:",scripts.size() + "");
            for(Element script: scripts)
            {
                if(script.toString().contains("eval(function")) {
                    eval = script.toString();
                    Log.d("Check script:",script.toString());
                    break;
                }

            }
            String codeConverted ="empty";

            if(eval.compareTo("empty")!=0) {
                eval = eval.substring(eval.indexOf("eval(") + 4, eval.indexOf("</script>") - 1);
                Log.d("Check evalfunction:", eval);

                org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();

                // Turn off optimization to make Rhino Android compatible
                rhino.setOptimizationLevel(-1);
                Scriptable scope = rhino.initStandardObjects();

               codeConverted  =    rhino.evaluateString(scope,eval,"JavaScript", 1, null).toString();

            }
            else {
                for(Element script: scripts)
                {
                    if(script.toString().contains("sources: [{file")) {
                        codeConverted = script.toString();
                        Log.d("Check sources: [file]:",script.toString());
                        break;
                    }

                }

            }



         int   firstindex = codeConverted.indexOf("file:") + 6;
         int   lastindex = firstindex;
            while(codeConverted.charAt(lastindex)!='\"') ++lastindex;

            downloadLink = codeConverted.substring(firstindex, lastindex);
            downloadLink = downloadLink.replace("mobile.mp4",title+" "+id+".mp4");
            downloadLink = downloadLink.replace("normal.mp4",title+" "+id+".mp4");
            Log.d("Check download_flashx:",downloadLink);
            linkList.add(downloadLink);
            downloadOptions.add(title +" "+id+ "-Flashx.tv");


            return "Success";

        }





        @Override
        protected void onPostExecute(String result) {
            // Set the bitmap into ImageView
            //   imagemanga.setImageBitmap(result);
            // Close progressdialog

            if (result == "Fail")
                downloadLink = "";


                //  mProgressDialog.dismiss();
            if(dialog.isShowing())
                dialog.dismiss();

            String[] arrayValue = new String[downloadOptions.size()];
            arrayValue = downloadOptions.toArray(arrayValue);
            adapterDownloadLink = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, android.R.id.text1, arrayValue);
            listViewDownload.setAdapter(adapterDownloadLink);
            if(!dialogListDownload.isShowing())
                dialogListDownload.show();
            else
                adapterDownloadLink.notifyDataSetChanged();


            loadingProgress.stop();

                MainActivity.webView.setVisibility(View.GONE);
            }






    }





    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {

            if(!html.contains("btn_download")) {
            //      new android.support.v7.app.AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
            //              .setPositiveButton(android.R.string.ok, null).setCancelable(true).create().show();


                getDownloadLinkAsync.execute(html);

            }




            //   Log.d("Check Html: ", html);
        }

    }


}
