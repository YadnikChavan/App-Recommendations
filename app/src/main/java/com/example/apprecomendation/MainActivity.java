package com.example.apprecomendation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecomendation.adapter.AppAdapter;

import java.util.List;

import bot.box.appusage.contract.UsageContracts;
import bot.box.appusage.handler.Monitor;
import bot.box.appusage.model.AppData;
import bot.box.appusage.utils.Duration;

public class MainActivity extends AppCompatActivity implements UsageContracts.View
        , AdapterView.OnItemSelectedListener  {

    private AppAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},100);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Monitor.hasUsagePermission()){
            Monitor.scan().getAppLists(this).fetchFor(Duration.TODAY);
            init();

        }else {
            Monitor.requestUsagePermission();
        }
    }

    private void init() {
        RecyclerView mRecyler = findViewById(R.id.recycler);
        mAdapter = new AppAdapter(this);

        Spinner spinner=findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.duration));
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        mRecyler.setLayoutManager(mLayoutManager);
        mRecyler.setAdapter(mAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        Monitor.scan().getAppLists(this).fetchFor(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void getUsageData(List<AppData> usageData, long mTotalUsage, int duration) {
        mAdapter.updateData(usageData);
    }
}