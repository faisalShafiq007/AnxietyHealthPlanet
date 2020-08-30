package com.example.anxietyhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileActivity extends AppCompatActivity {
ImageView pimage;
EditText name,padres,pcty,experience;

Button profilebtn;
FirebaseAuth mAuth;
String namestring,pasttring,citystring,addressstring;
    String image;
    String type;
    int GALLERY_PICK=1234;
    StorageReference userprofileimageRef;
    DatabaseReference userRef ;
    String Current_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        type= intent.getStringExtra("type");
        Log.e("type",type);
        if(type.equals("Patient")){

        }
        Intent i=getIntent();
final String emailregister=i.getStringExtra("email");
        pimage=findViewById(R.id.imageregister);
        name=findViewById(R.id.registername);
        padres=findViewById(R.id.registeraddress);
pcty=findViewById(R.id.registercity);
experience=findViewById(R.id.registerpastexperience);
        profilebtn=findViewById(R.id.createprofilebutton);
        if(type.equals("Patient")){
experience.setVisibility(View.INVISIBLE);

        }
        mAuth= FirebaseAuth.getInstance();
        userprofileimageRef = FirebaseStorage.getInstance().getReference().child("profileimage");
        Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(Current_user_id);

        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_PICK);

            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namestring=name.getText().toString();
                citystring=pcty.getText().toString();
                addressstring=padres.getText().toString();
                pasttring=experience.getText().toString();
                if(pimage.getDrawable()==null){
                    Toasttext("Please enter image");
                }

                else if(namestring.isEmpty()){
                    name.setError("please enter username");
                }
                else if(citystring.isEmpty()){
                    pcty.setError("please enter username");
                }
                else if(addressstring.isEmpty()){
                    padres.setError("please enter username");
                }



            else if(image==null){
    Toasttext("Please enter Profile Image");
                }
                else {
                    HashMap usermap=new HashMap();
                    usermap.put("name",namestring);
usermap.put("type",type);
                    usermap.put("city",citystring);
                    usermap.put("adress",addressstring);
                    usermap.put("experience",pasttring);
                    userRef.updateChildren(usermap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                       Toasttext("data entered successfully");
                            Intent n=new Intent(ProfileActivity.this,MainActivity.class);
                            n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(n);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasttext("data entered failed");
                        }
                    });
                }
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profileimage")){
                    image=dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.addimage).into(pimage);
                    }
                    else{
                        Toasttext("Enter image please");
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1234 ){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

            Uri ImageUri=data.getData();
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                final ProgressDialog progress = new ProgressDialog(ProfileActivity.this);
                final Timer t = new Timer();
                progress.setTitle("Loading");
                progress.setMessage("Wait while uploading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                t.schedule(new TimerTask() {
                    public void run() {
                        progress.dismiss();
                        // when the task active then close the dialog
                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                    }
                }, 6000);
                Uri resulturi=result.getUri();
                final StorageReference filepath=userprofileimageRef.child(Current_user_id +".jpg");
                filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            /*final String downloadurl= task.getResult().getStorage().getDownloadUrl().toString();*/
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadurl=uri.toString();
                                    userRef.child("profileimage").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                  /*Intent setupintent=new Intent(setup.this,setup.class);
                                  startActivity(setupintent);*/
                                            Toast.makeText(ProfileActivity.this,"image stored",Toast.LENGTH_LONG).show();


                                        }
                                    });
                                }
                            });

                        }
                    }


                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void Toasttext(String text){
        Toast.makeText(ProfileActivity.this,text,Toast.LENGTH_SHORT).show();
    }
}
