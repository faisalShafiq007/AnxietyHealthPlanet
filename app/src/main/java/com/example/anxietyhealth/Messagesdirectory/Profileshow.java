package com.example.anxietyhealth.Messagesdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anxietyhealth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profileshow extends AppCompatActivity {
String visituserid;
    TextView username,hname,address,city,experience,lne,wd;
    CircleImageView imageview;
    StorageReference userprofileimageRef ;
    String Currentuserid;
    FirebaseAuth mAuth;
    int Channelid=123;
    DatabaseReference doctorref;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileshow2);
        Intent i=getIntent();
        visituserid=i.getStringExtra("id");
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        address=findViewById(R.id.addressname);
        city=findViewById(R.id.cityname);
        experience=findViewById(R.id.experiencename);
        String Currrentuserid=mAuth.getCurrentUser().getUid();
        doctorref= FirebaseDatabase.getInstance().getReference("users").child(visituserid);
        username=findViewById(R.id.username);
        lne=findViewById(R.id.line);
        wd=findViewById(R.id.workdetails);
        hname=findViewById(R.id.hospitalname);
        imageview=findViewById(R.id.my_profile_pic);
        mAuth=FirebaseAuth.getInstance();
        Currentuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctorref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String image=dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.patient).into(imageview);

                }
                if (dataSnapshot.hasChild("name")) {
                    String fullname = dataSnapshot.child("name").getValue().toString();
                    getSupportActionBar().setTitle(fullname+" Profile");
                    username.setText("Name: "+ fullname);
                }
                if (dataSnapshot.hasChild("type")) {
                    String hospitalname = dataSnapshot.child("type").getValue().toString();
                    hname.setText("Type: "+hospitalname);
                }
                if (dataSnapshot.hasChild("adress")) {
                    String hospitaname = dataSnapshot.child("adress").getValue().toString();
                    address.setText("adress: "+hospitaname);
                }
                if (dataSnapshot.hasChild("city")) {
                    String hospitalname = dataSnapshot.child("city").getValue().toString();
                    city.setText("city: "+hospitalname);
                }
                if (dataSnapshot.hasChild("experience")) {
                    String hospitalname = dataSnapshot.child("experience").getValue().toString();
                    experience.setText(hospitalname);
                }
                else{
                    hname.setVisibility(View.GONE);
                    wd.setVisibility(View.GONE);
                    lne.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    experience.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }

