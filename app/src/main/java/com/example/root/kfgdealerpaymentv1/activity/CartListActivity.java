package com.example.root.kfgdealerpaymentv1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.adapter.CartListRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.adapter.PaymentListRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.database.PostgresqlConnection;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.CartProductModal;
import com.example.root.kfgdealerpaymentv1.modal.PaymentListModal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.root.kfgdealerpaymentv1.activity.LoginActivity.ad_user_id;
import static com.example.root.kfgdealerpaymentv1.activity.SearchCustomerActivity.customer_Code;

public class CartListActivity extends AppCompatActivity {

   // Spinner regionListSpinner,territoryListSpinner;
   // ImageButton nextActivity;
    ProgressDialog pd;
    SqliteDbHelper sqliteDbHelper;
    RecyclerView cartListRecyclerView;
    Toolbar toolbar;
    String productRequestType;
    Button orderConfirmBtn;

    List<CartProductModal> cartProductModalArrayList = new ArrayList<>();
    List<String> territoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            productRequestType = (String)bundle.get("activity");
        }

        cartListRecyclerView = findViewById(R.id.cart_list_recyclerview);
        orderConfirmBtn = findViewById(R.id.order_place_btn_id);
        sqliteDbHelper = new SqliteDbHelper(this);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productRequestType.equals("all_category")){
                    finish();
                    Intent intent = new Intent(CartListActivity.this,OrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                }else if(productRequestType.equals("single_category")){
                    finish();
                    Intent intent = new Intent(CartListActivity.this,SingleCategoryProductsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }else{
                    finish();
                    Intent intent = new Intent(CartListActivity.this,SelectCategotyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }

            }
        });
         AsyncLoadCart asyncCart = new AsyncLoadCart();
         asyncCart.execute();
       //  nextA();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.productcolor));
        }

        orderPlace();
    }


    private void orderPlace(){
        orderConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncConfirmOrder asyncConfirmOrder = new AsyncConfirmOrder();
                asyncConfirmOrder.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(productRequestType.equals("all_category")){
            finish();
            Intent intent = new Intent(CartListActivity.this,OrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else if(productRequestType.equals("single_category")){
            finish();
            Intent intent = new Intent(CartListActivity.this,SingleCategoryProductsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            finish();
            Intent intent = new Intent(CartListActivity.this,SelectCategotyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void nextA(){


    }


    private final class AsyncConfirmOrder extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            long now = System.currentTimeMillis();
            String order_id = ad_user_id +"-"+ String.valueOf(now);
            String result = "";
            boolean isCommit = false;
            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection = postgresqlConnection.getConn();
            if(connection != null){
                try{
                    connection.setAutoCommit(false);
                    Statement statement = connection.createStatement();
                    for(int i = 0;i<cartProductModalArrayList.size();i++){
                        String insert_query = "insert into t_productorderdata (order_id,ad_user_id,bpcode,product_id,product_name,Quantity,appversion) " +
                                "values ('" + order_id + "','" + ad_user_id + "','"+customer_Code+"',''"+getResources().getString(R.string.app_version)+"')";

                        int res = statement.executeUpdate(insert_query);
                        if(res == 1){
                            result = "Insertion-Successful";
                            isCommit = true;
                        }else{
                            isCommit = false;
                            break;
                        }
                    }

                    if(isCommit){
                        connection.commit();
                        result = "complete";
                    }else {
                        try {
                            connection.rollback();
                        } catch (SQLException e) {
                            connection.rollback();
                        }
                    }
                }catch (SQLException ex){
                    result = ex.getMessage();
                    try {
                        if (connection != null) {
                            connection.rollback();
                        }
                    }catch (Exception ex2){
                        result = ex2.getMessage();
                    }
                }finally {
                    if(connection != null){
                        try{
                            connection.close();
                        }catch (SQLException ex){
                            result = ex.getMessage();
                        }
                    }
                }
            }else{
                result = "Check Port,hostname is correct";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("complete")){
                    toastMessage(s);
                }else {
                    showAlert("Insertion Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(CartListActivity.this);
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

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(CartListActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(CartListActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    private final class AsyncLoadCart extends AsyncTask<String,String,String>{
        int ID = 0;
        String customerCode = "";
        int  productID = 0;
        String productName = "";
        String qty = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(s.equals("Success")){
                    if(cartProductModalArrayList.isEmpty()){
                        orderConfirmBtn.setEnabled(false);
                    }
                    initRecyclerView();
                }
            }catch (NullPointerException ex){
                Toast.makeText(CartListActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            //sqliteDbHelper.deleteTemporaryQuestion();
            Cursor result = sqliteDbHelper.getCartData();
            if (result.moveToFirst()) {
                do{
                    ID = result.getInt(0);
                    customerCode  = result.getString(1);
                    productID = result.getInt(2);
                    productName = result.getString(3);
                    qty = result.getString(4);

                    CartProductModal cartProductModal = new CartProductModal(ID,customerCode,productID,productName,qty);
                    cartProductModalArrayList.add(cartProductModal);
                    res = "Success";

                }while(result.moveToNext());

            }
            return res;
        }
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        cartListRecyclerView.setLayoutManager(linearLayoutManager);
        CartListRecyclerViewAdapter RecyclerViewAdapter = new CartListRecyclerViewAdapter(cartProductModalArrayList,CartListActivity.this);
        cartListRecyclerView.setAdapter(RecyclerViewAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
