package com.example.anxietyhealth.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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



public class adminmain extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    DatabaseReference doctorref;
    String visituserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmain);
        doctorref= FirebaseDatabase.getInstance().getReference().child("post");
        reccyclerView=findViewById(R.id.recyclerView);


        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(adminmain.this));
        fetch();
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("post");
        FirebaseRecyclerOptions<adminmodel> options =
                new FirebaseRecyclerOptions.Builder<adminmodel>()
                        .setQuery(query, new SnapshotParser<adminmodel>() {
                            @NonNull
                            @Override
                            public adminmodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new adminmodel(snapshot.child("amount").getValue().toString(),
                                        snapshot.child("name").getValue().toString(),
                                        snapshot.child("transactionid").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<adminmodel,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull adminmodel adminmodel) {


                holder.setTxtname(adminmodel.getName());
                holder.setTxtamount(adminmodel.getAmount());
                holder.setTxtid(adminmodel.getTransactionid());
                visituserid=getRef(position).getKey();


                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doctorref.child(visituserid).removeValue();
                        Intent n=new Intent(adminmain.this,adminmain.class);
                        startActivity(n);

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.postlist, parent, false);

                return new ViewHolder(view);
            }




        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtname;
        public TextView txtamount;
        public TextView txtid;
        public ImageButton btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           btn=itemView.findViewById(R.id.postlistbutton);
            txtname=itemView.findViewById(R.id.postlistname);
            txtamount=itemView.findViewById(R.id.postlistamount);
            txtid=itemView.findViewById(R.id.postlistid);


        }

        public void setTxtname(String stringa) {
            txtname.setText(stringa);
        }
        public void setTxtamount(String stringb) {
            txtamount.setText(stringb);
        }
        public void setTxtid(String stringc) {
            txtid.setText(stringc);
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
