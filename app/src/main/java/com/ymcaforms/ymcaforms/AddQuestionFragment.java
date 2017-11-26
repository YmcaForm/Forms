package com.ymcaforms.ymcaforms;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddQuestionFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{
    LinearLayout addFragment,linearLayout;
    Button mSubmitButton;
    EditText mQuestion,ed;
    String arr[]={"Text","Radio Button","Check Box"};
    String type="Text";
    Spinner sp;
    Button addoptions;
    int count=0;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_question_fragment, container, false);
        getDialog().setTitle("Add new Contact");

        mQuestion = (EditText) rootView.findViewById(R.id.add_question);
        addoptions=(Button)rootView.findViewById(R.id.add_options);
        linearLayout=(LinearLayout)rootView.findViewById(R.id.add_fragment);
        sp=(Spinner)rootView.findViewById(R.id.add_spinner);
        ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arr);
        sp.setAdapter(adap);
        sp.setOnItemSelectedListener(this);
        mSubmitButton = (Button) rootView.findViewById(R.id.add_submit);

        addoptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count++;
                EditText t = new EditText(getActivity());
                t.setHint("Option");
                t.setId(count);
                t.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(t);
            }
        });
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
                DatabaseHandler db = new DatabaseHandler(getActivity());
                db.addQuestion(new Question(mQuestion.getText().toString(),type,c,options));
                Toast.makeText(getActivity(), "Added!!..", Toast.LENGTH_SHORT).show();
                List<Question> questions = db.getAllQuestion();
                ((FormActivity)getActivity()).recyclerView.setAdapter(new QuestionAdapter(questions,getActivity()));
                /*ContactAdapter object = new ContactAdapter();
                object.notifyDataSetChanged();*/
                dismiss();
            }
        });

        /*Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.splash);
        addFragment = (LinearLayout) rootView.findViewById(R.id.add_fragment);
        addFragment.setAnimation(animation);*/

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type=sp.getItemAtPosition(i).toString();

        if(type.equals("Text"))
        {
            addoptions.setVisibility(View.INVISIBLE);
            for(int j=0; j<count; j++)
            {
                linearLayout.removeView(rootView.findViewById(j+1));
            }
            count=0;
        }
        else
        {
            addoptions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
