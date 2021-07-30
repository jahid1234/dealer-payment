package com.example.root.kfgdealerpaymentv1.modal;

/**
 * Created by root on 2/13/20.
 */

public class SurveyDetailModal {

    int userId;
    String customerCode;
    String surveyCode;


    public SurveyDetailModal(int userId, String customerCode, String surveyCode) {
        this.userId = userId;
        this.customerCode = customerCode;
        this.surveyCode = surveyCode;

    }

    public int getUserId() {
        return userId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getSurveyCode() {
        return surveyCode;
    }
}
