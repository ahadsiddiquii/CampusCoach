package com.example.adminapi;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    Activity activity;
    int layout;
    ArrayList<extendroutes> arrExtendRoutes;

    public CustomAdapter(@NonNull Activity activity, @LayoutRes int layout,@NonNull ArrayList<extendroutes> arrExtendRoutes) {
        super(activity, layout,arrExtendRoutes);
        this.activity = activity;
        this.layout= layout;
        this.arrExtendRoutes= arrExtendRoutes;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parennt){
        LayoutInflater layoutInflater=activity.getLayoutInflater();
        convertView= layoutInflater.inflate(layout,null);
        TextView ten=(TextView) convertView.findViewById(R.id.text_ten);
        ten.setText(arrExtendRoutes.get(position).getLocation());

        return convertView;
    }
}
