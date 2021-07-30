package com.example.root.kfgdealerpaymentv1.modal;

/**
 * Created by root on 3/3/21.
 */

public class CartProductModal {
    int ID;
    String customerCode;
    int product_id;
    String product_name;
    String qty;

    public CartProductModal(int ID, String customerCode, int product_id, String product_name, String qty) {
        this.ID = ID;
        this.customerCode = customerCode;
        this.product_id = product_id;
        this.product_name = product_name;
        this.qty = qty;
    }

    public int getID() {
        return ID;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQty() {
        return qty;
    }
}
