package com.user.studentapi;

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

public class OpenStudentDetails extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef,ref,refer;
    StudentInfo student;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String pointforquery,email,studentemail;

    FirebaseRecyclerAdapter<StudentInfo,ViewHolderStudents> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<StudentInfo> options;
    public ImageView mDelete;
    public ImageView mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_student_details);
        email=getIntent().getStringExtra("email");
//        pointforquery=getIntent().getStringExtra("pointnumber");
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Your Details");

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefreshStudentsOpen);
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
        mRecyclerView=findViewById(R.id.recyclerViewStudentsOpen);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mRef=mFirebaseDatabase.getReference("New Student Register");
        showData();


    }

    public void refreshData(){
        showData();
    }

    private void showData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        String id = user.getUid();

        DatabaseReference databaseReference = firebaseDatabase.getReference("New Student Register");
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                           if(dataSnapshot.exists()) {
                                                                               studentemail = dataSnapshot.child("email").getValue().toString();
//                                                                               Toast.makeText(OpenStudentDetails.this,studentemail,Toast.LENGTH_SHORT).show();
                                                                           }
                                                                           else{
//                                                                               Toast.makeText(OpenStudentDetails.this,"empty",Toast.LENGTH_SHORT).show();
                                                                           }
                                                                       }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query firebaseQuery = mRef.orderByChild("email").equalTo(studentemail);
        options = new FirebaseRecyclerOptions.Builder<StudentInfo>().setQuery(firebaseQuery, StudentInfo.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudentInfo, ViewHolderStudents>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderStudents holder, int position, @NonNull StudentInfo model) {

                holder.setDetails(getApplicationContext(), "  Student's Name: " + model.getStudentName(), "  Student's ID: " + model.getStudentID(), "  Parent/Guardian's CNIC: " + model.getGaurdianCNIC(), "  Student's Email: " + model.getEmail(), "  Student's Point Number: " + model.getPointnumber(), "  Student's Contact: " + model.getContact(), "  Date Joined: " + model.getJoindate(),"  Account: "+model.getAccount());
            }

            @NonNull
            @Override
            public ViewHolderStudents onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholderstudents, parent, false);
//                mDelete=itemView.findViewById(R.id.img_delete);


                ViewHolderStudents viewHolderStudents = new ViewHolderStudents(itemView);
                viewHolderStudents.setOnClickListener(new ViewHolderStudents.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                                final String name=getItem(position).getStudentName();
//                                final String email=getItem(position).getEmail();
//                                final String pointno=getItem(position).getPointnumber();
//                                final String contact=getItem(position).getPointnumber();
//                                final String cnic=getItem(position).getGaurdianCNIC();
//                                final String id=getItem(position).getStudentID();


//                                AlertDialog.Builder builder=new AlertDialog.Builder(OpenStudentDetails.this);
//                                String[] options={"Update","Delete"};
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        if(which==0){
//                                            Intent intent =new Intent(OpenStudentDetails.this,AddStudent.class);
//                                            intent.putExtra("studentName",name);
//                                            intent.putExtra("gaurdianCNIC",cnic);
//                                            intent.putExtra("pointnumber",pointno);
//                                            intent.putExtra("email",email);
//                                            intent.putExtra("contact",contact);
//                                            intent.putExtra("studentID",id);
//                                            startActivity(intent);
//                                        }
//                                        else if(which==1){
//                                            showDeleteDataDialog(id);
//                                        }
//                                    }
//                                });
//                                builder.create().show();
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

                return viewHolderStudents;
            }
        };
//    }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




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
//                firebaseSearch(query,pointforquery);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                firebaseSearch(newText,pointforquery);
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
