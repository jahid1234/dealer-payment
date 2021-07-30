package com.example.root.kfgdealerpaymentv1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.adapter.SingleProductRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.OrderProductModal;

import java.util.ArrayList;

public class SingleCategoryProductsActivity extends AppCompatActivity {

    RecyclerView singleProductListRecyclerView;
    ProgressDialog pd;
    Toolbar toolbar;
    ImageButton cartImageBtn;
    String categoryID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category_products);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            categoryID = (String)bundle.get("category_id");
        }
        singleProductListRecyclerView = findViewById(R.id.single_product_list_recyclerview);

        cartImageBtn = findViewById(R.id.cart_imageBtn_id_single_product_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SingleCategoryProductsActivity.this,SelectCategotyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });



        AsyncLoadAnswers task = new AsyncLoadAnswers();
        task.execute();
        buttonClick();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.productcolor));
        }
    }

    private void buttonClick(){
        cartImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SingleCategoryProductsActivity.this,CartListActivity.class);
                intent.putExtra("activity","single_category");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private final class AsyncLoadAnswers extends AsyncTask<String,String,String> {


        ArrayList<OrderProductModal> orderProductModalArrayList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
           // String product[] = getResources().getStringArray(R.array.product_name_array_list);
            SqliteDbHelper sqliteDbHelper = new SqliteDbHelper(SingleCategoryProductsActivity.this);
            Cursor cursor = sqliteDbHelper.getProduct(categoryID);
            while(cursor.moveToNext()) {

                String ID = cursor.getString(0);
                String pName = cursor.getString(1);
                OrderProductModal productModal = new OrderProductModal(ID,pName);
                orderProductModalArrayList.add(productModal);
                res = "Success";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Success")){
                    initRecyclerViewSingle(orderProductModalArrayList);
                }
            }catch (NullPointerException ex){
                Toast.makeText(SingleCategoryProductsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(SingleCategoryProductsActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading.....");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        pd.show();
    }

    private void dismissProgressDialog(){
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    private void initRecyclerViewSingle(ArrayList<OrderProductModal> orderProductModalArrayList){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        singleProductListRecyclerView.setLayoutManager(gridLayoutManager);
        SingleProductRecyclerViewAdapter adapter = new SingleProductRecyclerViewAdapter(orderProductModalArrayList,SingleCategoryProductsActivity.this);
        singleProductListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(SingleCategoryProductsActivity.this,SelectCategotyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}
