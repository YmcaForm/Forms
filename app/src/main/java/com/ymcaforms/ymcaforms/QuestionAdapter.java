package com.ymcaforms.ymcaforms;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder>{
    Context mContext;
    List<Question> mQuestion;
    public QuestionAdapter(List<Question> mQuestion, Context mContext) {
        this.mContext = mContext;
        this.mQuestion = mQuestion;
    }

    public QuestionAdapter() {

    }

    @Override
    public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_row,parent,false);
        return new QuestionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuestionHolder holder, final int position) {
        holder.tv_question.setText(String.valueOf(mQuestion.get(position).getQuestion())+" ?");
        holder.tv_type.setText(String.valueOf(mQuestion.get(position).get_type()));

        List<String> option=new ArrayList<>();
        String s="",g=mQuestion.get(position).getOptions();

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
            TextView textView= new TextView(mContext);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(option.get(j));
            holder.linearLayout.addView(textView);
        }


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.FragmentManager fm= ((Activity)mContext).getFragmentManager();
                UpdateQuestionFragment dialogFragment=new UpdateQuestionFragment(mQuestion.get(position).getID());
                dialogFragment.show(fm, "Update Question Fragment");

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseHandler db=new DatabaseHandler(mContext);
                                            db.deleteQuestion(mQuestion.get(position).getID());
                                            List<Question> questions = db.getAllQuestion();
                                            ((com.ymcaforms.ymcaforms.FormActivity) view.getContext()).recyclerView.setAdapter(new QuestionAdapter(questions,view.getContext()));

                                        }
                                    });

    }

    @Override
    public int getItemCount() {
        return mQuestion.size();
    }


    protected class QuestionHolder extends RecyclerView.ViewHolder{
        TextView tv_type,tv_question;
        CardView mCardView;
        Button delete;
        LinearLayout linearLayout;
        public QuestionHolder(View itemView) {
            super(itemView);
            tv_type=(TextView)itemView.findViewById(R.id.card_type);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            tv_question=(TextView) itemView.findViewById(R.id.card_question);
            delete=(Button)itemView.findViewById(R.id.card_delete);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.card_option_linear);

        }
    }

}