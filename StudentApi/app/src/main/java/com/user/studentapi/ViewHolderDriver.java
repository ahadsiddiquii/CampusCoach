package com.user.studentapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderDriver  extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderDriver(View itemView){
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
    public void setDetails(Context ctx, String DriverName, String DriverContact,String DriverPointNumber, String DriverCNIC,String DriverJoinedDate, String DriverEmail  ) {

        TextView driverName=mView.findViewById(R.id.rDriverName);
        driverName.setText(DriverName);
        TextView driverContact=mView.findViewById(R.id.rDriverContact);
        driverContact.setText(DriverContact);
        TextView driverPointNumber=mView.findViewById(R.id.rDriverPointno);
        driverPointNumber.setText(DriverPointNumber);
        TextView driverCNIC=mView.findViewById(R.id.rDriverCnic);
        driverCNIC.setText(DriverCNIC);
        TextView driverJoinedDate=mView.findViewById(R.id.rDriverjoineddate);
        driverJoinedDate.setText(DriverJoinedDate);
        TextView driverEmail=mView.findViewById(R.id.rDriverEmail);
        driverEmail.setText(DriverEmail);

    }

    private ViewHolderDriver.ClickListener mClickListener;


    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderDriver.ClickListener clickListener){
        mClickListener = clickListener;
    }



}