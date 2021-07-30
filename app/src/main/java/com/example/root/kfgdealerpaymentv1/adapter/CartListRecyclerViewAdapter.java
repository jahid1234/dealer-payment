package com.example.root.kfgdealerpaymentv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.CartProductModal;
import com.example.root.kfgdealerpaymentv1.modal.PaymentListModal;

import java.util.List;

/**
 * Created by root on 2/22/21.
 */

public class CartListRecyclerViewAdapter extends RecyclerView.Adapter<CartListRecyclerViewAdapter.ViewHolder>{

    List<CartProductModal> cartProductModals;
    Context mContext;

    SqliteDbHelper sqliteDbHelper;

    public CartListRecyclerViewAdapter(List<CartProductModal> paymentListModals, Context mContext) {
        this.cartProductModals = paymentListModals;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_recyclerview_item,parent,false);
        return new CartListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder,int position) {
        final boolean editable = false;
      //  holder.productNoText.setText("Item : " + String.valueOf(position + 1));
        holder.productNameText.setText(cartProductModals.get(position).getProduct_name());
        holder.quantityEditText.setText(cartProductModals.get(position).getQty());

    }

    @Override
    public int getItemCount() {
        return cartProductModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productNoText,productNameText;
        EditText quantityEditText;
        ImageButton editBtn,saveBtn,confirmBtn;
        ImageButton deleteBtn;

        public ViewHolder(View itemView){
            super(itemView);
         //   productNoText = itemView.findViewById(R.id.cart_product_no_textView);
            productNameText = itemView.findViewById(R.id.cart_product_name_textview);
            quantityEditText = itemView.findViewById(R.id.cart_product_qty_editTextview);
            editBtn = itemView.findViewById(R.id.cart_qty_edit_btn);
//            confirmBtn = itemView.findViewById(R.id.cart_confirm_btn);
            deleteBtn = itemView.findViewById(R.id.cart_product_delete_btn);
            saveBtn   = itemView.findViewById(R.id.cart_qty_save_btn);
            sqliteDbHelper = new SqliteDbHelper(mContext);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantityEditText.setEnabled(true);
                    quantityEditText.setFocusable(true);
                    quantityEditText.requestFocus();
                    editBtn.setVisibility(View.INVISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantityEditText.setEnabled(false);
                    sqliteDbHelper.updateCartItem(String.valueOf(cartProductModals.get(getAdapterPosition()).getID()),quantityEditText.getText().toString());
                    saveBtn.setVisibility(View.INVISIBLE);
                    editBtn.setVisibility(View.VISIBLE);

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sqliteDbHelper.deleteCartTableItem(cartProductModals.get(getAdapterPosition()).getID());
                    cartProductModals.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),cartProductModals.size());
                }
            });
        }


    }
}
