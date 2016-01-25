package com.example.user.animedownloader;

import android.content.Context;
import android.view.View;

import com.example.user.animedownloader.Engines.AnimeGogo_tv;
import com.example.user.animedownloader.Engines.AnimeKissanime;
import com.example.user.animedownloader.Engines.AnimeNova;
import com.example.user.animedownloader.Engines.AnimeRyuanime;
import com.example.user.animedownloader.Engines.AnimeWatchcartoononline;

import java.util.ArrayList;

/**
 * Created by user on 1/22/2016.
 */
public class EngineManager {

    Context context;
    String url;
    ArrayList<String> sites;
   public static ArrayList<String> currentSupportSites = new ArrayList<String>(){
       {
           add("http://www.animenova.org");
           add("http://www.ryuanime.com");
           add("http://www.gogoanime.io");
           add("http://www.kissanime.to");
           add("http://www.watchcartoononline.com");
       }
   };

    public EngineManager(Context context,String url)
    {
        this.context = context;
        this.url = url;
        sites = new ArrayList<String>(){
            {
                add("animenova");
                add("ryuanime");
                add("gogoanime");
                add("kissanime");
                add("watchcartoononline");
            }
        };

    }

    public static String getSupportedSites()
    {
        String list = "Supported sites: \n";
        for(String site : currentSupportSites)
        {
            list = list + site + "\n";
        }
        return list;
    }

    public void execute()
    {
        int index = 0;
        MainActivity.webView.setVisibility(View.GONE);
        for(String site : sites)
        {
            if(url.contains(site))
                break;
            ++index;
        }

        if(index == 0) {
            new AnimeNova(context).loadDataAsync(url);
        }
        else if(index == 1)
        {
            new AnimeRyuanime(context).loadDataAsync(url);
        }
        else if(index == 2)
        {
            new AnimeGogo_tv(context).loadDataAsync(url);
        }
        else if(index == 3)
        {
            new AnimeKissanime(context).loadDataAsync(url);
        }
        else if(index == 4)
        {
            new AnimeWatchcartoononline(context).loadDataAsync(url);
        }

    }



}
