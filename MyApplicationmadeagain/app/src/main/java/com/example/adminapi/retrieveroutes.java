package com.example.adminapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
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
//import android.widget.SearchView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class retrieveroutes extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Locations,ViewHolder1> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Locations> options;
    public ImageView mDelete;
    public ImageView mEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieveroutes);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Routes");

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshroutes);
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

        mRecyclerView=findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("Routes");

        showData();



    }

    public void refreshData(){
        showData();
    }



    private void showDeleteDataDialog(final String currentTitle) {
        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this Route?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query mQuery=mRef.orderByChild("title").equalTo(currentTitle);
                mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(retrieveroutes.this, "Route deleted successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(retrieveroutes.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showData(){

        options=new FirebaseRecyclerOptions.Builder<Locations>().setQuery(mRef,Locations.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Locations, ViewHolder1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder1 holder, int position, @NonNull Locations model) {
                holder.setDetails(getApplicationContext(),model.getTitle(),model.getLocation1(),model.getLocation2(),model.getLocation3(),model.getLocation4(),model.getLocation5(),model.getLocation6(),model.getLocation7(),model.getLocation8(),model.getLocation9(),model.getLocation10(),model.getLocation11(),model.getLocation12(),model.getLocation13(),model.getLocation14(),model.getLocation15(),model.getLocation16(),model.getLocation17());
            }

            @NonNull
            @Override
            public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations,parent,false);
//                mDelete=itemView.findViewById(R.id.img_delete);
                mEdit=itemView.findViewById(R.id.img_edit);

                ViewHolder1 viewHolder1=new ViewHolder1(itemView);
                viewHolder1.setOnClickListener(new ViewHolder1.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final String currenttTitle=getItem(position).getTitle();
                        final String dpno=getItem(position).getDpointno();
                        final String mlLoc1 =getItem(position).getLocation1();
                        final String mlLoc2=getItem(position).getLocation2();
                        final String mlLoc3=getItem(position).getLocation3();
                        final String mlLoc4=getItem(position).getLocation4();
                        final String mlLoc5=getItem(position).getLocation5();
                        final String mlLoc6=getItem(position).getLocation6();
                        final String mlLoc7=getItem(position).getLocation7();
                        final String mlLoc8=getItem(position).getLocation8();
                        final String mlLoc9=getItem(position).getLocation9();
                        final String mlLoc10=getItem(position).getLocation10();
                        final String mlLoc11=getItem(position).getLocation11();
                        final String mlLoc12=getItem(position).getLocation12();
                        final String mlLoc13=getItem(position).getLocation13();
                        final String mlLoc14=getItem(position).getLocation14();
                        final String mlLoc15=getItem(position).getLocation15();
                        final String mlLoc16=getItem(position).getLocation16();
                        final String mlLoc17=getItem(position).getLocation17();


                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
                        String[] options={"Update","Delete"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
                                    intent.putExtra("title",currenttTitle);
                                    intent.putExtra("dpointno",dpno);
                                    intent.putExtra("Location1",mlLoc1);
                                    intent.putExtra("Location2",mlLoc2);
                                    intent.putExtra("Location3",mlLoc3);
                                    intent.putExtra("Location4",mlLoc4);
                                    intent.putExtra("Location5",mlLoc5);
                                    intent.putExtra("Location6",mlLoc6);
                                    intent.putExtra("Location7",mlLoc7);
                                    intent.putExtra("Location8",mlLoc8);
                                    intent.putExtra("Location9",mlLoc9);
                                    intent.putExtra("Location10",mlLoc10);
                                    intent.putExtra("Location11",mlLoc11);
                                    intent.putExtra("Location12",mlLoc12);
                                    intent.putExtra("Location13",mlLoc13);
                                    intent.putExtra("Location14",mlLoc14);
                                    intent.putExtra("Location15",mlLoc15);
                                    intent.putExtra("Location16",mlLoc16);
                                    intent.putExtra("Location17",mlLoc17);
                                    startActivity(intent);
                                }
                                else if(which==1){
                                    showDeleteDataDialog(currenttTitle);
                                }
                            }
                        });
                        builder.create().show();
                    }



                    @Override
                    public void onItemLongClick(View view, int position) {

                        final String currenttTitle=getItem(position).getTitle();
                        showDeleteDataDialog(currenttTitle);

                        final String dpno=getItem(position).getDpointno();
                        final String mlLoc1 =getItem(position).getLocation1();
                        final String mlLoc2=getItem(position).getLocation2();
                        final String mlLoc3=getItem(position).getLocation3();
                        final String mlLoc4=getItem(position).getLocation4();
                        final String mlLoc5=getItem(position).getLocation5();
                        final String mlLoc6=getItem(position).getLocation6();
                        final String mlLoc7=getItem(position).getLocation7();
                        final String mlLoc8=getItem(position).getLocation8();
                        final String mlLoc9=getItem(position).getLocation9();
                        final String mlLoc10=getItem(position).getLocation10();
                        final String mlLoc11=getItem(position).getLocation11();
                        final String mlLoc12=getItem(position).getLocation12();
                        final String mlLoc13=getItem(position).getLocation13();
                        final String mlLoc14=getItem(position).getLocation14();
                        final String mlLoc15=getItem(position).getLocation15();
                        final String mlLoc16=getItem(position).getLocation16();
                        final String mlLoc17=getItem(position).getLocation17();
                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
                        String[] options={"Update","Delete"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
                                    intent.putExtra("title",currenttTitle);
                                    intent.putExtra("dpointno",dpno);
                                    intent.putExtra("Location1",mlLoc1);
                                    intent.putExtra("Location2",mlLoc2);
                                    intent.putExtra("Location3",mlLoc3);
                                    intent.putExtra("Location4",mlLoc4);
                                    intent.putExtra("Location5",mlLoc5);
                                    intent.putExtra("Location6",mlLoc6);
                                    intent.putExtra("Location7",mlLoc7);
                                    intent.putExtra("Location8",mlLoc8);
                                    intent.putExtra("Location9",mlLoc9);
                                    intent.putExtra("Location10",mlLoc10);
                                    intent.putExtra("Location11",mlLoc11);
                                    intent.putExtra("Location12",mlLoc12);
                                    intent.putExtra("Location13",mlLoc13);
                                    intent.putExtra("Location14",mlLoc14);
                                    intent.putExtra("Location15",mlLoc15);
                                    intent.putExtra("Location16",mlLoc16);
                                    intent.putExtra("Location17",mlLoc17);
                                    startActivity(intent);
                                }
                                else if(which==1){
                                    showDeleteDataDialog(currenttTitle);
                                }
                            }
                        });
                        builder.create().show();

                    }
                });

                return viewHolder1;
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void firebaseSearch(String searchText){

        String query=searchText.toLowerCase();
        Query firebaseSearchQuery=mRef.orderByChild("search").startAt(query).endAt(query + "\uf8ff");


        options=new FirebaseRecyclerOptions.Builder<Locations>().setQuery(firebaseSearchQuery,Locations.class).build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Locations, ViewHolder1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder1 holder, int position, @NonNull Locations model) {
                holder.setDetails(getApplicationContext(),model.getTitle(),model.getLocation1(),model.getLocation2(),model.getLocation3(),model.getLocation4(),model.getLocation5(),model.getLocation6(),model.getLocation7(),model.getLocation8(),model.getLocation9(),model.getLocation10(),model.getLocation11(),model.getLocation12(),model.getLocation13(),model.getLocation14(),model.getLocation15(),model.getLocation16(),model.getLocation17());
            }

            @NonNull
            @Override
            public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations,parent,false);

                ViewHolder1 viewHolder1=new ViewHolder1(itemView);
                viewHolder1.setOnClickListener(new ViewHolder1.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView mTitleTv=view.findViewById(R.id.rTitleTv);
                        TextView mLocation1=view.findViewById(R.id.Location1);
                        TextView mLocation2=view.findViewById(R.id.Location2);
                        TextView mLocation3=view.findViewById(R.id.Location3);
                        TextView mLocation4=view.findViewById(R.id.Location4);
                        TextView mLocation5=view.findViewById(R.id.Location5);
                        TextView mLocation6=view.findViewById(R.id.Location6);
                        TextView mLocation7=view.findViewById(R.id.Location7);
                        TextView mLocation8=view.findViewById(R.id.Location8);
                        TextView mLocation9=view.findViewById(R.id.Location9);
                        TextView mLocation10=view.findViewById(R.id.Location10);
                        TextView mLocation11=view.findViewById(R.id.Location11);
                        TextView mLocation12=view.findViewById(R.id.Location12);
                        TextView mLocation13=view.findViewById(R.id.Location13);
                        TextView mLocation14=view.findViewById(R.id.Location14);
                        TextView mLocation15=view.findViewById(R.id.Location15);
                        TextView mLocation16=view.findViewById(R.id.Location16);
                        TextView mLocation17=view.findViewById(R.id.Location17);

                        String mTitle=mTitleTv.getText().toString();
                        String mLoc1 = mLocation1.getText().toString();
                        String mLoc2=mLocation2.getText().toString();
                        String mLoc3=mLocation3.getText().toString();
                        String mLoc4=mLocation4.getText().toString();
                        String mLoc5=mLocation5.getText().toString();
                        String mLoc6=mLocation6.getText().toString();
                        String mLoc7=mLocation7.getText().toString();
                        String mLoc8=mLocation8.getText().toString();
                        String mLoc9=mLocation9.getText().toString();
                        String mLoc10=mLocation10.getText().toString();
                        String mLoc11=mLocation11.getText().toString();
                        String mLoc12=mLocation12.getText().toString();
                        String mLoc13=mLocation13.getText().toString();
                        String mLoc14=mLocation14.getText().toString();
                        String mLoc15=mLocation15.getText().toString();
                        String mLoc16=mLocation16.getText().toString();
                        String mLoc17=mLocation17.getText().toString();

                        Intent intent =new Intent(view.getContext(),OpenRoutes.class);
                        intent.putExtra("title",mTitle);
                        intent.putExtra("Location1",mLoc1);
                        intent.putExtra("Location2",mLoc2);
                        intent.putExtra("Location3",mLoc3);
                        intent.putExtra("Location4",mLoc4);
                        intent.putExtra("Location5",mLoc5);
                        intent.putExtra("Location6",mLoc6);
                        intent.putExtra("Location7",mLoc7);
                        intent.putExtra("Location8",mLoc8);
                        intent.putExtra("Location9",mLoc9);
                        intent.putExtra("Location10",mLoc10);
                        intent.putExtra("Location11",mLoc11);
                        intent.putExtra("Location12",mLoc12);
                        intent.putExtra("Location13",mLoc13);
                        intent.putExtra("Location14",mLoc14);
                        intent.putExtra("Location15",mLoc15);
                        intent.putExtra("Location16",mLoc16);
                        intent.putExtra("Location17",mLoc17);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        final String currentTitle=getItem(position).getTitle();
                        final String mLoc1 =getItem(position).getTitle();
                        final String mLoc2=getItem(position).getTitle();
                        final String mLoc3=getItem(position).getTitle();
                        final String mLoc4=getItem(position).getTitle();
                        final String mLoc5=getItem(position).getTitle();
                        final String mLoc6=getItem(position).getTitle();
                        final String mLoc7=getItem(position).getTitle();
                        final String mLoc8=getItem(position).getTitle();
                        final String mLoc9=getItem(position).getTitle();
                        final String mLoc10=getItem(position).getTitle();
                        final String mLoc11=getItem(position).getTitle();
                        final String mLoc12=getItem(position).getTitle();
                        final String mLoc13=getItem(position).getTitle();
                        final String mLoc14=getItem(position).getTitle();
                        final String mLoc15=getItem(position).getTitle();
                        final String mLoc16=getItem(position).getTitle();
                        final String mLoc17=getItem(position).getTitle();


                        AlertDialog.Builder builder=new AlertDialog.Builder(retrieveroutes.this);
                        String[] options={"Update","Delete"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent =new Intent(retrieveroutes.this,AddRouteActivity.class);
                                    intent.putExtra("title",currentTitle);
                                    intent.putExtra("Location1",mLoc1);
                                    intent.putExtra("Location2",mLoc2);
                                    intent.putExtra("Location3",mLoc3);
                                    intent.putExtra("Location4",mLoc4);
                                    intent.putExtra("Location5",mLoc5);
                                    intent.putExtra("Location6",mLoc6);
                                    intent.putExtra("Location7",mLoc7);
                                    intent.putExtra("Location8",mLoc8);
                                    intent.putExtra("Location9",mLoc9);
                                    intent.putExtra("Location10",mLoc10);
                                    intent.putExtra("Location11",mLoc11);
                                    intent.putExtra("Location12",mLoc12);
                                    intent.putExtra("Location13",mLoc13);
                                    intent.putExtra("Location14",mLoc14);
                                    intent.putExtra("Location15",mLoc15);
                                    intent.putExtra("Location16",mLoc16);
                                    intent.putExtra("Location17",mLoc17);
                                    startActivity(intent);
                                }
                                else if(which==1){
                                    showDeleteDataDialog(currentTitle);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });

                return viewHolder1;
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
        getMenuInflater().inflate(R.menu.menu,menu);
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
        if(id == R.id.action_add){
            startActivity(new Intent(retrieveroutes.this,AddRouteActivity.class));
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
