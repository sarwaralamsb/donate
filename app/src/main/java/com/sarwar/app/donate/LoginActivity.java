package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    Button UserLogin;
    TextView Register;
    EditText UsernameET, UserPasswordET;
    CheckBox CB;
    RelativeLayout LoginLayer;

    //shared preference to remember user
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String My_Shared="UserDetails";
    public static final String UNAME="UserName";
    public static final String UPASSWORD="UserPassword";


    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserLogin=(Button)findViewById(R.id.userloginbtn);
        Register=(TextView)findViewById(R.id.regtxtbtn);
        CB=(CheckBox)findViewById(R.id.remember);
        UsernameET=(EditText)findViewById(R.id.unloginET);
        UserPasswordET=(EditText)findViewById(R.id.loginpassET);
        LoginLayer=(RelativeLayout)findViewById(R.id.loginLayer);

        //code to remember user
        sharedPreferences=getSharedPreferences(My_Shared, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if (sharedPreferences.contains(UNAME)){
            String Rname=sharedPreferences.getString(UNAME,"");
            UsernameET.setText(Rname); }
        if (sharedPreferences.contains(UPASSWORD)) {
            String Rpass = sharedPreferences.getString(UPASSWORD, "");
            UserPasswordET.setText(Rpass); }

            mAuth = FirebaseAuth.getInstance();
            progressDialog = new ProgressDialog(this);

        UserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loginuser() {
    String username = UsernameET.getText().toString();
    String password = UserPasswordET.getText().toString();

    //if checkbox is checked then it will store user info else nothing
    if (CB.isChecked()) {
        editor.putString(UNAME, username);
        editor.putString(UPASSWORD, password);
        editor.commit();
    } else { }

    //EditText Box Checking
    if (TextUtils.isEmpty(username)) {
        Snackbar.make(LoginLayer, "Write Your Username.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return; }
    if (TextUtils.isEmpty(password)) {
        Snackbar.make(LoginLayer, "Write Your Password", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return; }

        progressDialog.setTitle("Logging in");
        //progressDialog.setMessage("Please wait for a while.....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login Successfull, Welcome to home ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Snackbar.make(LoginLayer, "Wrong username or password. Try again...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); }
        }
    });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id== R.id.adminm){
            Intent intent =new Intent(LoginActivity.this,AdminloginActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
