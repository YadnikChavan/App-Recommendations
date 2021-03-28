package com.example.apprecomendation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecomendation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.MyViewHolder> {

    List<String> AppName = new ArrayList<>();
    List<String> PackageLinks=new ArrayList<>();
    List<Bitmap> Icons = new ArrayList<>();
    Context context;
    String[] appNames,appLinks;
    List<String> links;

    public RecommendAdapter(Context ct, List<String> appName, List<String> packageLinks, List<String> iconsLinks){
        context=ct;
        links=iconsLinks;
        AppName=appName;
        PackageLinks=packageLinks;
        appNames=GetStringArray((ArrayList<String>) AppName);
        appLinks=GetStringArray((ArrayList<String>) PackageLinks);
    }



    public static Bitmap[] getBitmapArray(List<Bitmap> bitmap){
        Bitmap[] icons = new Bitmap[bitmap.size()];
        for (int i =0; i<bitmap.size();i++){
            icons[i]=bitmap.get(i);
            Log.i("AdapterBitmapp","I-"+i);
            Log.i("AdapterBitmapp",icons[i].toString());
        }
        return icons;
    }

    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.recommadation_row,parent,false);
        Log.i("Adapter","Size:"+AppName.size());

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("Adapter","Position"+position);
        holder.AppNames.setText(appNames[position]);
        //holder.packageLinks.setText(appLinks[position]);
//        Log.i("Adapter","BitIcon:"+Icons.get(position+1));
        //holder.imageView.setImageResource(R.drawable.ic_launcher_background);

        Picasso.with(context)
                .load(links.get(position))
                .into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(appLinks[position].substring(1));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return AppName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView AppNames;
        ConstraintLayout layout;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AppNames=itemView.findViewById(R.id.txtAppTitle);
            imageView=(ImageView) itemView.findViewById(R.id.imageButton);
            layout = itemView.findViewById(R.id.row_layout);
        }
    }
}
