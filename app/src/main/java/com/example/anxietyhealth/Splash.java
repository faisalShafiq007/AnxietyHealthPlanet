package com.example.anxietyhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
SharedPreferences sharedPreferences;
FirebaseAuth mAuth;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
       sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
       email = sharedPreferences.getString("email", "");
        password = sharedPreferences.getString("pass", "");
        Log.e("emailishere"+email,password);
if(email.isEmpty()){
    Intent n=new Intent(Splash.this,Platform.class);
   n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(n);
}
else{
    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Intent n=new Intent(Splash.this,MainActivity.class);
            n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(n);
        }
    });

}
            } }, 1000);
    }
}
