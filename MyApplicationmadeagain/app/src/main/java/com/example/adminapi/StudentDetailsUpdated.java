package com.example.adminapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentDetailsUpdated extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef,ref,refer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    FirebaseRecyclerAdapter<DriverInfo,ViewHolderPointNumber> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<DriverInfo> options;
    public ImageView mDelete;
    public ImageView mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details_updated);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Select Point");

        final SwipeRefreshLayout pullToRefreshStudents = findViewById(R.id.pullToRefreshStudents);
        pullToRefreshStudents.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefreshStudents.setRefreshing(false);
            }
        });

        mSharedPref=getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting=mSharedPref.getString("Sort","newest");
        if(mSorting.equals("newest")){
            mLayoutManager =new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }
        else if(mSorting.equals("oldest")){
            mLayoutManager=new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }
        mRecyclerView=findViewById(R.id.recyclerViewStudents);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Driver Details");

        showData();


    }

    public void refreshData(){
        showData();
    }

    private void showData(){

        options=new FirebaseRecyclerOptions.Builder<DriverInfo>().setQuery(mRef,DriverInfo.class).build();
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<DriverInfo, ViewHolderPointNumber>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPointNumber holder, int position, @NonNull DriverInfo model) {

                holder.setDetails(getApplicationContext(),"  Point: ",model.getDpointno());

            }

            @NonNull
            @Override
            public ViewHolderPointNumber onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholderpoints,parent,false);
//                mDelete=itemView.findViewById(R.id.img_delete);
                mEdit=itemView.findViewById(R.id.img_edit);

                ViewHolderPointNumber viewHolderPointNumber=new ViewHolderPointNumber(itemView);
                viewHolderPointNumber.setOnClickListener(new ViewHolderPointNumber.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView mPoint=view.findViewById(R.id.rPointNo);

                        String mPointNo=mPoint.getText().toString();

                        Intent intent =new Intent(view.getContext(),OpenStudentDetails.class);
                        intent.putExtra("pointnumber",mPointNo);
                        startActivity(intent);
                    }



                    @Override
                    public void onItemLongClick(View view, int position) {

//                        final String currenttTitle=getItem(position).getTitle();
//                        showDeleteDataDialog(currenttTitle);
//
//                        final String dpno=getItem(position).getDpointno();
//
//                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
//                        String[] options={"Update","Delete"};
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if(which==0){
//                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
//                                    intent.putExtra("title",currenttTitle);
//                                    intent.putExtra("dpointno",dpno);
//                                    intent.putExtra("Location1",mlLoc1);
//                                    intent.putExtra("Location2",mlLoc2);
//                                    intent.putExtra("Location3",mlLoc3);
//                                    intent.putExtra("Location4",mlLoc4);
//                                    intent.putExtra("Location5",mlLoc5);
//                                    intent.putExtra("Location6",mlLoc6);
//                                    intent.putExtra("Location7",mlLoc7);
//                                    intent.putExtra("Location8",mlLoc8);
//                                    intent.putExtra("Location9",mlLoc9);
//                                    intent.putExtra("Location10",mlLoc10);
//                                    intent.putExtra("Location11",mlLoc11);
//                                    intent.putExtra("Location12",mlLoc12);
//                                    intent.putExtra("Location13",mlLoc13);
//                                    intent.putExtra("Location14",mlLoc14);
//                                    intent.putExtra("Location15",mlLoc15);
//                                    intent.putExtra("Location16",mlLoc16);
//                                    intent.putExtra("Location17",mlLoc17);
//                                    startActivity(intent);
//                                }
//                                else if(which==1){
//                                    showDeleteDataDialog(currenttTitle);
//                                }
//                            }
//                        });
//                        builder.create().show();
//
                    }
                });

                return viewHolderPointNumber;
            }
        };


        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void firebaseSearch(String searchText){

        String query=searchText.toLowerCase();
        Query firebaseSearchQuery=mRef.orderByChild("dpointno").startAt(query).endAt(query + "\uf8ff");


        options=new FirebaseRecyclerOptions.Builder<DriverInfo>().setQuery(firebaseSearchQuery,DriverInfo.class).build();
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<DriverInfo, ViewHolderPointNumber>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPointNumber holder, int position, @NonNull DriverInfo model) {

                holder.setDetails(getApplicationContext(),"  Point: ",model.getDpointno());

            }

            @NonNull
            @Override
            public ViewHolderPointNumber onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholderpoints,parent,false);
//                mDelete=itemView.findViewById(R.id.img_delete);
                mEdit=itemView.findViewById(R.id.img_edit);

                ViewHolderPointNumber viewHolderPointNumber=new ViewHolderPointNumber(itemView);
                viewHolderPointNumber.setOnClickListener(new ViewHolderPointNumber.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView mPoint=view.findViewById(R.id.rPointNo);

                        String mPointNo=mPoint.getText().toString();

                        Intent intent =new Intent(view.getContext(),OpenStudentDetails.class);
                        intent.putExtra("pointnumber",mPointNo);
                        startActivity(intent);
                    }



                    @Override
                    public void onItemLongClick(View view, int position) {

//                        final String currenttTitle=getItem(position).getTitle();
//                        showDeleteDataDialog(currenttTitle);
//
//                        final String dpno=getItem(position).getDpointno();
//
//                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
//                        String[] options={"Update","Delete"};
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if(which==0){
//                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
//                                    intent.putExtra("title",currenttTitle);
//                                    intent.putExtra("dpointno",dpno);
//                                    intent.putExtra("Location1",mlLoc1);
//                                    intent.putExtra("Location2",mlLoc2);
//                                    intent.putExtra("Location3",mlLoc3);
//                                    intent.putExtra("Location4",mlLoc4);
//                                    intent.putExtra("Location5",mlLoc5);
//                                    intent.putExtra("Location6",mlLoc6);
//                                    intent.putExtra("Location7",mlLoc7);
//                                    intent.putExtra("Location8",mlLoc8);
//                                    intent.putExtra("Location9",mlLoc9);
//                                    intent.putExtra("Location10",mlLoc10);
//                                    intent.putExtra("Location11",mlLoc11);
//                                    intent.putExtra("Location12",mlLoc12);
//                                    intent.putExtra("Location13",mlLoc13);
//                                    intent.putExtra("Location14",mlLoc14);
//                                    intent.putExtra("Location15",mlLoc15);
//                                    intent.putExtra("Location16",mlLoc16);
//                                    intent.putExtra("Location17",mlLoc17);
//                                    startActivity(intent);
//                                }
//                                else if(which==1){
//                                    showDeleteDataDialog(currenttTitle);
//                                }
//                            }
//                        });
//                        builder.create().show();
//
                    }
                });

                return viewHolderPointNumber;
            }
        };


        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.studentdetails1,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_sort){
            showSortDialog();
            return true;
        }
//        if(id == R.id.action_add){
//            startActivity(new Intent(AnnouncementsUpdated.this,AddAnnouncement.class));
//            return true;
//        }
//        if(id == R.id.action_message){
//            startActivity(new Intent(AnnouncementsUpdated.this,MessageActivity.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog(){
        String[] sortOptions={"Newest","Oldest"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_sorts)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            SharedPreferences.Editor editor=mSharedPref.edit();
                            editor.putString("Sort","newest");
                            editor.apply();
                            recreate();
                        }
                        else if(which==1){
                            SharedPreferences.Editor editor=mSharedPref.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();
                            recreate();
                        }
                    }
                });
        builder.show();
    }
}
