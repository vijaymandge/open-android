package com.citrus.sdk.operations;

import android.app.Activity;

import com.citrus.sdk.Constants;
import com.citrus.sdk.database.BankOptions;
import com.citrus.sdk.database.DBHandler;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.options.PaymentOptions;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by shardul on 3/7/14.
 */
public class ManageOptions {
    private Activity activity;

    private OnTaskCompleted taskCompleted;

    private PaymentOptions myOptions;

    public ManageOptions(Activity activity) {
        this.activity = activity;
    }

    public void getStoreBanks(OnTaskCompleted taskCompleted) {
        this.taskCompleted = taskCompleted;
        init();
        fetchBanks();
    }

    private void init() {
        myOptions = new PaymentOptions(activity);
    }

    private void fetchBanks() {
        myOptions.getPaymentOptions(Constants.VANITY_URL, taskCompleted);
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
