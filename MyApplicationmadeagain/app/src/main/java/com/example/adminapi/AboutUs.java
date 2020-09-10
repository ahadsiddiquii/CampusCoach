package com.example.adminapi;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button mbackbutton,mnextbutton;
    private  int mcurrentpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();
        mSlideViewPager=(ViewPager)findViewById(R.id.SlideViewPager);
        mDotLayout=(LinearLayout)findViewById(R.id.dots);
        sliderAdapter=new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewlistener);
        mbackbutton=(Button)findViewById(R.id.previous);
        mnextbutton=(Button)findViewById(R.id.next);
        mnextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mcurrentpage==6)
                {
                    Intent intent=new Intent(AboutUs.this,AdminLog.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mSlideViewPager.setCurrentItem(mcurrentpage+1);
                }
            }
        });
        mbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcurrentpage==0)
                {
                    Intent intent=new Intent(AboutUs.this,AdminLog.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mSlideViewPager.setCurrentItem(mcurrentpage-1);
                }

            }
        });
    }
    public void addDotsIndicator(int position)
    {
        mDots=new TextView[7];
        mDotLayout.removeAllViews();
        for (int i=0;i<mDots.length;i++)
        {
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length>0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.gray));
        }

    }
    ViewPager.OnPageChangeListener viewlistener =new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mcurrentpage=i;
            if (i==0)
            {
                mnextbutton.setEnabled(true);
                mbackbutton.setEnabled(true);
                mbackbutton.setVisibility(View.VISIBLE);
                mnextbutton.setText("NEXT");
                mbackbutton.setText("BACK");
            }else if(i==mDots.length-1)
            {
                mnextbutton.setEnabled(true);
                mbackbutton.setEnabled(true);
                mbackbutton.setVisibility(View.VISIBLE);
                mnextbutton.setText("FINISH");
                mbackbutton.setText("BACK");
            }
            else
            {
                mnextbutton.setEnabled(true);
                mbackbutton.setEnabled(true);
                mbackbutton.setVisibility(View.VISIBLE);
                mnextbutton.setText("NEXT");
                mbackbutton.setText("BACK");
            }
        }


        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AboutUs.this, AdminLog.class);
        startActivity(intent);
        finish();
    }
}
