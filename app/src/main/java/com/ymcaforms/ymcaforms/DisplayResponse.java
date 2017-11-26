package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayResponse extends AppCompatActivity {
    private RecyclerView response;
    private DatabaseReference mformreference;
    String form_id, res_id;
    private FirebaseAuth mAuth;
    static LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_response);

        response = (RecyclerView) findViewById(R.id.recycler_display_response);
        response.setHasFixedSize(true);
        response.setLayoutManager(new LinearLayoutManager(this));



        mAuth = FirebaseAuth.getInstance();

        form_id = getIntent().getStringExtra("form_id");
        res_id = getIntent().getStringExtra("res_id");

        mformreference = FirebaseDatabase.getInstance().getReference().child("Responses").child(form_id).child(res_id);

        FirebaseRecyclerAdapter<Forms,FormsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Forms, FormsViewHolder>(
                Forms.class,
                R.layout.form_response_layout,
                FormsViewHolder.class,
                mformreference
        ) {
            @Override
            protected void populateViewHolder(final FormsViewHolder viewHolder, Forms model, int position) {
                final String list_form_id=getRef(position).getKey();

                mformreference.child(list_form_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String question="",answer="",type="";

                        if(dataSnapshot.child("Question").getValue()!=null)
                        question =  dataSnapshot.child("Question").getValue().toString();

                        if(dataSnapshot.child("Answer").getValue()!=null)
                        answer = dataSnapshot.child("Answer").getValue().toString();

                        if(dataSnapshot.child("Type").getValue()!=null)
                        type = dataSnapshot.child("Type").getValue().toString();


                        System.out.println(question + " " + answer);
                        int l = answer.length();
                        String s = "";
                        viewHolder.setQuestion(question);
                        if(type.equals("Check Box")){
                            for (int j=0 ; j<l ; ++j){
                                if(answer.charAt(j)=='$' && answer.charAt(j+1)=='$' && answer.charAt(j+2)=='$')
                                {
                                    viewHolder.setAnswer(s, type);
                                    j=j+2;
                                    s="";
                                }
                                else
                                    s=s+answer.charAt(j);
                            }
                        }
                        else
                            viewHolder.setAnswer(answer, type);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


        };

        response.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FormsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public FormsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setQuestion(String ques){
            TextView q = (TextView) mView.findViewById(R.id.question);
            q.setText(ques);
        }

        public void setAnswer(String ans, String type){

            if(type.equals("Check Box")) {
                linearLayout = (LinearLayout) mView.findViewById(R.id.create_linear);
                TextView textView = new TextView(mView.getContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(ans);
                textView.setPadding(18,0,18,0);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
                linearLayout.addView(textView);
            }
            else {
                TextView a = (TextView) mView.findViewById(R.id.responses);
                a.setText(ans);
            }
        }
    }

}
