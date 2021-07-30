package com.example.root.kfgdealerpaymentv1.modal;

/**
 * Created by root on 2/19/20.
 */

public class SurveyDetailLineModal {

    int questionID;
    int answerID;
    String remarks;
    String otherfeedmill;

    public SurveyDetailLineModal(int questionID, int answerID, String remarks,String otherfeedmill) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.remarks = remarks;
        this.otherfeedmill = otherfeedmill;
    }

    public int getQuestionID() {
        return questionID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getOtherfeedmill() {
        return otherfeedmill;
    }
}
