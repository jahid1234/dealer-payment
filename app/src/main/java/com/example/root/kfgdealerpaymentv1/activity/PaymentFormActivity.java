package com.example.root.kfgdealerpaymentv1.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.adapter.PaymentListRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.adapter.QuestionRecyclerViewAdapter;
import com.example.root.kfgdealerpaymentv1.database.PostgresqlConnection;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;
import com.example.root.kfgdealerpaymentv1.modal.PaymentListModal;
import com.example.root.kfgdealerpaymentv1.modal.QuestionModal;
import com.example.root.kfgdealerpaymentv1.modal.SurveyDetailLineModal;
import com.example.root.kfgdealerpaymentv1.modal.SurveyDetailModal;

import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentFormActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;
    Toolbar toolbar;
    RecyclerView questionRecyclerView;
    SqliteDbHelper sqliteDbHelper;
    ImageButton sendToServerBtn,addPaymentImageBtn;
    ProgressDialog pd;
    Spinner bankListSpinner,paymentMethodListSpinner,bankListSpinner1,paymentListSpinner;
    EditText paymentAmtEditText;

    Button viewPaymentBtn;
    double longitude = 0,latitude = 0;
   // static boolean firstQuestionDone = false,secondQuestionDone = false;

    SqliteDbHelper dbHelper;
    ArrayList<QuestionModal> questionModalArrayList = new ArrayList<>();
    ArrayList<SurveyDetailModal> surveyDetailModalArrayList = new ArrayList<>();
    ArrayList<SurveyDetailLineModal> surveyDetailLineModalArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(PaymentFormActivity.this,SearchCustomerActivity.class);
                intent.putExtra("module","payment");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbHelper = new SqliteDbHelper(this);
        sendToServerBtn = findViewById(R.id.sendToServer_btn);
        sqliteDbHelper = new SqliteDbHelper(this);
        questionRecyclerView = findViewById(R.id.question_recyclerview);
        bankListSpinner   = findViewById(R.id.bank_list_spinner);
        bankListSpinner1   = findViewById(R.id.bank_list_spinner1);
        paymentMethodListSpinner = findViewById(R.id.paymt_method_list_spinner);
        paymentListSpinner = findViewById(R.id.payment_list_spinner);

        addPaymentImageBtn = findViewById(R.id.add_paymentImageBtn);
        paymentAmtEditText = findViewById(R.id.amtNumEditText);
        viewPaymentBtn = findViewById(R.id.viewPaymentBtn_id);





        bankListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        bankListSpinner1.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        paymentMethodListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        paymentListSpinner.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

        loadBankSpinner();
        loadBankSpinner1();
        loadPaymentMethodSpinner();
        loadPaymentListSpinner();
