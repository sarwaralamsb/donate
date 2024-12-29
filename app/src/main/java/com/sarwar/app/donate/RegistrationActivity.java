package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText userRegET,passRegET,emailRegET,phoneRegET,confpassRegET, AddrresET;
    Button RegisterBtn;
    RelativeLayout RegisterLayer;


    private ProgressDialog progressDialog;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mauth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        userRegET=(EditText)findViewById(R.id.unsignET);
        passRegET=(EditText)findViewById(R.id.passwordsignET);
        confpassRegET=(EditText)findViewById(R.id.cpasswordsignET);
        emailRegET=(EditText)findViewById(R.id.emailsignET);
        phoneRegET=(EditText)findViewById(R.id.phonesignET);
        AddrresET=(EditText)findViewById(R.id.address_signET);
        RegisterBtn=(Button)findViewById(R.id.registerbtn);
        RegisterLayer=(RelativeLayout)findViewById(R.id.regLayer) ;

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Reguname=userRegET.getText().toString();
                final String Regpassword=passRegET.getText().toString();
                String Regconfpassword=confpassRegET.getText().toString();
                final String Regemail=emailRegET.getText().toString();
                final String Regphone=phoneRegET.getText().toString();
                final String RegAddress=AddrresET.getText().toString();

                if (TextUtils.isEmpty(Reguname)){
                    Snackbar.make(RegisterLayer,"Write Your Username.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }
                if (TextUtils.isEmpty(Regpassword)){
                    Snackbar.make(RegisterLayer,"Write Your Password",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }
                if (TextUtils.isEmpty(Regconfpassword)){
                    Snackbar.make(RegisterLayer,"Write Your Password Again to Confirm",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }
                if (TextUtils.isEmpty(Regemail)){
                    Snackbar.make(RegisterLayer,"Write Your Email",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                if (TextUtils.isEmpty(Regphone)){
                    Snackbar.make(RegisterLayer,"Write Your Phone Number",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                if (TextUtils.isEmpty(RegAddress)){
                    Snackbar.make(RegisterLayer,"Write Your Address",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                 if(!Regpassword.equals(Regconfpassword)){
                     Snackbar.make(RegisterLayer,"Password Doesn't Match. Try again",Snackbar.LENGTH_LONG)
                             .setAction("Action",null).show();
                     passRegET.setText(null);
                     confpassRegET.setText(null);}

                progressDialog.setTitle("Registering User");
                progressDialog.show();



                mauth.createUserWithEmailAndPassword(Regemail, Regpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    User user = new User(
                                            Reguname,
                                            Regemail,
                                            Regphone,
                                            RegAddress
                                    );
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                //display a failure message
                                            }
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Snackbar.make(RegisterLayer,task.getException().getMessage(),Snackbar.LENGTH_LONG)
                                            .setAction("Action",null).show();
                                }
                            }
                        });
            }
        });

        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if (id == android.R.id.home){
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
            }
        return true; }
}
