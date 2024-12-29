package com.sarwar.app.donate;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AdminloginActivity extends AppCompatActivity {

    EditText CAdminpassET, AdminpassET;
    Button AdminLoginBtn;
    RelativeLayout AdminLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdminpassET=(EditText)findViewById(R.id.unadloginET);
        CAdminpassET=(EditText)findViewById(R.id.adloginpassET);
        AdminLoginBtn=(Button)findViewById(R.id.adloginbtn);
        AdminLayer=(RelativeLayout)findViewById(R.id.adminloglayer);


        AdminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminpass=AdminpassET.getText().toString();
                String cadminpass=CAdminpassET.getText().toString();


                String apass="pass123";

                if (TextUtils.isEmpty(adminpass)){
                    Snackbar.make(AdminLayer,"Write Your Admin Password.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                if (TextUtils.isEmpty(cadminpass)){
                    Snackbar.make(AdminLayer,"Confirm Your Admin Password.",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                    return; }

                if (adminpass.equals(apass)&& cadminpass.equals(apass)){
                    Intent intent =new Intent(AdminloginActivity.this,PostviewAdminActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(AdminLayer,"Wrong Username or Password",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show(); }

                }});
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
