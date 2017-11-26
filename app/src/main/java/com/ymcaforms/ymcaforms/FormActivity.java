package com.ymcaforms.ymcaforms;

import java.util.HashMap;
import java.util.List;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormActivity extends AppCompatActivity {
    Button mAddButton;
    Button submit;
    EditText formname,formdescription;
    public RecyclerView recyclerView;
    DatabaseReference formreference;
    DatabaseReference questionreference;
    private FirebaseAuth mAuth;
    int flag=0;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        DatabaseHandler db = new DatabaseHandler(this);
        db.delete(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        submit=(Button)findViewById(R.id.submit);
        formname=(EditText)findViewById(R.id.form_name);
        formdescription=(EditText)findViewById(R.id.form_description);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag=0;
                String formnm=formname.getText().toString();
                String formds=formdescription.getText().toString();

                if(formnm==null || formnm.equals(""))
                    formnm="Unititled Form";

                if(formds==null || formds.equals(""))
                    formds="No Description";


                String userID="";
                if(mAuth.getCurrentUser()!=null)
                userID = mAuth.getCurrentUser().getUid();


                formreference= FirebaseDatabase.getInstance().getReference().child("Forms").child(userID);
                final String formid=formreference.push().getKey();

                System.out.println(formid);

                HashMap<String,String> usermap=new HashMap<>();
                usermap.put("form_name",formnm);
                usermap.put("form_description",formds);

                formreference.child(formid).setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(!task.isSuccessful())
                        {
                            flag=1;
                            Toast.makeText(getApplicationContext(),"Invalid Form",Toast.LENGTH_SHORT).show();;
                        }
                    }
                });

                System.out.println(flag);

                if(flag==0) {

                    DatabaseHandler db1 = new DatabaseHandler(getApplicationContext());
                    List<Question> submitquestion = db1.getAllQuestion();

                    questionreference=FirebaseDatabase.getInstance().getReference().child("Questions").child(formid);

                    for (Question cn : submitquestion) {


                        String questionid=questionreference.push().getKey();
                        HashMap<String,String> usermap1=new HashMap<>();
                        usermap1.put("Question",cn.getQuestion());
                        usermap1.put("Type",cn.get_type());
                        usermap1.put("Options",cn.getOptions());

                        questionreference.child(questionid).setValue(usermap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Intent i=new Intent(getApplicationContext(),CreateForm.class);

                                    String userid="";
                                    if(mAuth.getCurrentUser()!=null)
                                        userid = mAuth.getCurrentUser().getUid();

                                    i.putExtra("user_id",userid);
                                    i.putExtra("form_id",formid);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }

                }

            }
        });

        mAddButton = (Button) findViewById(R.id.but_add_question);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddQuestionFragment dialogFragment = new AddQuestionFragment();
                dialogFragment.show(fm, "Add Contact Fragment");
            }
        });

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");


        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Question> questions = db.getAllQuestion();
        for (Question cn : questions) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getQuestion() + " ,Phone: " + cn.get_type();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(FormActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new QuestionAdapter(questions,this));

    }
}
