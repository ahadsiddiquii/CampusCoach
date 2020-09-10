package com.user.studentapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderAnnounce extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderAnnounce(View itemView){
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
    public void setDetails(Context ctx,String Ano, String Announce,String date,String time,String extend) {
        TextView AnnouncementNumber=mView.findViewById(R.id.rAno);
        AnnouncementNumber.setText(Ano);
        TextView Announcement=mView.findViewById(R.id.rAnnounce);
        Announcement.setText(Announce);
        TextView DateOfAnnouncement=mView.findViewById(R.id.rdate);
        DateOfAnnouncement.setText(date);
        TextView TimeOfAnnouncement=mView.findViewById(R.id.rtime);
        TimeOfAnnouncement.setText(time);
        TextView Extend=mView.findViewById(R.id.rDescript);
        Extend.setText(extend);
    }

    private ViewHolderAnnounce.ClickListener mClickListener;


    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderAnnounce.ClickListener clickListener){
        mClickListener = clickListener;
    }




}
