package com.example.adminapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    //Arrays
    public int[] slide_images={
            R.drawable.basit,
            R.drawable.nadeem,
            R.drawable.zain,
            R.drawable.maria,
            R.drawable.izaan1,
            R.drawable.ahad,
            R.drawable.mariam
    };
    public  String[] names={
            "Basit Jasani",
            "Muhammad Nadeem",
            "Zain-ul-Hassan",
            "Maria AliAsghar",
            "Izaan Sohail",
            "Ahad-ur-Rehman Siddiqui",
            "Mariam Khatri"
    };
    public String [] ids={
            "\"Supervisor\"","\"Co-Supervisor\"","\"Advisor\"","K180161@nu.edu.pk","K180162@nu.edu.pk","K181138@nu.edu.pk","K181123@nu.edu.pk"
    };
    public String[] quote={
            "\"Teaching is a noble profession that shapes character, caliber, and future of an individual. If people remember me as a good teacher, that will be a big honour for me.\n\"",
            "\" If you want to teach people a new way of thinking, don't bother trying to teach them. Instead, give them a tool, the use of which will lead to new ways of thinking.\"",
            "\" Teaching is only demonstrating that it is possible. Learning is making it possible for yourself.\"",
            "\"The path from dreams to success does exist. May you have the vision to find it, the courage to get on to it, and the perseverance to follow it.\"" ,
            "\"Success isn't just about what you accomplish in your life; it's about what you inspire others to do.\"",
            "\"In order to succeed, your desire for success should be greater than your fear of failure.\"",
            "\"The secret to success is to know something nobody else knows.\""

    };
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==(LinearLayout) o;
    }
    @Override
    public Object instantiateItem(ViewGroup container,int position)
    {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.slider_layout,container,false);
        ImageView sliderImageView=(ImageView)view.findViewById(R.id.slide_images);
        TextView sname=(TextView)view.findViewById(R.id.names);
        TextView sid=(TextView)view.findViewById(R.id.ids);
        TextView squote=(TextView)view.findViewById(R.id.quote);
        sliderImageView.setImageResource(slide_images [position]);
        sname.setText(names[position]);
        sid.setText(ids[position]);
        squote.setText(quote[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((LinearLayout)object);
    }
}