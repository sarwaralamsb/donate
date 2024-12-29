package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;



    private List<DonationPost> HomepostList;
    private ListView listView;
    private PostViewAdapter hadapter;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.fragment_home, container, false);


       // mAuth= FirebaseAuth.getInstance();
        //user=mAuth.getCurrentUser();

        //String key1=reference.getKey();

        listView = (ListView)view.findViewById(R.id.listhome);
        //DatabaseReference newRef=reference.child(key1).getRef();





        /*refernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                       DonationPost donationPost = snapshot.getValue(DonationPost.class);
                       HomepostList.add(0,donationPost);
                }
                //Init adapter
                hadapter = new PostViewAdapter(getActivity(), R.layout.postdamo_activity, HomepostList);
                listView.setAdapter(hadapter);
                //adapter.notifyDataSetChanged();
                //  listView.smoothScrollToPosition(0);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });*/



        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        HomepostList = new ArrayList<>();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference refernce=database.getReference("Donation Post").getRef();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading Content");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


         refernce.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 progressDialog.dismiss();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     DonationPost donationPost = snapshot.getValue(DonationPost.class);
                     HomepostList.add(0,donationPost);
                 }
                 hadapter = new PostViewAdapter(getActivity(), R.layout.postdamo_activity, HomepostList);
                 listView.setAdapter(hadapter);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

                 progressDialog.dismiss();
             }
         });





    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload ,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item1){
        int id=item1.getItemId();
        if (id== R.id.reloadm){
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
