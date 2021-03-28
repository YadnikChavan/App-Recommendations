package com.example.apprecomendation;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class searchLinks extends AsyncTask<String, Void, List<String>> {

    Context ctx;
    public searchLinks(Context ct){ctx=ct;}
    public List<String> packageLinks=new ArrayList<>();
    public Uri[] links;

    @Override
    protected List<String> doInBackground(String... strings) {
        String packageName = strings[0];
        String page = "https://play.google.com/store/apps/details?id="+packageName;
        try {
            Document doc = (Document) Jsoup.connect(page).get();
            Elements results = doc.getElementsByClass("JC71ub");
            for(Element result:results){
                packageLinks.add(searchResult.insertString(result.attr("href"),"https://play.google.com/",0));
            }

            Log.i("searchLinks","Links="+packageLinks.toString());
            return packageLinks;

        }catch (Exception ex){
            Log.wtf("searchLinks",ex.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        RecommendationActivity.packageLinks = strings;
    }
}
