package com.example.root.kfgdealerpaymentv1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.activity.OrderActivity;
import com.example.root.kfgdealerpaymentv1.activity.SingleCategoryProductsActivity;
import com.example.root.kfgdealerpaymentv1.modal.OrderProductModal;

import java.io.BufferedReader;
import java.util.List;

/**
 * Created by root on 2/12/20.
 */

public class OrderProductCategoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderProductCategoryRecyclerViewAdapter.ViewHolder> {

    List<OrderProductModal> orderProductModalList;
    Context mContext;

    public OrderProductCategoryRecyclerViewAdapter(List<OrderProductModal> orderProductModalList, Context mContext) {
        this.orderProductModalList = orderProductModalList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_category_list_layout,parent,false);
        return new OrderProductCategoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // holder.setCheckbox(position);
      //  String firstCharacter = orderProductModalList.get(position).getProductName().substring(0,1);
      //  holder.productCategoryTitleTextView.setText(firstCharacter);
        holder.productCategoryTextView.setText(orderProductModalList.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return orderProductModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productCategoryTextView,productCategoryTitleTextView;
        CardView categoryCardView;

        public ViewHolder(View itemView){
            super(itemView);
            productCategoryTextView = itemView.findViewById(R.id.product_category_text);
           // productCategoryTitleTextView = itemView.findViewById(R.id.category_title_big);
            categoryCardView = itemView.findViewById(R.id.order_product_category_cardView);

            categoryCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryName = productCategoryTextView.getText().toString();
                    if(categoryName.equals("Item All")){

                        Intent intent = new Intent(mContext,OrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else{
                        Intent intent = new Intent(mContext,SingleCategoryProductsActivity.class);
                        intent.putExtra("category_id",orderProductModalList.get(getAdapterPosition()).getID());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                        ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }

                }
            });
        }

    }
}
