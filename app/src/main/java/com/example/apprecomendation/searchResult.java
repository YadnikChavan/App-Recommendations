package com.example.apprecomendation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class searchResult extends AsyncTask<String, Void, List<String>> {

    Context ctx;
    public List<String> appNames=new ArrayList<>();
    public List<String> packageLinks=new ArrayList<>();
    public String packageName;
    public searchResult (Context ct){
        ctx=ct;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        packageName = strings[0];
        String page = "https://play.google.com/store/apps/details?id="+packageName;
        Log.i("searchResult",page);
        try {
            Document doc = Jsoup.connect(page).get();
            Elements results = doc.getElementsByClass("WsMG1c nnK0zc");
            for(Element result:results){
                appNames.add(result.text());
            }
            setAppNames(appNames);
            Log.i("searchResults", "Apps:"+getAppNames().toString());
            return appNames;
        }
        catch (Exception  ex){
            Log.wtf("searchResults",ex.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        for(String s:strings){
            RecommendationActivity.appNames.add(s);
        }
    }

    public List<String> getAppNames() {
        return appNames;
    }

    public void setAppNames(List<String> appNames) {
        this.appNames = appNames;
    }



    public static String insertString(String originalString, String stringToBeInserted, int index) {

        // Create a new string
        String newString = new String();

        for (int i = 0; i < originalString.length(); i++) {

            // Insert the original string character
            // into the new string
            newString += originalString.charAt(i);

            if (i == index) {

                // Insert the string to be inserted
                // into the new string
                newString += stringToBeInserted;
            }
        }

        // return the modified String
        return newString;
    }

}


