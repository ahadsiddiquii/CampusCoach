package com.example.adminapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderPointNumber  extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderPointNumber(View itemView){
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
    public void setDetails(Context ctx,String p, String PointNumber) {
        TextView Point=mView.findViewById(R.id.rPoint);
        Point.setText(p);
        TextView PointNo=mView.findViewById(R.id.rPointNo);
        PointNo.setText(PointNumber);

    }

    private com.example.adminapi.ViewHolderPointNumber.ClickListener mClickListener;


    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(com.example.adminapi.ViewHolderPointNumber.ClickListener clickListener){
        mClickListener = clickListener;
    }



}
