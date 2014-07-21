package com.citrus.sdk.operations;

import android.app.Activity;

import com.citrus.sdk.database.BankOptions;
import com.citrus.sdk.database.DBHandler;
import com.citrus.sdk.webops.FetchPaymentoptions;
import com.citruspay.mobile.payment.JSONTaskComplete;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shardul on 3/7/14.
 */
public class ManageOptions {
    private Activity activity;

    private JSONTaskComplete taskCompleted;

    private JSONObject paymentObject = null;

    private String result;

    public ManageOptions(Activity activity) {
        this.activity = activity;
    }

    public void getStoreBanks(JSONTaskComplete taskCompleted) {
        this.taskCompleted = taskCompleted;

        new FetchPaymentoptions(activity, this.taskCompleted).execute();
    }

    public void storeBanks(JSONArray array) {
        DBHandler handler = new DBHandler(activity);
        handler.addBankOptions(array);
        handler.close();
    }

    public List<BankOptions> getBanks() {
        DBHandler handler = new DBHandler(activity);
        List<BankOptions> bankOptionsList = handler.getBankOptions();
        handler.close();
        return bankOptionsList;
    }


}
