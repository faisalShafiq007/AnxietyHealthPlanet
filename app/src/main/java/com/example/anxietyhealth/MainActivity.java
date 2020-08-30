package com.example.anxietyhealth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.anxietyhealth.Messagesdirectory.Chatactivity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.anxietyhealth.Messagesdirectory.allusershow;
import com.example.anxietyhealth.admin.adminmain;
import com.example.anxietyhealth.patientpayment.patientpayment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
CircleImageView cimagview;
TextView name,type;
FirebaseAuth mAuth;
ImageView payment,chat;
String cuid,img,nme,typ;
DatabaseReference userref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    String email,password;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        payment=findViewById(R.id.paymentimagview);
        chat=findViewById(R.id.chatimgview);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name=headerView.findViewById(R.id.headername);
        type=headerView.findViewById(R.id.headertype);
        cimagview=headerView.findViewById(R.id.headerimgview);
        mAuth=FirebaseAuth.getInstance();
        cuid=mAuth.getCurrentUser().getUid();
        getSupportActionBar().setTitle(R.string.app_name);

        userref= FirebaseDatabase.getInstance().getReference().child("users").child(cuid);

        final ProgressDialog progress = new ProgressDialog(this);
        final Timer t = new Timer();
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        t.schedule(new TimerTask() {
            public void run() {

                progress.dismiss();

                // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
       userref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                  img=dataSnapshot.child("profileimage").getValue().toString();
                   nme=dataSnapshot.child("name").getValue().toString();
                   typ=dataSnapshot.child("type").getValue().toString();
                   Picasso.get().load(img).into(cimagview);
                   name.setText("name: "+nme);
                   type.setText("Type: "+typ);

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
payment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(typ.equals("Patient")){
            Intent a=new Intent(MainActivity.this, patientpayment.class);
            startActivity(a);
        }
        else{
            Intent a=new Intent(MainActivity.this, adminmain.class);
            startActivity(a);
        }

}
});
chat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
Intent c=new Intent(MainActivity.this, allusershow.class);
startActivity(c);
    }
});
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }

         else if (id == R.id.nav_tools) {
            Intent na=new Intent(MainActivity.this,Profileshow.class);

            startActivity(na);

        } else if (id == R.id.logout) {
mAuth.signOut();
            sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            editor=sharedPreferences.edit();
            email = sharedPreferences.getString("email", "");
            password = sharedPreferences.getString("pass", "");
            editor.remove("email");
            editor.remove("pass");
            editor.commit();
            editor.clear();
            Intent na=new Intent(MainActivity.this,Platform.class);
            na.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(na);


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
