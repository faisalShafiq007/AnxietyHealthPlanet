package com.example.anxietyhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    EditText rgemail,rgpassword;
    CheckBox rgaggrementchkbox;
    Button registerbtn;
    TextView loginActivity;
    FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rgemail=findViewById(R.id.registeremail);
        rgpassword=findViewById(R.id.registerpassword);
        mAuth=FirebaseAuth.getInstance();
        registerbtn=findViewById(R.id.registerbutton);
        loginActivity=findViewById(R.id.registerlogin);
        Intent intent = getIntent();
        type= intent.getStringExtra("type");
Log.e("type",type);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
getSupportActionBar().hide();
loginActivity.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent loginintent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginintent);

    }
});
        mAuth=FirebaseAuth.getInstance();
       registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(rgemail.getText().toString().isEmpty()){
                    rgemail.setError("Please enter Email");
                }

                else if(!rgemail.getText().toString().contains("@")){
                    rgemail.setError("Please enter Proper Email e.g xxx@x.com");
                }

                else if(rgpassword.getText().toString().isEmpty()){
                    rgpassword.setError("Please Enter Password");
                }

                else if(rgpassword.getText().toString().length()<9){
                    rgpassword.setError("Please Enter Password with length 9");
                }


                else {
                    final String emailtext=rgemail.getText().toString();
                    final String passwordtext=rgpassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(emailtext,passwordtext).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                       Toasttext("Signup successfull");
                            editor = sharedpreferences.edit();
                            editor.putString("email", emailtext);
                            editor.putString("pass", passwordtext);
                            editor.commit();
                       Intent profileintent=new Intent(RegisterActivity.this,ProfileActivity.class);
                            profileintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       profileintent.putExtra("email",emailtext);
                       profileintent.putExtra("type",type);

                       startActivity(profileintent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasttext("Signup failed");
                        }
                    });


                }
            }
        });

    }
    private void Toasttext(String text) {
        Toast.makeText(RegisterActivity.this,text,Toast.LENGTH_SHORT).show();
    }

   }