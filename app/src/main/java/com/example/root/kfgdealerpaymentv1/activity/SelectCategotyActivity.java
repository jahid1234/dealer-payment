package com.example.root.kfgdealerpaymentv1.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.adapter.OrderProductCategoryRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.OrderProductModal;

import java.util.ArrayList;

public class SelectCategotyActivity extends AppCompatActivity {

    RecyclerView productCategoryRecyclerView;
    Toolbar toolbar;
    ProgressDialog pd;
    ImageButton cartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_categoty);

        productCategoryRecyclerView = findViewById(R.id.product_category_recyclerview);
        toolbar = findViewById(R.id.toolbar);
        cartBtn = findViewById(R.id.cart_imageBtn_id_selection_activity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SelectCategotyActivity.this,SearchCustomerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        AsyncLoadAnswers task = new AsyncLoadAnswers();
        task.execute();
        goCart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.productcolor));
        }
    }

    private void goCart(){
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SelectCategotyActivity.this,CartListActivity.class);
                intent.putExtra("activity","select_category");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

    }


    private final class AsyncLoadAnswers extends AsyncTask<String,String,String> {


        ArrayList<OrderProductModal> orderCategoryModalArrayList = new ArrayList<>();
        //   ArrayList<String> orderCategoryArrayList = new ArrayList<>();

//        private Context mContext;
//
//        public AsyncLoadAnswers(Context context) {
//            this.mContext = context;
//        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            OrderProductModal productModal;
            productModal = new OrderProductModal("0","Item All");
            orderCategoryModalArrayList.add(productModal);
            //String productCategory[] = getResources().getStringArray(R.array.product_categorylist_arrays);
            SqliteDbHelper sqliteDbHelper = new SqliteDbHelper(SelectCategotyActivity.this);
            Cursor cursor = sqliteDbHelper.getCategory();

                while (cursor.moveToNext()) {
                    String category_id = cursor.getString(0);
                    String categoryName = cursor.getString(1);
                    productModal = new OrderProductModal(category_id,categoryName);
                    orderCategoryModalArrayList.add(productModal);
                    // orderCategoryArrayList.add(categoryName);
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
                       initCategoryRecyclerView(orderCategoryModalArrayList);

                }
            }catch (NullPointerException ex){
                Toast.makeText(SelectCategotyActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void initCategoryRecyclerView(ArrayList<OrderProductModal> orderCategoryModalArrayList){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        productCategoryRecyclerView.setLayoutManager(gridLayoutManager);
        OrderProductCategoryRecyclerViewAdapter adapter = new OrderProductCategoryRecyclerViewAdapter(orderCategoryModalArrayList,SelectCategotyActivity.this);
        productCategoryRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(SelectCategotyActivity.this,SearchCustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(SelectCategotyActivity.this);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
