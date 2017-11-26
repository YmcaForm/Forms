package com.ymcaforms.ymcaforms;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachucool on 16-11-2017.
 */

@SuppressLint("ValidFragment")
public class UpdateQuestionFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    Button mSubmitButton,maddoptions;
    EditText Question,ed;
    Spinner sp;
    final int mQuestionId;
    Question mQuestion;
    String arr[]={"Text","Radio Button","Check Box"};
    String type;
    LinearLayout linearLayout;
    View rootView;
    int count=0;

    public UpdateQuestionFragment(int mQuestionId) {
        this.mQuestionId=mQuestionId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.update_question_fragment, container, false);
        getDialog().setTitle("Update Question");

        final DatabaseHandler db = new DatabaseHandler(getActivity());

        mQuestion = db.getQuestion(mQuestionId);

        Question = (EditText) rootView.findViewById(R.id.update_question);
        Question.setText(mQuestion.getQuestion().toString());

        sp=(Spinner)rootView.findViewById(R.id.update_spinner);
        type=mQuestion.get_type();

        count=mQuestion.getCount();
        maddoptions=(Button)rootView.findViewById(R.id.update_options);

        ArrayAdapter<String> adap=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,arr);
        sp.setAdapter(adap);
        sp.setOnItemSelectedListener(this);


        linearLayout=(LinearLayout)rootView.findViewById(R.id.update_fragment);

        if(type.equals("Radio Button"))
            sp.setSelection(1);

        if(type.equals("Check Box"))
            sp.setSelection(2);

        final List<String> option=new ArrayList<>();
        String s="",g=mQuestion.getOptions();

        for(int j=0; j<g.length(); j++)
        {
            if(g.charAt(j)=='$' && g.charAt(j+1)=='$' && g.charAt(j+2)=='$')
            {
                option.add(s);
                j=j+2;
                s="";
            }
            else
                s=s+g.charAt(j);
        }

        for(int j=0; j<option.size(); j++)
        {
            EditText editText= new EditText(getContext());
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            editText.setId(j+1);
            editText.setText(option.get(j));
            linearLayout.addView(editText);
        }
        maddoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count++;
                EditText t = new EditText(getContext());
                t.setHint("Option");
                t.setId(count);
                t.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT));
                linearLayout.addView(t);
            }
        });

        mSubmitButton = (Button) rootView.findViewById(R.id.update_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int c=count;
                String options="";
                for(int j=0; j<count; j++)
                {
                    ed=rootView.findViewById(j+1);
                    if(ed.getText().toString().equals(""))
                        c--;
                    else
                        options+=ed.getText().toString()+"$$$";
                }
                db.updateQuestion(new Question(Question.getText().toString(),type,c,options),mQuestionId);
                List<Question> question = db.getAllQuestion();
               ((FormActivity)getActivity()).recyclerView.setAdapter(new QuestionAdapter(question,getActivity()));
                /*ContactAdapter object = new ContactAdapter();
                object.notifyDataSetChanged();*/
                dismiss();
            }
        });


        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type=sp.getItemAtPosition(i).toString();

        if(type.equals("Text"))
        {
            maddoptions.setVisibility(View.INVISIBLE);
            for(int j=0; j<count; j++)
            {
                linearLayout.removeView(rootView.findViewById(j+1));
            }
            count=0;
        }
        else
        {
            maddoptions.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
