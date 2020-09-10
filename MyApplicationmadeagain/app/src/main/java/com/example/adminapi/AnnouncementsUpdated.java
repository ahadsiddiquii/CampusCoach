package com.example.adminapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnnouncementsUpdated extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef,ref,refer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    AnnounceInfo annouce;


    FirebaseRecyclerAdapter<AnnounceInfo,ViewHolderAnnounce> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<AnnounceInfo> options;
    public ImageView mDelete;
    public ImageView mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements_updated);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Announcements");

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
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
        mRecyclerView=findViewById(R.id.recyclerViewAnnouncements);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Announcements");
        Date date=new Date();
        Date newDate=new Date(date.getTime());
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String todaysdate=dt.format(newDate);
        check();
        remove();
        ref = database.getReference("Announcements");
        Query mQuery=ref.orderByChild("intdate").equalTo(todaysdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()) {
                   showData();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void refreshData(){
        check();
        remove();
        Date date=new Date();
        Date newDate=new Date(date.getTime());
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        final String todaysdate=dt.format(newDate);
        ref = database.getReference("Announcements");
        Query mQuery=ref.orderByChild("intdate").equalTo(todaysdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    showData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData(){

        options=new FirebaseRecyclerOptions.Builder<AnnounceInfo>().setQuery(mRef,AnnounceInfo.class).build();
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<AnnounceInfo, ViewHolderAnnounce>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnnounce holder, int position, @NonNull AnnounceInfo model) {
                int numberano= (int) model.getAno();
                int i=1;
                holder.setDetails(getApplicationContext(),"  "+numberano+"."," "+model.getDannounce(),model.getIntdate(),model.getInttime(),model.getExtension());
                i++;
            }

            @NonNull
            @Override
            public ViewHolderAnnounce onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announceinfoupdated,parent,false);
//                mDelete=itemView.findViewById(R.id.img_delete);
                mEdit=itemView.findViewById(R.id.img_edit);

                ViewHolderAnnounce viewHolderAnnounce=new ViewHolderAnnounce(itemView);
                viewHolderAnnounce.setOnClickListener(new ViewHolderAnnounce.ClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        final long Announceno=getItem(position).getAno();
                        final String Announcement=getItem(position).getDannounce();
                        final String Description=getItem(position).getExtension();
                        final String Time=getItem(position).getInttime();
                        final String Date=getItem(position).getIntdate();
                        final String a= String.valueOf(Announceno);
                        AlertDialog.Builder builder=new AlertDialog.Builder(AnnouncementsUpdated.this);
                        String[] options={"Open","Update","Delete"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    Intent intent =new Intent(AnnouncementsUpdated.this,OpenAnnouncement.class);
                                    intent.putExtra("ano",a);
                                    intent.putExtra("dannounce",Announcement);
                                    intent.putExtra("extension",Description);
                                    intent.putExtra("inttime",Time);
                                    intent.putExtra("intdate",Date);
                                    startActivity(intent);
                                }
                                else if(which==1){
                                    Intent intent =new Intent(AnnouncementsUpdated.this,AddAnnouncement.class);
//                                    intent.putExtra("ano",Announceno);
                                    intent.putExtra("dannounce",Announcement);
                                    intent.putExtra("extension",Description);
                                    startActivity(intent);
                                }
                                else if(which==2){
                                    showDeleteDataDialog(Announcement);
                                }
                            }
                        });
                        builder.create().show();
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

                return viewHolderAnnounce;
            }
        };


        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void remove(){
        Date date = new Date();
        Date newDate = new Date(date.getTime() );
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("adano").equalTo(0);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    refer = database.getReference("Announcements");
                    refer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                d.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void check() {
        Date date = new Date();
        Date newDate = new Date(date.getTime());
//        + (604800000L * 2) + (24 * 60 * 60)
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        ref = database.getReference("Admin Announce");
        Query mQuery=ref.orderByChild("intdate").equalTo(stringdate);
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        break;
                    }
                }
                else{
                    AdminAnnounce adminAnnounce=new AdminAnnounce(0,stringdate);
                    FirebaseDatabase.getInstance().getReference("Admin Announce")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(adminAnnounce).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),"Announcement Successful",Toast.LENGTH_SHORT).show();
//                    startActivity( new Intent(Announcements.this,Announcements.class));
//                    Announcements.this.finish();
                            } else {
//                    Toast.makeText(getApplicationContext(),"Announcement Unsuccessful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showDeleteDataDialog(final String Announcement) {
        AlertDialog.Builder builder=new AlertDialog.Builder(AnnouncementsUpdated.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this Announcement?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mQuery=mRef.orderByChild("dannounce").equalTo(Announcement);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            annouce = ds.getValue(AnnounceInfo.class);
                            ds.getRef().removeValue();
                            long no=annouce.getAno()-1;
                            Date date=new Date();
                            Date newDate=new Date(date.getTime());
//        +(604800000L*2)+(24*60*60)
                            SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
                            final String stringdate=dt.format(newDate);
                            AdminAnnounce adminAnnounce=new AdminAnnounce(no,stringdate);
                            FirebaseDatabase.getInstance().getReference("Admin Announce")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(adminAnnounce).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(),"Announcement Successful",Toast.LENGTH_SHORT).show();
//                    startActivity( new Intent(Announcements.this,Announcements.class));
//                    Announcements.this.finish();
                                    } else {
//                    Toast.makeText(getApplicationContext(),"Announcement Unsuccessful",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        Toast.makeText(AnnouncementsUpdated.this, "Announcement deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(AnnouncementsUpdated.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
        getMenuInflater().inflate(R.menu.announcements,menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                firebaseSearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                firebaseSearch(newText);
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
        if(id == R.id.action_add){
            startActivity(new Intent(AnnouncementsUpdated.this,AddAnnouncement.class));
            return true;
        }
        if(id == R.id.action_message){
            startActivity(new Intent(AnnouncementsUpdated.this,MessageActivity.class));
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

