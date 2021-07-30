package com.example.root.kfgdealerpaymentv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.modal.QuestionModal;

import java.util.List;

/**
 * Created by root on 2/12/20.
 */

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder>  {
    List<QuestionModal> questionModals;
    Context mContext;
    private int adapterPosition;

    public QuestionRecyclerViewAdapter(List<QuestionModal> questionModals, Context mContext) {
        this.questionModals = questionModals;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_list_layout,parent,false);
        return new QuestionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.questionTextView.setText(questionModals.get(position).getMethodName());


//        holder.questionTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, OrderActivity.class);
//                intent.putExtra("selected_question",questionModals.get(position).getQuestions());
//                intent.putExtra("question_id",questionModals.get(position).getQuestion_ID());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return questionModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView questionTextView;
        EditText paymentAmtEditText;

        public ViewHolder(View itemView){
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            paymentAmtEditText = itemView.findViewById(R.id.paymentAmtEditText);
        }


    }
}
