package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPostHistoryActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;


    private List<DonationPost> UserpostList;
    private ListView listView;
    private PostViewAdapter adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post_history);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ref1=database.getReference("Donation Post").child(user.getUid());
        String UID=ref1.getKey();

        final DatabaseReference reference=database.getReference("Donation Post");
        Query query=reference.orderByChild("uid").equalTo(UID);

        UserpostList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listuserposthistory);
        final LinearLayout PhistoryL=(LinearLayout)findViewById(R.id.posthistorylayer) ;

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    DonationPost donationPost = snapshot.getValue(DonationPost.class);
                    UserpostList.add(0,donationPost);
                    }

                //Init adapter
                adapter = new PostViewAdapter(UserPostHistoryActivity.this, R.layout.postdamo_activity, UserpostList);
                //Set adapter for listview

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(PhistoryL,"Long Press To Delete Your Post",Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            }

        });

       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
               android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(UserPostHistoryActivity.this);
               View mView = getLayoutInflater().inflate(R.layout.popup_userpostitemdelete, null);

               Button delPostBtn = (Button) mView.findViewById(R.id.delpoppostbtn);
               Button delPostNoBtn = (Button) mView.findViewById(R.id.delpoppostnobtn);

               mBuilder.setView(mView);
               final android.app.AlertDialog dialog = mBuilder.create();
               dialog.show();


               delPostNoBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialog.dismiss();
                   }
               });

               delPostBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialog.dismiss();
                       DonationPost donationPost=(DonationPost) adapterView.getItemAtPosition(position);
                       String key=donationPost.getKey();

                       FirebaseDatabase ndatabase=FirebaseDatabase.getInstance();
                       final DatabaseReference ref=ndatabase.getReference("Donation Post");

                       ref.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               if (task.isSuccessful()){
                                   Toast.makeText(UserPostHistoryActivity.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                                   finish();
                                   startActivity(getIntent());
                               }else {
                                   Toast.makeText(UserPostHistoryActivity.this, "Post Not Deleted!!!", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

                      }
               });
               return true;
           }
       });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true; }
        return super.onOptionsItemSelected(item);
    }

}
