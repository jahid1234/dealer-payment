package com.example.root.kfgdealerpaymentv1.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.OrderProductModal;

import java.util.List;

import static com.example.root.kfgdealerpaymentv1.activity.SearchCustomerActivity.customer_Code;

/**
 * Created by root on 2/12/20.
 */

public class SingleProductRecyclerViewAdapter extends RecyclerView.Adapter<SingleProductRecyclerViewAdapter.ViewHolder> {

    List<OrderProductModal> orderProductModalList;
    Context mContext;

    public SingleProductRecyclerViewAdapter(List<OrderProductModal> orderProductModalList, Context mContext) {
        this.orderProductModalList = orderProductModalList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_recyclerview_item,parent,false);
        return new SingleProductRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // holder.setCheckbox(position);
        String firstCharacter = orderProductModalList.get(position).getProductName().substring(0,1);
        holder.productTitleTextView.setText(firstCharacter);
        holder.productNameTextView.setText(orderProductModalList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return orderProductModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productNameTextView;
        TextView productTitleTextView;
        CardView cardViewBtn;
        SqliteDbHelper sqliteDbHelper;
       // CheckBox ansCheckBox;

        public ViewHolder(View itemView){
            super(itemView);

            productTitleTextView = itemView.findViewById(R.id.single_product_child_item_title_big);
            productNameTextView = itemView.findViewById(R.id.single_product_child_item_title);
            cardViewBtn = itemView.findViewById(R.id.product_cardView_id_single_category);

            sqliteDbHelper = new SqliteDbHelper(mContext);
            cardViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.setContentView(R.layout.activity_add_to_cart);

                    TextView bigTitle = dialog.findViewById(R.id.order_dialog_child_item_title_big);
                    final TextView smallTitle = dialog.findViewById(R.id.order_dialog_product_name_textView);
                    ImageButton cancelBtn    = dialog.findViewById(R.id.order_dialog_close_dialogBtn_id);
                    Button addButton   = dialog.findViewById(R.id.order_dialog_add_btn_id);
                    TextView price  = dialog.findViewById(R.id.order_dialog_product_price_text);
                    final EditText quantity = dialog.findViewById(R.id.order_dialog_product_qty_EditText);

                    final String getFirstCharacter = orderProductModalList.get(getAdapterPosition()).getProductName().substring(0,1);
                    bigTitle.setText(getFirstCharacter);
                    smallTitle.setText(orderProductModalList.get(getAdapterPosition()).getProductName());

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(quantity.getText().toString().equals("")){
                                quantity.setError("Enter quantity");
                                return;
                            }
                            sqliteDbHelper.insertIntoCartDataTable(customer_Code,orderProductModalList.get(getAdapterPosition()).getID(),smallTitle.getText().toString(),quantity.getText().toString());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

    }
}
