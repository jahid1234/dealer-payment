package com.example.root.kfgdealerpaymentv1.activity;

/**
 * Created by root on 3/1/21.
 */

public class ChildItem {
    // Declaration of the variable
    private String ID;
    private String ChildItemTitle;

    // Constructor of the class
    // to initialize the variable*

    public ChildItem(String ID, String childItemTitle) {
        this.ID = ID;
        ChildItemTitle = childItemTitle;
    }

    // Getter and Setter method
    // for the parameter
    public String getChildItemTitle()
    {
        return ChildItemTitle;
    }

    public void setChildItemTitle(
            String childItemTitle)
    {
        ChildItemTitle = childItemTitle;
    }

    public String getID() {
        return ID;
    }
}
