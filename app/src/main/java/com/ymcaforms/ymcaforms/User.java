package com.ymcaforms.ymcaforms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by Kachucool on 25-11-2017.
 */

public class User extends AppCompatActivity{

    String userId,formId;
    FirebaseAuth mAuth;
    DatabaseReference mquestiondatabase;
    LinearLayout linearLayout;
    DatabaseReference mformdatabase,mresponserefernce;
    ArrayList<EditText> allEds=new ArrayList<>();
    ArrayList<RadioGroup> allradiogroup=new ArrayList<>();
    ArrayList<RadioButton> allradio=new ArrayList<>();
    ArrayList<CheckBox> allcheck=new ArrayList<>();
    ArrayList<Integer> radiocount=new ArrayList<>();
    ArrayList<Integer> checkcount=new ArrayList<>();
    StorageReference mimagereference;
    TextView tv1;
    Button b;
    ImageView iv;
    CardView cd;
    TextView tv;
    int count=0,question=0,text=0,radiogroup=0,radio=0,check=0,checkbox=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

        userId=getIntent().getStringExtra("user_id");
        formId=getIntent().getStringExtra("form_id");


        cd=(CardView)findViewById(R.id.create_cardview);
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        b=(Button)findViewById(R.id.create_button);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        mquestiondatabase= FirebaseDatabase.getInstance().getReference().child("Questions").child(formId);
        linearLayout=(LinearLayout)findViewById(R.id.create_linear);
        mformdatabase=FirebaseDatabase.getInstance().getReference().child("Forms").child(userId);

        mimagereference= FirebaseStorage.getInstance().getReference();
        //iv=(ImageView)findViewById(R.id.ImageView);

        mimagereference=FirebaseStorage.getInstance().getReference();
        mformdatabase.child(formId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String form_name=dataSnapshot.child("form_name").getValue().toString();
                String form_description=dataSnapshot.child("form_description").getValue().toString();

                TextView textView= new TextView(getApplicationContext());
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.weight=1;
                textView.setLayoutParams(layoutParams);
                textView.setText(form_name);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
                textView.setTextColor(getResources().getColor(R.color.black));
                linearLayout.addView(textView);

                TextView textView1= new TextView(getApplicationContext());
                textView.setLayoutParams(layoutParams);
                textView1.setText(form_description);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                textView1.setTextColor(getResources().getColor(R.color.black));
                linearLayout.addView(textView1);

                mquestiondatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            count++;
                            String question=snapshot.child("Question").getValue().toString();
                            String type=snapshot.child("Type").getValue().toString();
                            String options=snapshot.child("Options").getValue().toString();

                            int c=0;
                            String s="";
                            ArrayList<String> option=new ArrayList<>();
                            for(int j=0; j<options.length(); j++)
                            {
                                if(options.charAt(j)=='$' && options.charAt(j+1)=='$' && options.charAt(j+2)=='$')
                                {
                                    c++;
                                    option.add(s);
                                    j=j+2;
                                    s="";
                                }
                                else
                                    s=s+options.charAt(j);
                            }

                            TextView textView=new TextView(getApplicationContext());
                            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0,60,0,0);
                            textView.setTextColor(getResources().getColor(R.color.black));
                            textView.setLayoutParams(layoutParams);

                            textView.setText(question);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
                            linearLayout.addView(textView);

                            if(type.equals("Text"))
                            {
                                EditText ed=new EditText(getApplicationContext());
                                layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(0,20,0,0);
                                ed.setLayoutParams(layoutParams);
                                allEds.add(ed);
                                ed.setHint("Your Answer");
                                ed.setTextSize(TypedValue.COMPLEX_UNIT_SP,14f);
                                ed.setTextColor(getResources().getColor(R.color.black));
                                linearLayout.addView(ed);
                            }

                            if(type.equals("Radio Button"))
                            {
                                RadioGroup rg=new RadioGroup(getApplicationContext());
                                layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(0,20,0,0);
                                rg.setLayoutParams(layoutParams);
                                allradiogroup.add(rg);


                                for(int i=0; i<option.size(); i++)
                                {
                                    RadioButton rb=new RadioButton(getApplicationContext());
                                    layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(0,15,0,0);
                                    rb.setLayoutParams(layoutParams);
                                    allradio.add(rb);
                                    rb.setText(option.get(i));
                                    rb.setTextColor(getResources().getColor(R.color.black));
                                    rg.addView(rb);
                                }

                                radiocount.add(option.size());
                                linearLayout.addView(rg);
                            }

                            if(type.equals("Check Box"))
                            {
                                for(int i=0; i<option.size(); i++)
                                {
                                    CheckBox cb = new CheckBox(getApplicationContext());
                                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(0, 10, 0, 0);
                                    cb.setLayoutParams(layoutParams);
                                    cb.setText(option.get(i));
                                    cb.setTextColor(getResources().getColor(R.color.black));
                                    allcheck.add(cb);
                                    linearLayout.addView(cb);
                                }

                                checkcount.add(option.size());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mquestiondatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mresponserefernce=FirebaseDatabase.getInstance().getReference().child("Responses").child(formId);
                        String response_id=mresponserefernce.push().getKey();

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            HashMap<String,String> hashMap=new HashMap<>();
                            String questiion_id=snapshot.getKey();
                            System.out.println(questiion_id);
                            String question=snapshot.child("Question").getValue().toString();
                            String type=snapshot.child("Type").getValue().toString();

                            String answer="";

                            if(type.equals("Text"))
                            {
                                answer=allEds.get(text).getText().toString();
                                text++;
                            }

                            if(type.equals("Radio Button"))
                            {
                                for(int i=radio; i<radio+radiocount.get(radiogroup); i++)
                                {
                                    if(allradio.get(i).isChecked())
                                    {
                                        answer=allradio.get(i).getText().toString();
                                    }
                                }

                                radio+=radiocount.get(radiogroup);
                                radiogroup++;
                            }

                            if(type.equals("Check Box"))
                            {
                                for(int i=check; i<check+checkcount.get(checkbox); i++)
                                {
                                    if(allcheck.get(i).isChecked())
                                    {
                                        answer+=allcheck.get(i).getText().toString()+"$$$";
                                    }
                                }

                                check+=checkcount.get(checkbox);
                                checkbox++;
                            }

                            hashMap.put("Question",question);
                            hashMap.put("Answer",answer);
                            hashMap.put("Type",type);

                            mresponserefernce.child(response_id).child(questiion_id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                        Toast.makeText(getApplicationContext(),"Response Recorded",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
