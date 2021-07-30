package com.example.root.kfgdealerpaymentv1.modal;

/**
 * Created by root on 2/22/21.
 */

public class PaymentListModal {

    int ID;
    String paymentName;
    double amount;

    public PaymentListModal(int ID, String paymentName, double amount) {
        this.ID = ID;
        this.paymentName = paymentName;
        this.amount = amount;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public double getAmount() {
        return amount;
    }

    public int getID() {
        return ID;
    }
}
