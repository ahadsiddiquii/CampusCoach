package com.example.adminapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewHolder1 extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder1(View itemView){
        super(itemView);
        mView=itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
                return false;
            }
        });



    }
    public void setDetails(Context ctx, String title, String location1, String location2, String location3, String location4, String location5, String location6, String location7, String location8, String location9, String location10, String location11, String location12,String location13,String location14,String location15,String location16,String location17) {
        TextView rTitleTv=mView.findViewById(R.id.rTitleTv);
        rTitleTv.setText(title);
        TextView Location1=mView.findViewById(R.id.Location1);
        Location1.setText(location1);
        TextView Location2=mView.findViewById(R.id.Location2);
        Location2.setText(location2);
        TextView Location3=mView.findViewById(R.id.Location3);
        Location3.setText(location3);
        TextView Location4=mView.findViewById(R.id.Location4);
        Location4.setText(location4);
        TextView Location5=mView.findViewById(R.id.Location5);
        Location5.setText(location5);
        TextView Location6=mView.findViewById(R.id.Location6);
        Location6.setText(location6);
        TextView Location7=mView.findViewById(R.id.Location7);
        Location7.setText(location7);
        TextView Location8=mView.findViewById(R.id.Location8);
        Location8.setText(location8);
        TextView Location9=mView.findViewById(R.id.Location9);
        Location9.setText(location9);
        TextView Location10=mView.findViewById(R.id.Location10);
        Location10.setText(location10);
        TextView Location11=mView.findViewById(R.id.Location11);
        Location11.setText(location11);
        TextView Location12=mView.findViewById(R.id.Location12);
        Location12.setText(location12);
        TextView Location13=mView.findViewById(R.id.Location13);
        Location13.setText(location13);
        TextView Location14=mView.findViewById(R.id.Location14);
        Location14.setText(location14);
        TextView Location15=mView.findViewById(R.id.Location15);
        Location15.setText(location15);
        TextView Location16=mView.findViewById(R.id.Location16);
        Location16.setText(location16);
        TextView Location17=mView.findViewById(R.id.Location17);
        Location17.setText(location17);
    }

    private ViewHolder1.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolder1.ClickListener clickListener){
        mClickListener = clickListener;
    }



}
