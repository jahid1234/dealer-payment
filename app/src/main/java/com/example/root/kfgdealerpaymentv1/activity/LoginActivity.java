package com.example.root.kfgdealerpaymentv1.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    EditText userNameEditText,passwordEditText;
    CheckBox rememberCkBox;
    Button loginButton;

    String userName,password;
    public static int ad_user_id = 0;

    private SharedPreferences rememberLoginPreference;
    private SharedPreferences.Editor rememberLoginPreferenceEditor;
    boolean rememberLogin = false;

    SqliteDbHelper dbHelper;
    ProgressDialog pd;
    boolean doublePressBackbtnToExit = false;

    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Shared Preference
        rememberLoginPreference = getSharedPreferences("loginPrefsCustomerVisit",MODE_PRIVATE);
        rememberLoginPreferenceEditor = rememberLoginPreference.edit();

        dbHelper = new SqliteDbHelper(this);

        initializeUI();
        checkPermissionMethod();
        clickLoginButton();
        loadFromSharedPreferences();
    }

    private void initializeUI(){
        userNameEditText = findViewById(R.id.employee_editText);
        passwordEditText = findViewById(R.id.password_editText);

        rememberCkBox = findViewById(R.id.remember_checkbox);
        loginButton   = findViewById(R.id.login_btn);
    }

    private void checkPermissionMethod(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                ActivityCompat.requestPermissions(LoginActivity.this, permissionsRequired, 1);
                return;

            }
        }
    }

    private void loadFromSharedPreferences(){
        rememberLogin = rememberLoginPreference.getBoolean("rememberLogin",false);
        if(rememberLogin){
            userNameEditText.setText(rememberLoginPreference.getString("userName",""));
            passwordEditText.setText(rememberLoginPreference.getString("password",""));
            rememberCkBox.setChecked(true);
        }
    }

    private void clickLoginButton(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if(userName.isEmpty()){
                    userNameEditText.setError("Please Enter user Name");
                    return;
                }

                if(password.isEmpty()){
                    passwordEditText.setError("Please Enter Password");
                    return;
                }

                if(rememberCkBox.isChecked()){

                    rememberLoginPreferenceEditor.putBoolean("rememberLogin",true);
                    rememberLoginPreferenceEditor.putString("userName",userName);
                    rememberLoginPreferenceEditor.putString("password",password);
                    rememberLoginPreferenceEditor.commit();
                }else {
                    rememberLoginPreferenceEditor.clear();
                    rememberLoginPreferenceEditor.commit();
                }


                finish();
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                //overridePendingTransition(0,0);


             //   boolean vpn = checkVPN();
//                boolean vpn = true;
//                if(vpn) {
//                    AsyncLogin task = new AsyncLogin();
//                    task.execute();
//                }else{
//                    showAlert("Warning","Please Check Vpn is Connect");
//                }
            }
        });
    }


    private final class AsyncLogin extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection = postgresqlConnection.getConn();
            if(connection != null){
                try{
                    Statement statement = connection.createStatement();
                    String sql_query = "select ad_user_id from ad_user where name = '" + userName + "' and password = '" + password + "' ";
                    ResultSet resultSet = statement.executeQuery(sql_query);
                    if(resultSet.next()){
                        ad_user_id = resultSet.getInt("ad_user_id");
                        result = "login-successful";
                    }else{
                        result = "Unauthorized UserName or Wrong-Password";
                    }

                }catch (SQLException ex){
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
                if(s.equals("login-successful")){
                    finish();
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    showAlert("Login Error",s);
                }
            }catch (NullPointerException ex){
                showAlert("Error",ex.getMessage());
            }
        }
    }



    private void toastMessage(String message) {
        Toast toast = Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
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


    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(LoginActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Logging in.....");
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
        if(doublePressBackbtnToExit){
            super.onBackPressed();
            finish();
        }

        doublePressBackbtnToExit = true;
        toastMessage("Press again to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePressBackbtnToExit = false;
            }
        },2000);
    }

}
