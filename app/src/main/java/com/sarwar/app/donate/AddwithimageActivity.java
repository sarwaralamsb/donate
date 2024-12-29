package com.sarwar.app.donate;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddwithimageActivity extends AppCompatActivity {

    EditText DetailsetI, PhoneetI, AddreesetI;
    TextView unTv, DateTv,Status, ChangeImage;
    ImageView displaydamoimage;
    Button UploadI;
    RelativeLayout AddPostImageL;

    private Uri imgUri;
    public static final int REQUEST_CODE = 1234;
    public static final String FB_STORAGE_PATH = "DonationPostPhotos/";

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    Date currentTime;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwithimage);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        currentTime= Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy  hh:mm aa");
        formattedDate = df.format(currentTime);

        String getname=getIntent().getStringExtra("key_24");
        String getdetails=getIntent().getStringExtra("key_21");
        String getphone=getIntent().getStringExtra("key_22");
        String getaddress=getIntent().getStringExtra("key_23");

        DetailsetI=(EditText)findViewById(R.id.postdetailseti);
        PhoneetI=(EditText)findViewById(R.id.postphoneeti);
        AddreesetI=(EditText)findViewById(R.id.postaddresseti);
        unTv=(TextView)findViewById(R.id.unviewtvi);
        DateTv =(TextView)findViewById(R.id.datetvi);
        displaydamoimage=(ImageView)findViewById(R.id.imagedi);
        UploadI=(Button)findViewById(R.id.btnupposti);
        Status=(TextView)findViewById(R.id.notcollectedi);
        ChangeImage=(TextView)findViewById(R.id.changeimagetxtbtn);
        AddPostImageL=(RelativeLayout)findViewById(R.id.addimageLayer) ;

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);

        DetailsetI.setText(getdetails);
        PhoneetI.setText(getphone);
        AddreesetI.setText(getaddress);
        unTv.setText(getname);
        DateTv.setText(formattedDate);

        ChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                displaydamoimage.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } } }
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void uploadPostI (View v){
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            final DatabaseReference dr=database.getReference().child(user.getUid());
            final DatabaseReference reference=database.getReference("Donation Post").push();
            String Key=reference.getKey();
            String Uid=dr.getKey();

            final String name=unTv.getText().toString();
            final String details=  DetailsetI.getText().toString();
            final String phone= PhoneetI.getText().toString();
            final String address=AddreesetI.getText().toString();
            final String status=Status.getText().toString();
            final String current_time= DateTv.getText().toString();
            final String key=Key;
            final String uid=Uid;

            if (TextUtils.isEmpty(details)){
            Snackbar.make(AddPostImageL,"Write Details Information.",Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
            return; }
            if (TextUtils.isEmpty(phone)){
            Snackbar.make(AddPostImageL,"Write Your Phone Number.",Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
            return; }
            if (TextUtils.isEmpty(address)){
            Snackbar.make(AddPostImageL,"Write The Address to Collect Your Item.",Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
            return; }

            final ProgressDialog dialog = new ProgressDialog(AddwithimageActivity.this);
            dialog.setTitle("Uploading Your Post");
            dialog.show();

            StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DonationPost donatePost = new DonationPost(
                            name,
                            taskSnapshot.getDownloadUrl().toString(),
                            details,
                            phone,
                            address,
                            status,
                            uid,
                            key,
                            current_time
                    );
                    reference.setValue(donatePost);

                    dialog.dismiss();
                    Toast.makeText(AddwithimageActivity.this, "Your post is uploaded successfully", Toast.LENGTH_SHORT).show();

                    DetailsetI.setText(null);
                    PhoneetI.setText(null);
                    AddreesetI.setText(null);
                    displaydamoimage.setImageBitmap(null);
                    Snackbar.make(AddPostImageL,"Your post is uploaded successfully",Snackbar.LENGTH_LONG)
                            .setAction("Action",null).show();
                }
                })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Dimiss dialog when error
                            dialog.dismiss();
                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show(); }})
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //Show upload progress
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }});
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home){
            this.finish();
            return true;
        }return super.onOptionsItemSelected(item);
    }
}
