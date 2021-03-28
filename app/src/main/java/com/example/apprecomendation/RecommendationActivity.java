package com.example.apprecomendation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecomendation.adapter.RecommendAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends Activity {

    RecyclerView recyclerView;
    searchResult searchResult = new searchResult(RecommendationActivity.this);
    public static List<String> packageLinks = new ArrayList<>();
    public static List<String> appNames = new ArrayList<>();
    public static List<Bitmap> bitmapsIcons = new ArrayList<>();
    public static List<String> links;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_list);
        recyclerView = findViewById(R.id.recommendRecycler);
        RecommendAdapter recommendAdapter= new RecommendAdapter(RecommendationActivity.this,appNames,packageLinks,links);
        recyclerView.setAdapter(recommendAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
