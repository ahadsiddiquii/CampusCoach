package com.example.adminapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderStudents  extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolderStudents(View itemView){
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
    public void setDetails(Context ctx,String StudentName,String StudentID,String GuardianCNIC,String Email,String PointNumber,String Contact,String Joindate,String Account) {

        TextView studentName=mView.findViewById(R.id.rStudentName);
        studentName.setText(StudentName);
        TextView studentID=mView.findViewById(R.id.rStudentID);
        studentID.setText(StudentID);
        TextView email=mView.findViewById(R.id.rEmail);
        email.setText(Email);
        TextView contact=mView.findViewById(R.id.rContact);
        contact.setText(Contact);
        TextView PointNo=mView.findViewById(R.id.rPointNumber);
        PointNo.setText(PointNumber);
        TextView cnic=mView.findViewById(R.id.rCNIC);
        cnic.setText(GuardianCNIC);
        TextView joindate=mView.findViewById(R.id.rJoinDate);
        joindate.setText(Joindate);
        TextView account=mView.findViewById(R.id.rAcc);
        account.setText(Account);

    }

    private com.example.adminapi.ViewHolderStudents.ClickListener mClickListener;


    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(com.example.adminapi.ViewHolderStudents.ClickListener clickListener){
        mClickListener = clickListener;
    }



}