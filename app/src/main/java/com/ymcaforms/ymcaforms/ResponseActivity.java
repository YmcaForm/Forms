package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResponseActivity extends AppCompatActivity {
    // private ListView responses;
    private RecyclerView response;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mformreference;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        mToolbar = (Toolbar) findViewById(R.id.response_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Responses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        String current_userid="";

        if(mAuth.getCurrentUser()!=null)
         current_userid=mAuth.getCurrentUser().getUid();

        final String form_id = getIntent().getStringExtra("form_id");

        response = (RecyclerView) findViewById(R.id.response_recyler_view);
        response.setHasFixedSize(true);
        response.setLayoutManager(new LinearLayoutManager(this));

        mformreference = FirebaseDatabase.getInstance().getReference().child("Responses").child(form_id);

        final int[] i = {1};

        FirebaseRecyclerAdapter<Forms, FormsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Forms, FormsViewHolder>(
                Forms.class,
                R.layout.single_response_layout,
                FormsViewHolder.class,
                mformreference
        ) {
            @Override
            protected void populateViewHolder(final FormsViewHolder viewHolder, Forms model, final int position) {
                final String list_form_id = getRef(position).getKey();

                String str = "Response " + String.valueOf(i[0]);
                i[0]++;


                System.out.println("yoyo");
                viewHolder.setDescription(str);

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mformreference.child(list_form_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                    String id = snapshot.getKey();
                                    System.out.println(form_id + " " + list_form_id+" " + id);

                                    Intent displayResponse = new Intent(ResponseActivity.this, DisplayResponse.class);
                                    displayResponse.putExtra("form_id",form_id);
                                    displayResponse.putExtra("res_id", list_form_id);
                                    startActivity(displayResponse);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                                /*Intent i = new Intent(getApplicationContext(), ResponseActivity.class);
                                i.putExtra("form_id", list_form_id);
                                startActivity(i);

*/
                        Toast.makeText(ResponseActivity.this, "Clicked"+ position, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        };

        response.setAdapter(firebaseRecyclerAdapter);
    }

    final public static class FormsViewHolder extends RecyclerView.ViewHolder {

        View mview;
        public FormsViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setDescription(String description) {

            TextView tv_description=mview.findViewById(R.id.response_textView);
            tv_description.setText(description);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

      /*  mformreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String str = "Response " + String.valueOf(i[0]);
                    data.add(str);
                    i[0]++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResponseActivity.this, android.R.layout.simple_list_item_activated_1, data);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }
}



