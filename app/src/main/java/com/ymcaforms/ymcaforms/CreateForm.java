package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class CreateForm extends AppCompatActivity {

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

        tv1=(TextView)findViewById(R.id.form_url);

        final BranchUniversalObject buo = new BranchUniversalObject()
                .setContentMetadata(new ContentMetadata().addCustomMetadata("key1", userId).addCustomMetadata("key2",formId));


        final LinkProperties lp = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 123 launch")
                .setStage("new user")
                .addControlParameter("$desktop_url", "http://example.com/home")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        buo.generateShortUrl(CreateForm.this, lp, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", "got my Branch link to share: " + url);
                    tv1.setText(url);
                }
            }
        });


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareSheetStyle ss = new ShareSheetStyle(CreateForm.this, "Check this out!", "Ymca Forms")
                        .setCopyUrlStyle(ContextCompat.getDrawable(CreateForm.this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                        .setMoreOptionStyle(ContextCompat.getDrawable(CreateForm.this, android.R.drawable.ic_menu_search), "Show more")
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                        .setAsFullWidthStyle(true)
                        .setSharingTitle("Share With");

                buo.showShareSheet(CreateForm.this, lp,  ss,  new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }
                    @Override
                    public void onShareLinkDialogDismissed() {
                    }
                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                    }
                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });

            }
        });


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

                String form_name="",form_description="";
                if(dataSnapshot.child("form_name").getValue()!=null)
                form_name=dataSnapshot.child("form_name").getValue().toString();

                if(dataSnapshot.child("form_description").getValue()!=null)
                form_description=dataSnapshot.child("form_description").getValue().toString();

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

                            String question="",type="",options="";
                            count++;

                            if(dataSnapshot.child("Question").getValue()!=null)
                            question=snapshot.child("Question").getValue().toString();

                            if(dataSnapshot.child("Type").getValue()!=null)
                            type=snapshot.child("Type").getValue().toString();

                            if(dataSnapshot.child("Options").getValue()!=null)
                            options=snapshot.child("Options").getValue().toString();

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


        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap=getBitmapFromView(cd);


                        StorageReference mountainsRef = mimagereference.child("Screenshots").child(formId+".jpg");
                        StorageReference mountainImagesRef = mimagereference.child("images/mountains.jpg");

                        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                        mountainsRef.getPath().equals(mountainImagesRef.getPath());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    String url = task.getResult().getDownloadUrl().toString();
                                        if(task.isSuccessful())
                                        {
                                            Map update_hashmap = new HashMap();
                                            update_hashmap.put("image", url);
                                            DatabaseReference mformimage=FirebaseDatabase.getInstance().getReference().child("Images").child(userId);
                                            mformimage.child(formId).setValue(update_hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"oyooyooy",Toast.LENGTH_SHORT);
                                                    }
                                                }
                                            });
                                        }
                            }
                        });
                    }
                });
            }
        };
        thread.start();


                /*rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap;
                rootView.buildDrawingCache();
                bitmap= Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);
                iv.setImageBitmap(bitmap);*/



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


    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
