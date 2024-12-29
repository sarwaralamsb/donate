package com.sarwar.app.donate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserprofileFragment extends Fragment {


    TextView UsernameTV,EmailTV, PhoneTV,AddressTv,IconLetterTV;
    Button ChangePassBtn, DeleteAccountBtn;

    FirebaseAuth mAuth;
    FirebaseUser user;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // create your view using LayoutInflater
        View view= inflater.inflate(R.layout.fragment_userprofile, container, false);


        UsernameTV=(TextView)view.findViewById(R.id.tvProfileName);
        EmailTV=(TextView)view.findViewById(R.id.tvEmail);
        PhoneTV=(TextView)view.findViewById(R.id.tvProPhone);
        AddressTv=(TextView)view.findViewById(R.id.tvProAddress);
        IconLetterTV=(TextView)view.findViewById(R.id.usericon);
        ChangePassBtn=(Button)view.findViewById(R.id.changepasswordbtn) ;
        DeleteAccountBtn=(Button)view.findViewById(R.id.delaccountbtn) ;

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // for(DataSnapshot ds:dataSnapshot.getChildren()){
                String name=dataSnapshot.child("name").getValue().toString();
                String email=dataSnapshot.child("email").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String address=dataSnapshot.child("address").getValue().toString();

                UsernameTV.setText(name);
                EmailTV.setText(email);
                PhoneTV.setText(phone);
                AddressTv.setText(address);
                IconLetterTV.setText(name.substring(0,1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChangePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepass();
            }
        });
        DeleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });


        return view;
    }

    //method continue from here
    public void changepass(){
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.popup_passchange, null);

        final EditText newPass=(EditText) mView.findViewById(R.id.newpasset);
        final EditText newConfirmPass=(EditText) mView.findViewById(R.id.newpassconfirm);
        Button updatePass =(Button) mView.findViewById(R.id.changepassbtn);

        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NewPass=newPass.getText().toString();
                String ConPass=newConfirmPass.getText().toString();

                if (TextUtils.isEmpty(NewPass)) {
                    Toast.makeText(getActivity(), "Please write your password ", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(ConPass)){
                    Toast.makeText(getActivity(), "Please write your password to confirm", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!NewPass.equals(ConPass)){
                    Toast.makeText(getActivity(), "Passwords didn't match, Try Again.", Toast.LENGTH_LONG).show();
                    newPass.setText(null);
                    newConfirmPass.setText(null);
                }else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Password Change Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Something Went Wrong. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void deleteAccount(){
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.popup_delete_account, null);

        Button delAcntBtn=(Button) mView.findViewById(R.id.delpopyesbtn);
        mBuilder.setView(mView);
        final android.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

        delAcntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    //method to dactive/delete account
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), "Couldn't Delete Account.. Please Try Again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout ,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item1){
        int id=item1.getItemId();

        if (id== R.id.exit){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        return true;
    }


}


