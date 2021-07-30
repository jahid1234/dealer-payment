package com.example.root.kfgdealerpaymentv1.modal;

/**
 * Created by root on 2/12/20.
 */

public class OrderProductModal {

    String ID;
    String productName;

    public OrderProductModal(String ID, String productName) {
        this.ID = ID;
        this.productName = productName;
    }

    public String getID() {
        return ID;
    }

    public String getProductName() {
        return productName;
    }
}
