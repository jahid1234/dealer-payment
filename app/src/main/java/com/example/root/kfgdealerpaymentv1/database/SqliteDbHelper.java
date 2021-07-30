package com.example.root.kfgdealerpaymentv1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 2/11/20.
 */

public class SqliteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kfgdealerpaymentv1";

    public SqliteDbHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String createCustomerTable    =  "CREATE TABLE customer(customer_id INTEGER PRIMARY KEY AUTOINCREMENT,customer_code Text,customer_name Text)";
       String createPaymentListTable =  "CREATE TABLE paymentlist(paymentlist_id INTEGER PRIMARY KEY AUTOINCREMENT,paymentFor Text,paymentAmt Text)";
       String createCartDataTable    =  "CREATE TABLE cartData(cartData_id INTEGER PRIMARY KEY AUTOINCREMENT,customer_code Text,product_id Text,product_name Text,quantity Text)";
//       String createSurveyDataLineTable =  "CREATE TABLE surveyDataLine(surveyDataLine_id INTEGER PRIMARY KEY AUTOINCREMENT,survey_code Text,survey_question_id Integer,survey_answer_id Integer,remarks Text,otherfeedmill Text)";
       String createProductTable   = "CREATE TABLE productlist(m_category_id Text,category_name Text,m_product_id Text,product_name Text)";

       db.execSQL(createCustomerTable);
       db.execSQL(createPaymentListTable);
       db.execSQL(createCartDataTable);
       db.execSQL(createProductTable);
//       db.execSQL(createIsAnsweredTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");
        db.execSQL("DROP TABLE IF EXISTS paymentlist");
        db.execSQL("DROP TABLE IF EXISTS cartData");
        db.execSQL("DROP TABLE IF EXISTS productlist");
//        db.execSQL("DROP TABLE IF EXISTS isQuestionAnswered");
    }


    public boolean insertIntoProductTable(String m_category_id,String  category_name,String m_product_id,String product_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("m_category_id", m_category_id);
        contentValues.put("category_name", category_name);
        contentValues.put("m_product_id", m_product_id);
        contentValues.put("product_name", product_name);

        long res = db.insert("productlist", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertIntoCartDataTable(String customerCode,String  product_id,String product_name,String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_code", customerCode);
        contentValues.put("product_id", product_id);
        contentValues.put("product_name", product_name);
        contentValues.put("quantity", quantity);

        long res = db.insert("cartData", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean insertIntoCustomer(String code,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_code", code);
        contentValues.put("customer_name", name);

        long res = db.insert("customer", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getCustomerNameByCode(String customerCode){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select customer_name from customer where lower(customer_code) = '"+customerCode+"'  ",null);
        return res;

    }

    public void deleteCustomer(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM customer ";
        db.execSQL(query);
    }

    public boolean insertIntoPaymentList(String paymentFor,String paymentAmt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("paymentFor", paymentFor);
        contentValues.put("paymentAmt", paymentAmt);


        long res = db.insert("paymentlist", null, contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }


    public Cursor getPaymentList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from paymentlist ",null);
        return res;

    }

    public Cursor getCategory(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select Distinct m_category_id,category_name from productlist ",null);
        return res;

    }

    public Cursor getProduct(String m_category_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select m_product_id,product_name from productlist where m_category_id = '"+m_category_id+"' ",null);
        return res;

    }

    public Cursor getProductByName(String category_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select m_product_id,product_name from productlist where category_name = '"+category_name+"' ",null);
        return res;

    }

//    public Cursor getAllCategory(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select m_product_id,product_name from productlist where m_category_id = '"+m_category_id+"' ",null);
//        return res;
//
//    }


    public Cursor getCartData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from cartData",null);
        return res;

    }

    public void deletePaymentListTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM paymentlist ";
        db.execSQL(query);
    }

    public void deletePaymentListItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM paymentlist where paymentlist_id = '"+id+"'";
        db.execSQL(query);
    }

    public void deleteCartTableItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM cartData where cartData_id = '"+id+"' ";
        db.execSQL(query);
    }


    public void deleteCartTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM cartData";
        db.execSQL(query);
    }

    public void deleteProductTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productlist";
        db.execSQL(query);
    }

    public boolean updateCartItem(String primaryId,String qty){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity",qty );

        // Cursor result = db.rawQuery("update sales_order set discount = '"+discount+"' where docNo = '"+docNo+"'",null);
        long result = db.update("cartData",contentValues,"cartData_id = ?",new String[]{primaryId});
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updatePaymentListItem(String primaryId,String amount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("paymentAmt",amount );

        // Cursor result = db.rawQuery("update sales_order set discount = '"+discount+"' where docNo = '"+docNo+"'",null);
        long result = db.update("paymentlist",contentValues,"paymentlist_id = ?",new String[]{primaryId});
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