//        AsyncInsertQuestion task = new AsyncInsertQuestion();
//        task.execute();

        //loadQuestion();
        sendToServer();
        addPaymentMethod();
        viewPayment();

        sqliteDbHelper.deletePaymentListTable();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }




    private void viewPayment(){
        viewPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = new Activity();
                List<PaymentListModal> paymentListArrayList = new ArrayList<>();
                final Dialog dialog = new Dialog(PaymentFormActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//                Rect displayRectangle = new Rect();
//                Window window = activity.getWindow();
//                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//
//                LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View layout = inflater.inflate(R.layout.activity_payment_list, null);
//                layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
//                layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

               // dialog.setContentView(layout);
                dialog.setContentView(R.layout.activity_payment_list);

                RecyclerView paymentListRecyclerView = dialog.findViewById(R.id.payment_list_recyclerview);
                ImageButton closeDialogBtn = dialog.findViewById(R.id.close_dialogBtn_id);
               // ImageButton saveDialogBtn = dialog.findViewById(R.id.save_dialogBtn_id);

                int ID = 0;
                String paymentFor = "";
                double paymentAmt = 0.0;

                Cursor result = sqliteDbHelper.getPaymentList();
                if (result.moveToFirst()) {
                    do{
                        ID = result.getInt(0);
                        paymentFor  = result.getString(1);
                        paymentAmt = result.getDouble(2);

                        PaymentListModal paymentListModal = new PaymentListModal(ID,paymentFor,paymentAmt);
                        paymentListArrayList.add(paymentListModal);


                    }while(result.moveToNext());
                }

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext(),LinearLayoutManager.VERTICAL,false);
               // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                paymentListRecyclerView.setLayoutManager(linearLayoutManager);
                PaymentListRecyclerViewAdapter RecyclerViewAdapter = new PaymentListRecyclerViewAdapter(paymentListArrayList,PaymentFormActivity.this);
                paymentListRecyclerView.setAdapter(RecyclerViewAdapter);

                closeDialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


    private void toastMessage(String message) {
        Toast toast = Toast.makeText(PaymentFormActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private void addPaymentMethod(){


        addPaymentImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String paymentFor = paymentListSpinner.getSelectedItem().toString();
                String paymentAmt = paymentAmtEditText.getText().toString();
                if(paymentAmt.equals("")){
                    toastMessage("Please enter amount");
                    return;
                }
                boolean res = sqliteDbHelper.insertIntoPaymentList(paymentFor,paymentAmt);
                if(res){
                    toastMessage("Payment Added");
                    paymentAmtEditText.setText("");
                }else{
                    toastMessage("Error while adding payment");
                }
            }
        });
    }

    private void loadBankSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.color_spinner_layout,getResources().getStringArray(R.array.bank_name_arrays));

        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        bankListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void loadBankSpinner1(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.color_spinner_layout,getResources().getStringArray(R.array.bank_name_arrays));

        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        bankListSpinner1.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }
    private void loadPaymentMethodSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.color_spinner_layout,getResources().getStringArray(R.array.payment_method_arrays));

        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        paymentMethodListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }

    private void loadPaymentListSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.color_spinner_layout,getResources().getStringArray(R.array.payment_category_arrays));

        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        paymentListSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }



    private void loadQuestion(){
        questionModalArrayList.clear();
//        Cursor result = sqliteDbHelper.getQuestions();
//        if (result.moveToFirst()) {
//            do{
//                String question   = result.getString(0);
//                int questionID    = result.getInt(1);
//                String isAnswered = result.getString(2);
//                QuestionModal questionModal = new QuestionModal(question,questionID,isAnswered);
//                questionModalArrayList.add(questionModal);
//            }while(result.moveToNext());
//        }

//        String[] paymentMethodArray = getResources().getStringArray(R.array.payment_category_arrays);
//        for(int i=0;i<paymentMethodArray.length;i++){
//            String methodName = paymentMethodArray[i];
//            QuestionModal questionModal = new QuestionModal(methodName);
//            questionModalArrayList.add(questionModal);
//        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        questionRecyclerView.setLayoutManager(linearLayoutManager);
        QuestionRecyclerViewAdapter adapter = new QuestionRecyclerViewAdapter(questionModalArrayList,PaymentFormActivity.this);
        questionRecyclerView.setAdapter(adapter);
    }


    private void sendToServer(){



        sendToServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServerBtn.setEnabled(false);
                gps = new GPSTracker(PaymentFormActivity.this);
                if (gps.canGetLocation) {
                    getLocation();
                } else {
                    gps.showSettingsAlert();
                }

//                Cursor result = dbHelper.getSurveyData(survey_code);
//                if (result.moveToFirst()) {
//                    do{
//                        int userID = result.getInt(0);
//                        String customer_code = result.getString(1);
//
//                        SurveyDetailModal surveyDetailModal = new SurveyDetailModal(userID,customer_code,survey_code);
//                        surveyDetailModalArrayList.add(surveyDetailModal);
//                    }while(result.moveToNext());
//
//                    Cursor cursor = dbHelper.getSurveyDataLine(survey_code);
//                        if(cursor.moveToFirst()){
//                            do{
//                                int questionID = cursor.getInt(0);
//                                int answerID   = cursor.getInt(1);
//                                String remarks = cursor.getString(2);
//                                String otherfeedmill = cursor.getString(3);
//
//                                SurveyDetailLineModal surveyDetailLineModal = new SurveyDetailLineModal(questionID,answerID,remarks,otherfeedmill);
//                                surveyDetailLineModalArrayList.add(surveyDetailLineModal);
//                            }while(cursor.moveToNext());
//                        }
//                }

                if(!surveyDetailModalArrayList.isEmpty() && !surveyDetailLineModalArrayList.isEmpty()){
                    boolean vpnCheck = checkVPN();
                    if(vpnCheck){
                        AsyncSendToServer task = new AsyncSendToServer();
                        task.execute();
                    }else {
                        Toast.makeText(PaymentFormActivity.this, "Connect Vpn", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PaymentFormActivity.this, "No Answer Exists", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private final class AsyncSendToServer extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            try{
                if(s.equals("Success")){

                }else{
                    Toast.makeText(PaymentFormActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException ex){
                Toast.makeText(PaymentFormActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            PostgresqlConnection postgresqlConnection = new PostgresqlConnection();
            Connection connection =  postgresqlConnection.getConn();
            boolean isCommit = false;
            String result = "Fail";
            if(connection != null) {
                try {
                    connection.setAutoCommit(false);


                    if(isCommit){
                        connection.commit();
                        result = "Success";
                    }else {
                        try {
                            connection.rollback();
                        } catch (SQLException e) {
                            connection.rollback();
                        }
                    }
                } catch (SQLException ex) {
                    result = ex.getMessage();
                    try{
                        if(connection != null){
                            connection.rollback();
                        }
                    }catch (SQLException ex2){
                       result =  ex2.getMessage();
                    }
                }finally {
                    try {
                        if(connection != null) {
                            connection.close();
                        }else{
                            result = "Connection Failed";
                        }
                    } catch (SQLException e) {
                        result = e.getMessage();
                    }
                }

            }else{
                Toast.makeText(PaymentFormActivity.this, "Check connection,port", Toast.LENGTH_SHORT).show();
            }
            return result;
        }
    }


    private void showProgressDialog(){
        if(pd == null){
            pd = new ProgressDialog(PaymentFormActivity.this);
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


    private void getLocation(){
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        System.out.println("Latitude :" + String.valueOf(latitude));
        System.out.println("Longitude :" + String.valueOf(longitude));
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
        Intent intent = new Intent(PaymentFormActivity.this,SearchCustomerActivity.class);
        intent.putExtra("module","payment");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}


class GPSTracker extends Service implements LocationListener {

    private final Context mContext;


    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {

        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            Log.d("Network", "Network");
                            if (locationManager != null) {
                             location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                             }

                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                                 Log.d("GPS Enabled", "GPS Enabled");
                                if (locationManager != null) {
                                     location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                }


                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */

    public double getLatitude() {
        if (location != null) {
            this.latitude = location.getLatitude();
        }
        // System.out.println("inside latitude" + latitude);
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */

    public double getLongitude() {
        if (location != null) {
            this.longitude = location.getLongitude();
        }
        //System.out.println("Longitude" + longitude);
        // return longitude
        return longitude;
    }


    public boolean canGetLocation() {
        // System.out.println("inside Can Get");
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Gps Setting");
        alertDialog.setMessage("Gps is not enabled do you want to enable it");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}