package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostviewAdminActivity extends AppCompatActivity {
    private List<DonationPost> AdminViewpostList;
    private ListView listView;
    private PostViewAdapter adapter;
    private ProgressDialog progressDialog;

    String updatedstatus;
    LinearLayout PVAdLayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview_admin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listpostviewadmin);
        AdminViewpostList = new ArrayList<>();
        PVAdLayer=(LinearLayout)findViewById(R.id.adminpostviewlayer);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Donation Post").getRef();

        progressDialog = new ProgressDialog(PostviewAdminActivity.this);
        progressDialog.setTitle("Loading Content");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonationPost donationPost = snapshot.getValue(DonationPost.class);
                    AdminViewpostList.add(0,donationPost);
                }
                adapter = new PostViewAdapter(PostviewAdminActivity.this, R.layout.postdamo_activity, AdminViewpostList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });



        //listview long click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(PostviewAdminActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.popup_adminpostdel, null);

                Button deletePost=(Button)mView.findViewById(R.id.delpoppostbtnadmin);
                Button CancelDel=(Button)mView.findViewById(R.id.delpoppostnobtnadmin);

                mBuilder.setView(mView);
                final android.app.AlertDialog dialog = mBuilder.create();
                dialog.show();

                CancelDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                deletePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        DonationPost donationPost=(DonationPost) adapterView.getItemAtPosition(position);
                        String key=donationPost.getKey();

                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        final DatabaseReference ref=database.getReference("Donation Post");

                        ref.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Snackbar.make(PVAdLayer, "Post Deleted successfully.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    finish();
                                    startActivity(getIntent());
                                }else {
                                    Snackbar.make(PVAdLayer, "Post Not Deleted", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        });
                    }
                });
                return true;
            }
        });





        //listview click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(PostviewAdminActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.popup_updatestatus, null);

                final Button updatePostStatus=(Button)mView.findViewById(R.id.updatestatusbtnadmin) ;
                RadioGroup radioGroup=(RadioGroup)mView.findViewById(R.id.rg);
                RadioButton rb1=(RadioButton) mView.findViewById(R.id.RB1recv);
                RadioButton rb2=(RadioButton)mView.findViewById(R.id.RB2way);
                RadioButton rb3=(RadioButton)mView.findViewById(R.id.RB3cld);
                RadioButton rb4=(RadioButton)mView.findViewById(R.id.RB4cancel);

                mBuilder.setView(mView);
                final android.app.AlertDialog dialog = mBuilder.create();
                dialog.show();

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        if(checkId == R.id.RB1recv){
                            updatedstatus="Received"; }
                        if(checkId==R.id.RB2way){
                            updatedstatus="On The Way"; }
                        if(checkId==R.id.RB3cld){
                            updatedstatus="Collected"; }
                        if(checkId == R.id.RB4cancel){
                            updatedstatus="Cancelled"; }
                    }
                });

                updatePostStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DonationPost donationPost = (DonationPost) adapterView.getItemAtPosition(position);
                        String key = donationPost.getKey();
                        final DatabaseReference databaseReference = database.getReference("Donation Post").child(key);
                        databaseReference.child("status").setValue(updatedstatus);
                        dialog.dismiss();
                        Snackbar.make(PVAdLayer, "Status is changed successfully.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        //finish();
                        //startActivity(getIntent());
                        }
                    });
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.adminviewmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            startActivity(new Intent(this, AdminloginActivity.class));
        }
        if(id== R.id.reloadpageadmin){
            Intent intent =new Intent(PostviewAdminActivity.this,PostviewAdminActivity.class);
            startActivity(intent);
        }
        if(id== R.id.exitadmin){
            Intent intent =new Intent(PostviewAdminActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
