package com.example.user.animedownloader.Engines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.animedownloader.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2016.
 */
public class AnimeKissanime {


    String url;
    String downloadLink;
    ArrayList<String> downloadOptions;
    ArrayList<String> linkList;
    android.content.Context context;
    ProgressDialog mProgressDialog;
    String title = "AnimeDownloader";
    public AnimeKissanime(Context context)
    {
        this.context = context;
        downloadOptions = new ArrayList<>();
        linkList = new ArrayList<>();
        initialWebView();

    }


    public void initialWebView()
    {
        MainActivity.webView.getSettings().setJavaScriptEnabled(true);
        MainActivity.webView.addJavascriptInterface(new MyJavaScriptInterface(context), "HtmlViewer");
        MainActivity.webView.setVisibility(View.VISIBLE);

        MainActivity.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, final String url) {
                MainActivity.webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                //   Log.d("Check jsoup:", url);
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
        MainActivity.webView.loadUrl(url);

    }



    private class GetDownloadLinkAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog


        }

        @Override
        protected String doInBackground(String... html) {


                Document doc;
            doc = Jsoup.parseBodyFragment(html[0]);

          //  Log.d("Check doinbackground:",doc.toString());
            Elements options =  doc.getElementById("selectQuality").children();
            for(Element option: options)
            {
                downloadOptions.add(title + "-" + option.text());
                String encoded = option.attr("value");
                linkList.add(decodeString(encoded));

            }

                return "Success";

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



    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
       //     new android.support.v7.app.AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
       //             .setPositiveButton(android.R.string.ok, null).setCancelable(true).create().show();

            new GetDownloadLinkAsync().execute(html);

            //   Log.d("Check Html: ", html);
        }

    }


    public static String decodeString(String encoded) {
        byte[] dataDec = Base64.decode(encoded, Base64.DEFAULT);
        String decodedString = "";
        try {

            decodedString = new String(dataDec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {

            return decodedString;
        }
    }
}
