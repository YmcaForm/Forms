package com.ymcaforms.ymcaforms;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
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

public class User extends AppCompatActivity {

    String userId, formId;
    FirebaseAuth mAuth;
    DatabaseReference mquestiondatabase;
    LinearLayout linearLayout;
    DatabaseReference mformdatabase, mresponserefernce;
    ArrayList<EditText> allEds = new ArrayList<>();
    ArrayList<RadioGroup> allradiogroup = new ArrayList<>();
    ArrayList<RadioButton> allradio = new ArrayList<>();
    ArrayList<CheckBox> allcheck = new ArrayList<>();
    ArrayList<Integer> radiocount = new ArrayList<>();
    ArrayList<Integer> checkcount = new ArrayList<>();
    ArrayList<LinearLayout> linearLayouts=new ArrayList<>();
    StorageReference mimagereference;
    TextView tv1;
    Button b;
    ImageView iv;
    CardView cd;
    TextView tv;
    int count = 0, question = 0, text = 0, radiogroup = 0, radio = 0, check = 0, checkbox = 0,k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userId = getIntent().getStringExtra("user_id");
        formId = getIntent().getStringExtra("form_id");


        cd = (CardView) findViewById(R.id.create_cardview);
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        b = (Button) findViewById(R.id.create_button);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        mquestiondatabase = FirebaseDatabase.getInstance().getReference().child("Questions").child(formId);
        linearLayout = (LinearLayout) findViewById(R.id.create_linear);
        mformdatabase = FirebaseDatabase.getInstance().getReference().child("Forms").child(userId);

        mimagereference = FirebaseStorage.getInstance().getReference();
        //iv=(ImageView)findViewById(R.id.ImageView);

        mimagereference = FirebaseStorage.getInstance().getReference();
        mformdatabase.child(formId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String form_name = "", form_description = "";

                if (dataSnapshot.child("form_name").getValue() != null)
                    form_name = dataSnapshot.child("form_name").getValue().toString();

                if (dataSnapshot.child("form_description").getValue() != null)
                    form_description = dataSnapshot.child("form_description").getValue().toString();

                LinearLayout linearLayout1=new LinearLayout(getApplicationContext());
                linearLayout1.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,70,0,0);
                linearLayout1.setLayoutParams(layoutParams);


                TextView textView = new TextView(getApplicationContext());
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(form_name);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
                textView.setTextColor(getResources().getColor(R.color.black));
                linearLayout1.addView(textView);

                TextView textView1 = new TextView(getApplicationContext());
                layoutParams.setMargins(0,20,0,0);
                textView.setLayoutParams(layoutParams);
                textView1.setText(form_description);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                textView1.setTextColor(getResources().getColor(R.color.black));
                linearLayout1.addView(textView1);

                linearLayout.addView(linearLayout1);
                linearLayouts.add(linearLayout1);

                mquestiondatabase.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String question, type, options ;
                            count++;

                                question = snapshot.child("Question").getValue().toString();
                                type = snapshot.child("Type").getValue().toString();
                                options = snapshot.child("Options").getValue().toString();

                            int c = 0;
                            String s = "";
                            ArrayList<String> option = new ArrayList<>();
                            for (int j = 0; j < options.length(); j++) {
                                if (options.charAt(j) == '$' && options.charAt(j + 1) == '$' && options.charAt(j + 2) == '$') {
                                    c++;
                                    option.add(s);
                                    j = j + 2;
                                    s = "";
                                } else
                                    s = s + options.charAt(j);
                            }

                            LinearLayout linearLayout2=new LinearLayout(getApplicationContext());
                            linearLayout2.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0,30,0,30);
                            linearLayout2.setLayoutParams(layoutParams);

                            TextView textView = new TextView(getApplicationContext());
                            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 100, 0, 0);
                            textView.setTextColor(getResources().getColor(R.color.black));
                            textView.setLayoutParams(layoutParams);

                            textView.setText(question);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                            linearLayout2.addView(textView);

                            if (type.equals("Text")) {
                                EditText ed = new EditText(getApplicationContext());
                                layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(0, 30, 0, 0);
                                ed.setLayoutParams(layoutParams);
                                allEds.add(ed);
                                ed.setHint("Your Answer");
                                ed.getBackground().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                                ed.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                                ed.setTextColor(getResources().getColor(R.color.black));
                                linearLayout2.addView(ed);
                            }

                            if (type.equals("Radio Button")) {
                                RadioGroup rg = new RadioGroup(getApplicationContext());
                                layoutParams.setMargins(0, 30, 0, 0);
                                rg.setLayoutParams(layoutParams);
                                allradiogroup.add(rg);


                                for (int i = 0; i < option.size(); i++) {
                                    RadioButton rb = new RadioButton(getApplicationContext());
                                    int color=Color.parseColor("#000000");
                                    rb.setButtonTintList(ColorStateList.valueOf(color));
                                    int color1=Color.parseColor("#FFC0CB");
                                    rb.setHighlightColor(color1);
                                    layoutParams.setMargins(0, 10, 0, 0);
                                    rb.setLayoutParams(layoutParams);
                                    allradio.add(rb);
                                    rb.setText(option.get(i));
                                    rb.setTextColor(getResources().getColor(R.color.black));
                                    rg.addView(rb);
                                }

                                radiocount.add(option.size());
                                linearLayout2.addView(rg);
                            }

                            if (type.equals("Check Box")) {
                                for (int i = 0; i < option.size(); i++) {
                                    CheckBox cb = new CheckBox(getApplicationContext());
                                    int color=Color.parseColor("#000000");
                                    cb.setButtonTintList(ColorStateList.valueOf(color));
                                    layoutParams.setMargins(0, 10, 0, 0);
                                    cb.setLayoutParams(layoutParams);
                                    cb.setText(option.get(i));
                                    cb.setTextColor(getResources().getColor(R.color.black));
                                    allcheck.add(cb);
                                    linearLayout2.addView(cb);
                                }

                                checkcount.add(option.size());
                            }

                            linearLayout.addView(linearLayout2);
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

                        mresponserefernce = FirebaseDatabase.getInstance().getReference().child("Responses").child(formId);
                        String response_id = mresponserefernce.push().getKey();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            String questiion_id = snapshot.getKey();
                            System.out.println(questiion_id);

                            String question , type ;
                                question = snapshot.child("Question").getValue().toString();
                                type = snapshot.child("Type").getValue().toString();

                            String answer = "";

                            if (type.equals("Text")) {
                                answer = allEds.get(text).getText().toString();
                                text++;
                            }

                            if (type.equals("Radio Button")) {
                                for (int i = radio; i < radio + radiocount.get(radiogroup); i++) {
                                    if (allradio.get(i).isChecked()) {
                                        answer = allradio.get(i).getText().toString();
                                    }
                                }

                                radio += radiocount.get(radiogroup);
                                radiogroup++;
                            }

                            if (type.equals("Check Box")) {
                                for (int i = check; i < check + checkcount.get(checkbox); i++) {
                                    if (allcheck.get(i).isChecked()) {
                                        answer += allcheck.get(i).getText().toString() + "$$$";
                                    }
                                }

                                check += checkcount.get(checkbox);
                                checkbox++;
                            }

                            hashMap.put("Question", question);
                            hashMap.put("Answer", answer);
                            hashMap.put("Type", type);

                            mresponserefernce.child(response_id).child(questiion_id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                        Toast.makeText(getApplicationContext(), "Response Recorded", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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
