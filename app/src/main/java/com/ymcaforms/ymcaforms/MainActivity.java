package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton addForm;
    private DatabaseReference mformreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        addForm = (FloatingActionButton) findViewById(R.id.fab);

        String user_id="";
        if(mAuth.getCurrentUser()!=null)
            user_id = mAuth.getCurrentUser().getUid();

        mformreference= FirebaseDatabase.getInstance().getReference().child("Forms").child(user_id);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("YMCA Forms");

        recyclerView=(RecyclerView)findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent formIntent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(formIntent);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sign_out){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // this is when user press back on mainactivity page it doesn't come to startactivity page.. i.e its clears and make a new task
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(id == R.id.contact){
            Toast.makeText(MainActivity.this, "Contact Us", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            sendToLogin();

        FirebaseRecyclerAdapter<Forms,FormsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Forms, FormsViewHolder>(
                Forms.class,
                R.layout.single_form_layout,
                FormsViewHolder.class,
                mformreference
        ) {
            @Override
            protected void populateViewHolder(final FormsViewHolder viewHolder, Forms model, int position) {

                final String list_form_id=getRef(position).getKey();
                final String list_user_id=mAuth.getCurrentUser().getUid();

                mformreference.child(list_form_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        String name=dataSnapshot.child("form_name").getValue().toString();
                        String description=dataSnapshot.child("form_description").getValue().toString();

                        DatabaseReference imagerefernece=FirebaseDatabase.getInstance().getReference().child("Images").child(list_user_id);
                        imagerefernece.child(list_form_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child("image").getValue()!=null) {

                                    String image = dataSnapshot.child("image").getValue().toString();
                                    System.out.println(image);
                                    viewHolder.setimage(image);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.setName(name);
                        viewHolder.setDescription(description);

                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent i=new Intent(getApplicationContext(),ResponseActivity.class);
                                i.putExtra("form_id",list_form_id);
                                i.putExtra("user_id",list_user_id);
                                startActivity(i);


                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public void sendToLogin(){
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    final public static class FormsViewHolder extends RecyclerView.ViewHolder {

        View mview;
        public FormsViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setDescription(String description) {

            TextView tv_description=mview.findViewById(R.id.previous_form_description);
            tv_description.setText(description);
        }

        public void setName(String name) {

            TextView tv_name=mview.findViewById(R.id.previous_form_name);
            tv_name.setText(name);
        }

        public void setimage(String image) {

            ImageView iv=mview.findViewById(R.id.previous_form_image);
            System.out.print("hlo");
            Picasso.with(mview.getContext()).load(image).into(iv);
        }
    }
}