package com.example.anxietyhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Platform extends AppCompatActivity {
ImageView patient,doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform);
        patient=findViewById(R.id.pateintimageview);
        doctor=findViewById(R.id.doctorimagview);
        getSupportActionBar().hide();
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentcall("Patient");
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentcall("Doctor");
            }
        });
    }

    private void intentcall(String a) {
        Intent type=new Intent(Platform.this,LoginActivity.class);
        type.putExtra("type",a);
        startActivity(type);

    }
}
