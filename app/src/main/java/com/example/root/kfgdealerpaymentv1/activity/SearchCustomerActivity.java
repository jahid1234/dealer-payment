package com.example.root.kfgdealerpaymentv1.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.kfgdealerpaymentv1.R;
import com.example.root.kfgdealerpaymentv1.database.SqliteDbHelper;

public class SearchCustomerActivity extends AppCompatActivity {

    public static String customer_Code = "";
    EditText    codeEditText;
    ImageButton searchCustomer,refreshBtn,forwardBtn,backwardBtn;
    TextView nameTextView;
    SqliteDbHelper sqliteDbHelper;
    String moduleName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            moduleName = (String) bundle.get("module");
        }

        codeEditText = findViewById(R.id.bp_codeEditText);
        searchCustomer = findViewById(R.id.get_name_ImageBtn);
        refreshBtn  = findViewById(R.id.refresh_imageBtn_id);
        forwardBtn  = findViewById(R.id.forward_arrow_imageBtn_id);
        backwardBtn  = findViewById(R.id.back_imageBtn_id);

        nameTextView = findViewById(R.id.bp_nameTextView);
        sqliteDbHelper = new SqliteDbHelper(this);

        sqliteDbHelper.deleteCartTable();
        findCustomer();
        buttonClick();
    }

    private void findCustomer(){

        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerCode = codeEditText.getText().toString().toLowerCase();
                Cursor cursor =sqliteDbHelper.getCustomerNameByCode(customerCode);

                if (cursor.moveToFirst()) {
                    do{
                        nameTextView.setText(cursor.getString(0));
                        codeEditText.setEnabled(false);
                        customer_Code = codeEditText.getText().toString();
                    }while(cursor.moveToNext());

                }else{
                    nameTextView.setText("");
                    toastMessage("No Customer found You should Sync First");
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast toast = Toast.makeText(SearchCustomerActivity.this,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private void buttonClick(){
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEditText.setEnabled(true);
                codeEditText.setText("");
                nameTextView.setText("");
                customer_Code = "";
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameTextView.getText().toString().equals("")){
                    toastMessage("No Customer Name Found");
                    return;
                }
                if(moduleName.equals("payment")) {
                    finish();
                    Intent intent = new Intent(SearchCustomerActivity.this, PaymentFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }else{
                    finish();
                    Intent intent = new Intent(SearchCustomerActivity.this, SelectCategotyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            }
        });

        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SearchCustomerActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(SearchCustomerActivity.this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
