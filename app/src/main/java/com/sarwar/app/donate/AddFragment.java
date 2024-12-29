package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class AddFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseUser user;

    Button uploadBtn;
    RelativeLayout addLayer;
    TextView ntv, Datetv, Discardbtn,Status;

    Date currentTime;
    String formattedDate;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // create your view using LayoutInflater
        View view = inflater.inflate(R.layout.fragment_add, container, false);

          TextView selectimage=(TextView)view.findViewById(R.id.addimagebtn);
          Discardbtn=(TextView)view.findViewById(R.id.discardbtn);
          uploadBtn=(Button)view.findViewById(R.id.btnuppost) ;
          Status=(TextView)view.findViewById(R.id.notcollected);
          final EditText Details=(EditText)view.findViewById(R.id.postdetailset) ;
          final EditText Phone=(EditText)view.findViewById(R.id.postphoneet);
          final EditText Address=(EditText)view.findViewById(R.id.postaddresset);

          addLayer=(RelativeLayout) view.findViewById(R.id.addlayer);
          ntv=(TextView)view.findViewById(R.id.unviewtv);
          Datetv=(TextView)view.findViewById(R.id.datetv);

          //long timeInMillis = System.currentTimeMillis();
          //currentTime.setTime(timeInMillis);

          currentTime= Calendar.getInstance().getTime();
          SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy  hh:mm aa");
          formattedDate = df.format(currentTime);
          Datetv.setText(formattedDate);

          getInfo();

          Discardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Details.setText(null);
                Phone.setText(null);
                Address.setText(null); }});

          selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Det=Details.getText().toString();
                String Pet=Phone.getText().toString();
                String Aet=Address.getText().toString();
                String Ntv=ntv.getText().toString();
                Intent intent =new Intent(getActivity(),AddwithimageActivity.class);
                intent.putExtra("key_21",Det);
                intent.putExtra("key_22",Pet);
                intent.putExtra("key_23",Aet);
                intent.putExtra("key_24",Ntv);
                startActivity(intent);
               }
           });

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                final DatabaseReference ref=database.getReference().child(user.getUid());
                final DatabaseReference reference=database.getReference("Donation Post").push();
                String Key=reference.getKey();
                String Uid=ref.getKey();

                final String details=Details.getText().toString();
                final String phone=Phone.getText().toString();
                final String address=Address.getText().toString();
                final String name= ntv.getText().toString();
                final String status=Status.getText().toString();
                final String key=Key;
                final String current_time=formattedDate;
                final String url=null;
                final String uid=Uid;

                if (TextUtils.isEmpty(details)){
                    Snackbar.make(addLayer,"Write Details Information.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }
                if (TextUtils.isEmpty(phone)){
                    Snackbar.make(addLayer,"Write Your Phone Number.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }
                if (TextUtils.isEmpty(address)){
                    Snackbar.make(addLayer,"Write The Address to Collect Your Item.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setTitle("Uploading Post");
                dialog.show();

                DonationPost donatePost = new DonationPost(
                        name,
                        url,
                        details,
                        phone,
                        address,
                        status,
                        uid,
                        key,
                        current_time);

                reference.setValue(donatePost);


                dialog.dismiss();
                Details.setText(null);
                Phone.setText(null);
                Address.setText(null);

                Snackbar.make(addLayer, "Your post is successfully uploaded.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
            }
        });





        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public  void getInfo() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user;
        user = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users").child(user.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // for(DataSnapshot ds:dataSnapshot.getChildren()){
                String name = dataSnapshot.child("name").getValue().toString();
                ntv.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history ,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item1){
        int id=item1.getItemId();

        if (id== R.id.history){
            Intent intent=new Intent(getActivity(), UserPostHistoryActivity.class);
            startActivity(intent);
        }
        return true;
    }
}