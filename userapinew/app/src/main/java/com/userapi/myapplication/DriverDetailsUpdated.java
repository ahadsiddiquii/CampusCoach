package com.userapi.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DriverDetailsUpdated extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    String studentpointnumber;

    FirebaseRecyclerAdapter<DriverInfo,ViewHolderDriver> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<DriverInfo> options;
    public ImageView mDelete;
    public ImageView mEdit;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String email,studentpointno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details_updated);
        email=getIntent().getStringExtra("emails");

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Your Driver's Details");

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshDrivers);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
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

        mRecyclerView=findViewById(R.id.recyclerViewDrivers);
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
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    String id = user.getUid();
    DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
    databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                studentpointno = dataSnapshot.child("pointnumber").getValue().toString();
//                Toast.makeText(OpenStudentDetails.this,studentpointnumber,Toast.LENGTH_SHORT).show();
            }
            else{
//                Toast.makeText(OpenStudentDetails.this,"empty",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
//    Toast.makeText(DriverDetailsUpdated.this,email,Toast.LENGTH_SHORT).show();
//    Query query = FirebaseDatabase.getInstance().getReference("New Student Register")
//            .orderByChild("email")
//            .equalTo(email);
//    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                             @Override
//                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                 if (dataSnapshot.exists()) {
//                                                    studentpointnumber = dataSnapshot.child("pointnumber").getValue().toString();
//                                                     Toast.makeText(DriverDetailsUpdated.this,studentpointnumber,Toast.LENGTH_SHORT).show();
//                                                 }
//                                                 else {
//                                                     Toast.makeText(DriverDetailsUpdated.this, "empty", Toast.LENGTH_SHORT).show();
//                                                 }
//                                             }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
                Query firebaseQuery = mRef.orderByChild("dpointno").equalTo(studentpointno);
                options = new FirebaseRecyclerOptions.Builder<DriverInfo>().setQuery(firebaseQuery, DriverInfo.class).build();
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DriverInfo, ViewHolderDriver>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderDriver holder, int position, @NonNull DriverInfo model) {


                        holder.setDetails(getApplicationContext(), "  Driver's Name: " + model.getDname(), "  Contact Number: " + model.getDcontact(), "  Point: " + model.getDpointno(), "  Driver's CNIC: " + model.getDcnic(), "  Joined Date: " + model.getDjoindate(), "  Driver's Email: " + model.getDemail());

                    }

                    @NonNull
                    @Override
                    public ViewHolderDriver onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholderdriver, parent, false);
//                mDelete=itemView.findViewById(R.id.img_delete);


                        ViewHolderDriver viewHolderDriver = new ViewHolderDriver(itemView);
            viewHolderDriver.setOnClickListener(new ViewHolderDriver.ClickListener(){
                @Override
                public void onItemClick(View view, int position) {
////                        final long Announceno=getItem(position).getAno();
//                    final String Dname=getItem(position).getDname();
//                    final String Djoineddate=getItem(position).getDjoindate();
//                    final String Demail=getItem(position).getDemail();
//                    final String Dpointno=getItem(position).getDpointno();
//                    final String Dcontact=getItem(position).getDcontact();
//                    final String Dcnic=getItem(position).getDcnic();
//
//
//
//
                }
//
//
//
                @Override
                public void onItemLongClick(View view, int position) {
//
////                        final String currenttTitle=getItem(position).getTitle();
////                        showDeleteDataDialog(currenttTitle);
////
////                        final String dpno=getItem(position).getDpointno();
////
////                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
////                        String[] options={"Update","Delete"};
////                        builder.setItems(options, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                if(which==0){
////                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
////                                    intent.putExtra("title",currenttTitle);
////                                    intent.putExtra("dpointno",dpno);
////                                    intent.putExtra("Location1",mlLoc1);
////                                    intent.putExtra("Location2",mlLoc2);
////                                    intent.putExtra("Location3",mlLoc3);
////                                    intent.putExtra("Location4",mlLoc4);
////                                    intent.putExtra("Location5",mlLoc5);
////                                    intent.putExtra("Location6",mlLoc6);
////                                    intent.putExtra("Location7",mlLoc7);
////                                    intent.putExtra("Location8",mlLoc8);
////                                    intent.putExtra("Location9",mlLoc9);
////                                    intent.putExtra("Location10",mlLoc10);
////                                    intent.putExtra("Location11",mlLoc11);
////                                    intent.putExtra("Location12",mlLoc12);
////                                    intent.putExtra("Location13",mlLoc13);
////                                    intent.putExtra("Location14",mlLoc14);
////                                    intent.putExtra("Location15",mlLoc15);
////                                    intent.putExtra("Location16",mlLoc16);
////                                    intent.putExtra("Location17",mlLoc17);
////                                    startActivity(intent);
////                                }
////                                else if(which==1){
////                                    showDeleteDataDialog(currenttTitle);
////                                }
////                            }
////                        });
////                        builder.create().show();
////
                }
            });

                        return viewHolderDriver;
                    }
                };




    mRecyclerView.setLayoutManager(mLayoutManager);
    firebaseRecyclerAdapter.startListening();
    mRecyclerView.setAdapter(firebaseRecyclerAdapter);

}



    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                firebaseSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                firebaseSearch(newText);
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_sort){
            showSortDialog();
            return true;
        }

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
