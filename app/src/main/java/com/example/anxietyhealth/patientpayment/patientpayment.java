package com.example.anxietyhealth.patientpayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anxietyhealth.MainActivity;
import com.example.anxietyhealth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class patientpayment extends AppCompatActivity {
    EditText name, txID, amount;
    DatabaseReference postref;
    FirebaseAuth mAuth;
    String Currentuserid;
    Button done;
    String savecurrentdate,savecurrenttime,postrandomname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientpayment);
        getSupportActionBar().setTitle("Payment");
        done=findViewById(R.id.paymentDone);
        name=findViewById(R.id.paymentName);
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        savecurrentdate=currentdate.format(calfordate.getTime());

        Calendar calfortime=Calendar.getInstance();
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm");
        savecurrenttime=currenttime.format(calfortime.getTime());
        postrandomname=savecurrentdate.concat(savecurrenttime);
        txID=findViewById(R.id.paymentTID);
        amount=findViewById(R.id.paymentAmount);
        mAuth=FirebaseAuth.getInstance();
        Currentuserid=mAuth.getCurrentUser().getUid();
        postref= FirebaseDatabase.getInstance().getReference().child("post").child(Currentuserid+postrandomname);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap postmap=new HashMap();
                postmap.put("name",name.getText().toString());
                postmap.put("transactionid",txID.getText().toString());
                postmap.put("amount",amount.getText().toString());
                postref.updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(patientpayment.this,"Payment Completed",Toast.LENGTH_SHORT).show();
                        Intent p=new Intent(patientpayment.this, MainActivity.class);
                        startActivity(p);
                        patientpayment.this.finish();
                    }
                });
            }
        });

//        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Payment");
    }
}
