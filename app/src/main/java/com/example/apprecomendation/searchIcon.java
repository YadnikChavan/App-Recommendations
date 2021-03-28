package com.example.apprecomendation;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class searchIcon extends AsyncTask<String, Void, List<String>> {

    Context ctx;
    Uri[] links;

    public searchIcon(Context ct) {
        ctx = ct;

    }

    @Override
    protected List<String> doInBackground(String... strings) {
        String packageName = strings[0];
        String page = "https://play.google.com/store/apps/details?id=" + packageName;
        List<String> iconsLinks = new ArrayList<>();
        List<Bitmap> icons = new ArrayList<Bitmap>();
        List<String> filterIconsLinks = new ArrayList<>();

        try {
            Document doc = (Document) Jsoup.connect(page).get();
            Elements results = doc.getElementsByClass("T75of QNCnCf");
            for (Element result : results) {
                iconsLinks.add(result.attr("data-src"));
            }
            filterIconsLinks = removeDuplicates(iconsLinks);
            Log.i("searchIcon", "iconLinks" + filterIconsLinks.toString());
            try {

                /*for(Object strurl: filterIconsLinks){
                    URL url = new URL(strurl.toString());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    icons.add(bmp);*/


                for (int i = 0; i < filterIconsLinks.size(); i++) {
                    links[i] = Uri.parse(filterIconsLinks.get(i));
                }
            } catch (Exception e) {
                Log.wtf("searchIconForLoop", e.toString());
            }
            Log.i("searchIcon", "BitImg Icons" + filterIconsLinks.toString());
            return filterIconsLinks;

        } catch (Exception e) {
            Log.wtf("searchIcon", "" + e.toString());
        }
        return filterIconsLinks;
    }





    @Override
    protected void onPostExecute(List<String> links) {
        super.onPostExecute(links);
        RecommendationActivity.links = links;
    }

    public static <T> ArrayList<T> removeDuplicates(List<String> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (String element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add((T) element);
            }
        }

        // return the new list
        return newList;
    }
}
