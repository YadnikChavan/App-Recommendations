package com.example.apprecomendation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.apprecomendation.adapter.AppTimeLineAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bot.box.appusage.contract.PackageContracts;
import bot.box.appusage.contract.TimelineContracts;
import bot.box.appusage.handler.Monitor;
import bot.box.appusage.model.AppData;
import bot.box.appusage.model.TimeLine;
import bot.box.appusage.utils.Duration;
import bot.box.appusage.utils.UsageUtils;


public class DetailActivity extends AppCompatActivity {
    public static final String PACKAGE_NAME = "_packageName";

    public static void start(Activity activity,String packageName){
        Intent intent=new Intent(activity,DetailActivity.class);
        intent.putExtra(PACKAGE_NAME,packageName);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String packageName = getIntent().getStringExtra(PACKAGE_NAME);

        readingFile();

        Monitor.scan().queryFor(new PackageContracts.View() {
            @Override
            public void getUsageForPackage(AppData appData, int duration) {
                ((TextView) findViewById(R.id.name)).setText(appData.mName);
                ((TextView) findViewById(R.id.total_times_launched)).setText(appData.mCount+""+getResources().getQuantityString(R.plurals.times_launched, appData.mCount));
                ((TextView) findViewById(R.id.data_used)).setText(UsageUtils.humanReadableByteCount(appData.mWifi+appData.mMobile)+"Consumed");
                ((TextView) findViewById(R.id.last_launched)).setText(String.format(Locale.getDefault(),"%s","Last launched " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.getDefault()).format(new Date(appData.mEventTime))));
                ((TextView) findViewById(R.id.total_usage_time)).setText(UsageUtils.humanReadableMillis(appData.mUsageTime));
                Glide.with(DetailActivity.this).load(UsageUtils.parsePackageIcon(appData.mPackageName, R.mipmap.ic_launcher)).transition(new DrawableTransitionOptions().crossFade()).into((ImageView) findViewById(R.id.icon));
            }



            @Override
            public void showProgress() {

            }

            @Override
            public void hideProgress() {

            }
        }).whichPackage(packageName).fetchFor(Duration.TODAY);

        AppTimeLineAdapter timeLineAdapter = new AppTimeLineAdapter(getApplicationContext());
        RecyclerView rv = findViewById(R.id.timelineRecyclerView);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(timeLineAdapter);
        Monitor.scan().generateTimeline(new TimelineContracts.View(){
            @Override
            public void onTimelineGenerated(List<List<TimeLine>> timeline) {
                timeLineAdapter.updateData(timeline);
            }
        }).whichPackage(packageName).fetchForToday();
    }

    public void readingFile(){
        try {
            String packageName = getIntent().getStringExtra(PACKAGE_NAME);
            InputStream inputStream = getResources().openRawResource(R.raw.playstore);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            boolean found=false;
            String line="";String AppId="";String AppName ="";String AppCategory ="";String AppRating ="";String AppRatingCount ="";

            Log.i("DetailActivity","packageName :"+packageName);

            while ((line=bufferedReader.readLine()) != null && !found){

                String App[]=line.split(",");
                //Log.i("DetailActivity", "line: "+App[0]);
                AppId = App[0];
                if(AppId.equals(packageName)){
                    found=true;
                    AppName = App[1];AppCategory=App[2];AppRating=App[3];AppRatingCount=App[4];
                }


            }

            if(found){
                Log.i("DetailActivity","AppId:"+AppId+" AppName:"+AppName+" AppCategory:"+AppCategory+" AppRating:"+AppRating+" AppRatingCount:"+AppRatingCount);
            }
            else {
                Log.i("DetailActivity","Cannot Found the App");
            }
        }catch (Exception ex){
            Log.wtf("DetailActivity","Error"+ex);
        }
    }

    public void searchSimilar(){

    }

}
