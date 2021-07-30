package com.example.root.kfgdealerpaymentv1.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.PaymentListModal;

import java.util.List;

/**
 * Created by root on 2/22/21.
 */

public class PaymentListRecyclerViewAdapter extends RecyclerView.Adapter<PaymentListRecyclerViewAdapter.ViewHolder>{

    List<PaymentListModal> paymentListModals;
    Context mContext;
    SqliteDbHelper sqliteDbHelper;

    public PaymentListRecyclerViewAdapter(List<PaymentListModal> paymentListModals, Context mContext) {
        this.paymentListModals = paymentListModals;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list_layout,parent,false);
        return new PaymentListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder,int position) {
        final boolean editable = false;
        holder.paymentTextView.setText(paymentListModals.get(position).getPaymentName());
        holder.paymentAmtEditText.setText(String.valueOf(paymentListModals.get(position).getAmount()));

    }

    @Override
    public int getItemCount() {
        return paymentListModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView paymentTextView;
        EditText paymentAmtEditText;
        ImageButton editBtn,saveBtn;
        ImageButton deleteBtn;

        public ViewHolder(View itemView){
            super(itemView);
            paymentTextView = itemView.findViewById(R.id.paymentTextView);
            paymentAmtEditText = itemView.findViewById(R.id.paymentAmtEditText);
            editBtn = itemView.findViewById(R.id.btn_edit_payment);
            saveBtn = itemView.findViewById(R.id.btn_save_payment);
            deleteBtn = itemView.findViewById(R.id.btn_delete_payment);

            sqliteDbHelper = new SqliteDbHelper(mContext);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentAmtEditText.setEnabled(true);
                    paymentAmtEditText.setFocusable(true);
                    paymentAmtEditText.requestFocus();
                    editBtn.setVisibility(View.INVISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                }
            });


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentAmtEditText.setEnabled(false);
                    sqliteDbHelper.updatePaymentListItem(String.valueOf(paymentListModals.get(getAdapterPosition()).getID()),paymentAmtEditText.getText().toString());
                    saveBtn.setVisibility(View.INVISIBLE);
                    editBtn.setVisibility(View.VISIBLE);

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqliteDbHelper.deletePaymentListItem(paymentListModals.get(getAdapterPosition()).getID());
                    paymentListModals.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),paymentListModals.size());
                }
            });
        }


    }
}
