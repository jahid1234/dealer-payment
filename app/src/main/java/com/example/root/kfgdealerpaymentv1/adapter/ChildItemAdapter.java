package com.example.root.kfgdealerpaymentv1.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
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
import com.example.root.kfgdealerpaymentv1.activity.ChildItem;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;

import java.util.List;

import static com.example.root.kfgdealerpaymentv1.activity.SearchCustomerActivity.customer_Code;
import static com.example.root.kfgdealerpaymentv1.activity.SearchCustomerActivity.customer_Code;

/**
 * Created by root on 3/1/21.
 */

public class ChildItemAdapter extends RecyclerView.Adapter<ChildItemAdapter.ChildViewHolder> {

    Context mContext;
    private List<ChildItem> ChildItemList;

    // Constuctor
    ChildItemAdapter(Context mContext,List<ChildItem> childItemList)
    {
        this.mContext = mContext;
        this.ChildItemList = childItemList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i)
    {

        // Here we inflate the corresponding
        // layout of the child item
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.child_recyclerview_item,
                        viewGroup, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int position)
    {

        // Create an instance of the ChildItem
        // class for the given position
        ChildItem childItem = ChildItemList.get(position);

        // For the created instance, set title.
        // No need to set the image for
        // the ImageViews because we have
        // provided the source for the images
        // in the layout file itself
        childViewHolder.ChildItemTitle.setText(childItem.getChildItemTitle());
        String getFirstCharacter = childItem.getChildItemTitle().substring(0,1);
        childViewHolder.titleBigText.setText(getFirstCharacter);

    }

    @Override
    public int getItemCount()
    {

        // This method returns the number
        // of items we have added
        // in the ChildItemList
        // i.e. the number of instances
        // of the ChildItemList
        // that have been created
        return ChildItemList.size();
    }

    // This class is to initialize
    // the Views present
    // in the child RecyclerView
    class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView ChildItemTitle,titleBigText;
        CardView productCardView;
        SqliteDbHelper sqliteDbHelper;
        ChildViewHolder(View itemView)
        {
            super(itemView);
            ChildItemTitle = itemView.findViewById(R.id.child_item_title);
            titleBigText = itemView.findViewById(R.id.child_item_title_big);

            productCardView = itemView.findViewById(R.id.product_cardView_id);

            sqliteDbHelper = new SqliteDbHelper(mContext);
            productCardView.setOnClickListener(new View.OnClickListener() {
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

                    String getFirstCharacter = ChildItemList.get(getAdapterPosition()).getChildItemTitle().substring(0,1);
                    bigTitle.setText(getFirstCharacter);
                    smallTitle.setText(ChildItemList.get(getAdapterPosition()).getChildItemTitle());

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

                            sqliteDbHelper.insertIntoCartDataTable(customer_Code,ChildItemList.get(getAdapterPosition()).getID(),smallTitle.getText().toString(),quantity.getText().toString());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }
}
