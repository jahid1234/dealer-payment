package com.example.root.kfgdealerpaymentv1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.database.PostgresqlConnection;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;

import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView syncButton,paymentBtn,orderBtn;
    ImageButton nextActivityImageBtn,refreshImageBtn;
    SqliteDbHelper dbHelper;
    ProgressDialog pd;
    EditText codeEditText,nameEditText;

    static String customarName = "",customerCode = "";
    static String survey_code = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new SqliteDbHelper(this);
        toolbar = findViewById(R.id.toolbar);
        syncButton = findViewById(R.id.syncBtn_customer);
        paymentBtn = findViewById(R.id.paymentcardId);
        orderBtn   = findViewById(R.id.ordercardId);
        //nameEditText = findViewById(R.id.editText_name);
        //codeEditText = findViewById(R.id.editText_code);
        //nextActivityImageBtn = findViewById(R.id.nextActivityImageBtn);
        //refreshImageBtn      = findViewById(R.id.refreshActivityImageBtn);

        // nameEditText.setEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

               // overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        buttonClick();
       // onCustomerCodeEditTextFocusChange();
       // nextActivity();
    }

//
//    private void onCustomerCodeEditTextFocusChange(){
//        codeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                String customerCode = codeEditText.getText().toString().toLowerCase();
//                Cursor cursor =dbHelper.getCustomerNameByCode(customerCode);
//
//                if (cursor.moveToFirst()) {
//                    do{
//                       nameEditText.setText(cursor.getString(0));
//                       nameEditText.setEnabled(false);
//                       codeEditText.setEnabled(false);
//                   }while(cursor.moveToNext());
//
//                }else{
//                    nameEditText.setText("");
//                    toastMessage("No Customer found You should Sync First");
//                }
//            }
//        });
//    }


    private void buttonClick(){
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  boolean checkVpn = checkVPN();
                boolean checkVpn = true;
                if(checkVpn) {
                    AsyncSyncCustomer task = new AsyncSyncCustomer();
                    task.execute();
                }else {
                    toastMessage("Please Connect Vpn");
                }
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this, SearchCustomerActivity.class);
                intent.putExtra("module","payment");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
               // overridePendingTransition(0,0);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this, SearchCustomerActivity.class);
                intent.putExtra("module","order");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
               // overridePendingTransition(0,0);
            }
        });
    }


    private final class AsyncSyncCustomer extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
          //  dbHelper.deleteCustomer();
            dbHelper.deleteProductTable();
           // dbHelper.deleteQuestion();
            String result = "";
            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection = postgresqlConnection.getConn();
            if(connection != null){
                try{

                    Statement statement1 = connection.createStatement();
//                    String getCustomer_query = "select bpcode,bpname  from t_customer_kfg where isactive = 'Y' ";
//                    ResultSet resultSet1 = statement1.executeQuery(getCustomer_query);
//                    if(resultSet1 != null) {
//                        while (resultSet1.next()) {
//                            String customer_code = resultSet1.getString("bpcode");
//                            String customer_name = resultSet1.getString("bpname");
//                            dbHelper.insertIntoCustomer(customer_code, customer_name);
//                            result = "Sync-Successful";
//                        }
//                    }

                    String getCategory = "Select COALESCE(M_Product_Category_ID,0) as M_Product_Category_ID,name from M_Product_Category where ad_client_ID=1000000 and name like 'Finis%' or m_product_category_parent_ID=1000263 order by name";
                    ResultSet resultSet2 = statement1.executeQuery(getCategory);
                    if(resultSet2 != null) {
                        while (resultSet2.next()) {
                            int m_category_id = resultSet2.getInt("M_Product_Category_ID");
                            String category_name = resultSet2.getString("name");

                            String getProduct = "select COALESCE(m_product_id,0) as m_product_id,name from m_product where m_product_category_id = '"+m_category_id+"'";

                            Statement statement2 = connection.createStatement();
                            ResultSet resultSet3 = statement2.executeQuery(getProduct);
                            while (resultSet3.next()) {
                                    int m_product_id = resultSet3.getInt("m_product_id");
                                    String product_name = resultSet3.getString("name");
                                    dbHelper.insertIntoProductTable(String.valueOf(m_category_id),category_name,String.valueOf(m_product_id), product_name);
                                    result = "Sync-Successful";
                            }
                        }
                    }

                } catch (Exception ex){
                    result = ex.getMessage();
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
                if(s.equals("Sync-Successful")){
                    toastMessage(s);
                }else {
                    showAlert("Sync Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }

//    private void nextActivity(){
//        nextActivityImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                  customerCode = codeEditText.getText().toString();
//                  customarName = nameEditText.getText().toString();
//                if(customarName.isEmpty()){
//                    toastMessage("Please select Customer first");
//                    return;
//                }
//
//                long now = System.currentTimeMillis();
//                survey_code = ad_user_id +"-"+ String.valueOf(now);
//
//                boolean headerInsert = dbHelper.insertIntoSurveyTable(ad_user_id,customerCode,survey_code);
//                if(headerInsert){
//                    finish();
//                    Intent intent = new Intent(HomeActivity.this, PaymentFormActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//
//                }else{
//                    toastMessage("No survey head created");
//                }
//            }
//        });
//
//        refreshImageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                codeEditText.setText("");
//                codeEditText.setEnabled(true);
//                nameEditText.setText("");
//                nameEditText.setEnabled(true);
//            }
//        });
//    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(HomeActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(HomeActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(HomeActivity.this);
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

    public boolean checkVPN(){

        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {
            //Timber.d("isVpnUsing Network List didn't received");
        }

        return networkList.contains("tun0");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
       // overridePendingTransition(0,0);
    }
}
