package com.example.anxietyhealth.Messagesdirectory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.anxietyhealth.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class allusershow extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    DatabaseReference doctorref;
    Spinner spinner1;
    String searchtext;
    Query Doctorquery=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusershow);
        spinner1=findViewById(R.id.spinner_1);
        ArrayAdapter<CharSequence> arrayAdapter= ArrayAdapter.createFromResource(this,R.array.curency,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener( this);


        doctorref= FirebaseDatabase.getInstance().getReference().child("users");
        reccyclerView=findViewById(R.id.alluserrecyclerView);
        
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(allusershow.this));
        reccyclerView.setNestedScrollingEnabled(false);
        fetch();
    }
    private void Searchuser(String searchBoxInput) {
        Doctorquery= doctorref.orderByChild("type").startAt(searchBoxInput)
                .endAt(searchBoxInput.concat("\uf8ff"));
        FirebaseRecyclerOptions<allusermodel> options =
                new FirebaseRecyclerOptions.Builder<allusermodel>()
                        .setQuery(Doctorquery, new SnapshotParser<allusermodel>() {
                            @NonNull
                            @Override
                            public allusermodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new allusermodel(snapshot.child("name").getValue().toString(),
                                        snapshot.child("type").getValue().toString(),
                                        snapshot.child("profileimage").getValue().toString());
                            }
                        })
                        .build();

        adapter=new FirebaseRecyclerAdapter<allusermodel,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull allusermodel allusermodel) {

                holder.setTxtinstitue(allusermodel.getInstitute());
                holder.setTxtusername(allusermodel.getName());
                holder.setTxtmage(allusermodel.getProfileimage());
                final String visituserid =getRef(position).getKey();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
Intent i=new Intent(allusershow.this,Chatactivity.class);
i.putExtra("id",visituserid);
startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.alluserlist, parent, false);

                return new ViewHolder(view);
            }




        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("users");
        FirebaseRecyclerOptions<allusermodel> options =
                new FirebaseRecyclerOptions.Builder<allusermodel>()
                        .setQuery(query, new SnapshotParser<allusermodel>() {
                            @NonNull
                            @Override
                            public allusermodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new allusermodel(snapshot.child("name").getValue().toString(),
                                        snapshot.child("type").getValue().toString(),
                                        snapshot.child("profileimage").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<allusermodel, ViewHolder>(options) {




            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull allusermodel allusermodel) {

                holder.setTxtinstitue(allusermodel.getInstitute());
                holder.setTxtusername(allusermodel.getName());
                holder.setTxtmage(allusermodel.getProfileimage());
                final String visituserid =getRef(position).getKey();
holder.root.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(allusershow.this,Chatactivity.class);
        i.putExtra("id",visituserid);
        startActivity(i);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent pr=new Intent(allusershow.this,Profileshow.class);
pr.putExtra("id",visituserid);
startActivity(pr);
            }
        });

    }
});
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.alluserlist, parent, false);

                return new ViewHolder(view);
            }




        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text=adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();
        if(text.equals("Add Filter")){
fetch();
        }
        else{Searchuser(text);}
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public TextView txtusername;
        public TextView txtinstitute;
        public CircleImageView cv;
        public ImageButton btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.liist_root);
            txtinstitute=itemView.findViewById(R.id.alluseuniname);
            txtusername=itemView.findViewById(R.id.allusername);
            cv=itemView.findViewById(R.id.allusercircleImageView);
            btn=itemView.findViewById(R.id.viewprofile);

        }

        public void setTxtinstitue(String stringb) {
            txtinstitute.setText(stringb);
        }
        public void setTxtusername(String stringc) {
            txtusername.setText(stringc);
        }
        public void setTxtmage(String stringd) {
            Picasso.get().load(stringd).into(cv);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
