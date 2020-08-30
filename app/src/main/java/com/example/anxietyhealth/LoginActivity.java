package com.example.anxietyhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
EditText etemail,etpassword;

Button Loginbtn;
TextView registerActivity;
FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    String type,databasetype;
    String cuid;
    DatabaseReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etemail=findViewById(R.id.loginemail);
        etpassword=findViewById(R.id.loginpassword);

        Loginbtn=findViewById(R.id.loginbutton);
        registerActivity=findViewById(R.id.loginregister);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        Log.e("type",type);
        registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registr=new Intent(LoginActivity.this,RegisterActivity.class);
               registr.putExtra("type",type);
                startActivity(registr);
            }
        });
mAuth= FirebaseAuth.getInstance();

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etemail.getText().toString().isEmpty()){
                    etemail.setError("Please enter Email");
                }

               else if(!etemail.getText().toString().contains("@")){
                    etemail.setError("Please enter Proper Email e.g xxx@x.com");
                }

               else if(etpassword.getText().toString().isEmpty()){
                   etpassword.setError("Please Enter Password");
                }

                else if(etpassword.getText().toString().length()<9){
                    etpassword.setError("Please Enter Password with length 9");
                }
                else {
                    final String emailtext=etemail.getText().toString();
                    final String passwordtext=etpassword.getText().toString();

                    mAuth.signInWithEmailAndPassword(emailtext,passwordtext).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            editor = sharedpreferences.edit();
                            editor.putString("email", emailtext);
                            editor.putString("pass", passwordtext);
                            editor.commit();
                            cuid=mAuth.getCurrentUser().getUid();
                            userref= FirebaseDatabase.getInstance().getReference().child("users").child(cuid);
                            final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
                            final Timer t = new Timer();
                            progress.setTitle("Loading");
                            progress.setMessage("Wait while authenticating...");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();
                            t.schedule(new TimerTask() {
                                public void run() {
                                    progress.dismiss();

                                    // when the task active then close the dialog
                                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                                }
                            }, 2000); // after 2 second (or 2000 miliseconds), the task wi
                            userref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        databasetype=dataSnapshot.child("type").getValue().toString();

                                        if(databasetype.equals(type)){
                                            Intent n=new Intent(LoginActivity.this,MainActivity.class);
                                            n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(n);
                                        }
                                        else{ Toasttext("You are not a "+type);
                                            mAuth.signOut();
                                            sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                            editor=sharedpreferences.edit();
                                            editor.remove("email");
                                            editor.remove("pass");
                                            editor.commit();
                                            editor.clear();
                                            Intent na=new Intent(LoginActivity.this,Platform.class);
                                            na.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(na);}
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                       Toasttext("Failed to Login");
                        }
                    });

                }
            }
        });
    }

    private void Toasttext(String text) {
    Toast.makeText(LoginActivity.this,text,Toast.LENGTH_SHORT).show();
    }
}
