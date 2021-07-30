package com.example.root.kfgdealerpaymentv1.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.adapter.OrderProductCategoryRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.adapter.ParentItemAdapter;
import com.example.root.kfgdealerpaymentv1.adapter.SingleProductRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.OrderProductModal;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog pd;
    RecyclerView orderRecyclerView;
    RecyclerView productCategoryRecyclerView;
    ImageButton cartBtn;

    SqliteDbHelper dbHelper;

    ArrayList<String> orderProductArrayList = new ArrayList<>();
    ArrayList<OrderProductModal> checkedOrderProductModalArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){

        }

        toolbar = findViewById(R.id.toolbar);
        orderRecyclerView  = findViewById(R.id.product_list_recyclerview);
       // orderRecyclerView1 = findViewById(R.id.product_list_recyclerview1);
       // productCategoryRecyclerView = findViewById(R.id.product_category_recyclerview);


        cartBtn = findViewById(R.id.cart_imageBtn_id);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(OrderActivity.this,SelectCategotyActivity.class);
                intent.putExtra("module","order");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        dbHelper = new SqliteDbHelper(this);
        AsyncLoadAnswers task = new AsyncLoadAnswers(getApplicationContext());
        task.execute();
       // dbHelper.deleteCartTable();
        goToCart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.productcolor));
        }
    }


//   public void hideAllProductList(Context mContext){
//       orderRecyclerView.setVisibility(View.INVISIBLE);
//     //  singleProductListRecyclerView.setVisibility(View.VISIBLE);
//       ArrayList<OrderProductModal> orderProductModalArrayList = new ArrayList<>();
////       orderProductModalArrayList.clear();
//       String product[] = mContext.getResources().getStringArray(R.array.product_name_array_list);
//       for(int i = 0;i<product.length;i++){
//           String pName = product[i];
//           OrderProductModal productModal = new OrderProductModal(pName);
//           orderProductModalArrayList.add(productModal);
//       }
//
//      // initRecyclerViewSingle(orderProductModalArrayList);
//    }
//
//   public void showAllProductList(Context mContext){
//      // singleProductListRecyclerView.setVisibility(View.INVISIBLE);
//       orderRecyclerView.setVisibility(View.VISIBLE);
//       AsyncLoadAnswers task = new AsyncLoadAnswers(mContext);
//       task.execute();
//
//    }

    private final class AsyncLoadAnswers extends AsyncTask<String,String,String>{


        ArrayList<OrderProductModal> orderCategoryModalArrayList = new ArrayList<>();
        ArrayList<String> orderCategoryArrayList = new ArrayList<>();

        private Context mContext;

        public AsyncLoadAnswers(Context context) {
            this.mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            //String productCategory[] = getResources().getStringArray(R.array.product_categorylist_arrays);
            Cursor cursor = dbHelper.getCategory();
            while (cursor.moveToNext()) {
                String categoryName = cursor.getString(1);

                orderCategoryArrayList.add(categoryName);
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
                   initRecyclerView(orderCategoryArrayList,mContext);

                }
            }catch (NullPointerException ex){
                Toast.makeText(OrderActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initRecyclerView(ArrayList<String> orderCategoryArrayList,Context mContext){
          LinearLayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
          ParentItemAdapter parentItemAdapter = new ParentItemAdapter(OrderActivity.this,ParentItemList(orderCategoryArrayList,mContext));
          orderRecyclerView.setAdapter(parentItemAdapter);
          orderRecyclerView.setLayoutManager(layoutManager);
    }

    private List<ParentItem> ParentItemList(ArrayList<String> orderCategoryArrayList,Context mContext)
    {
        List<ParentItem> itemList = new ArrayList<>();

        for(int i = 0;i<orderCategoryArrayList.size();i++){
          //  String cName = orderCategoryArrayList.get(i);
            ParentItem item = new ParentItem(orderCategoryArrayList.get(i), ChildItemList(orderCategoryArrayList.get(i)));
            itemList.add(item);
        }


        return itemList;
    }

    private List<ChildItem> ChildItemList(String orderCategoryName)
        {
            List<ChildItem> ChildItemList
                    = new ArrayList<>();

          //  String product[] = getResources().getStringArray(R.array.product_name_array_list);

          //  for(int i = 0;i<orderCategoryArrayList.size();i++){
                Cursor cursor = dbHelper.getProductByName(orderCategoryName);
                while (cursor.moveToNext()){
                    String ID = cursor.getString(0);
                    String pName = cursor.getString(1);
                    ChildItemList.add(new ChildItem(ID,pName));
                }


              //  orderProductModalArrayList.add(productModal);
          //  }

            return ChildItemList;
        }

//    private void initRecyclerView1(){
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
//        orderRecyclerView1.setLayoutManager(gridLayoutManager);
//        SingleProductRecyclerViewAdapter adapter = new SingleProductRecyclerViewAdapter(orderProductModalArrayList,OrderActivity.this);
//        orderRecyclerView1.setAdapter(adapter);
//
//    }

    private void goToCart(){
            cartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(OrderActivity.this,CartListActivity.class);
                    intent.putExtra("activity","all_category");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(OrderActivity.this);
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
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(OrderActivity.this,SelectCategotyActivity.class);
        intent.putExtra("module","order");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}
