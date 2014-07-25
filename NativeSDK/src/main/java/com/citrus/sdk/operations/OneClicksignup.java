/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.citrus.sdk.operations;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.text.TextUtils;

import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.webops.SavePayOption;
import com.citrus.sdk.webops.SignUpAsynch;
import com.citruspay.mobile.payment.JSONTaskComplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by shardul on 24/6/14.
 */
public class OneClicksignup {

    private Activity activity;

    private JSONTaskComplete listener;

    private Messenger messenger;

    private String[] signUparams;

    private JSONObject paymentDetails;

    private String type;

    public OneClicksignup(Activity activity) {
        this.activity = activity;
    }


    public void oneclickSignUp(String[] signUpParams, JSONObject paymentDetails, String type) {
        this.signUparams = signUpParams;
        this.paymentDetails = paymentDetails;
        this.type = type;
        initListeners();
        signUp(signUpParams, listener);
    }

    public void initListeners() {
        listener = new JSONTaskComplete() {
            @Override
            public void onTaskExecuted(JSONObject[] paymentObject, String message) {
                if (TextUtils.equals(message, "success")) {
                    if (TextUtils.equals(type, "netbanking")) {
                        savebankOption(paymentDetails);
                    }
                    else {
                        saveCardOption(paymentDetails);
                    }
                }

            }
        };

        messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (TextUtils.equals(result, "success")) {

                    if (TextUtils.equals(type, "netbanking")) {
                        savebankOption(paymentDetails);
                    }
                    else {
                        saveCardOption(paymentDetails);
                    }

                }
            }
        };
    }

    public void signUp(String[] signUpParams, JSONTaskComplete listener) {
        new SignUpAsynch(activity, listener).execute(signUpParams);
    }


    public void savebankOption(JSONObject paymentDetails) {

        JSONObject savePayObject = new JSONObject();

        Iterator<String> key = paymentDetails.keys();

        try {
            savePayObject.put("type", "netbanking").put("owner", "");
            while (key.hasNext()) {
                String keyValue = (String) key.next();
                savePayObject.put(keyValue, paymentDetails.getString(keyValue));
            }
        } catch (JSONException e) {

        }

        new SavePayOption(this.activity, savePayObject).execute();
    }

    public void saveCardOption(JSONObject paymentDetails) {

        JSONObject savePayObject = new JSONObject();

        Iterator<String> key = paymentDetails.keys();

        try {
            savePayObject.put("bank", "");
            while (key.hasNext()) {
                String keyValue = (String) key.next();
                savePayObject.put(keyValue, paymentDetails.getString(keyValue));
            }
        } catch (JSONException e) {

        }
        new SavePayOption(this.activity, paymentDetails).execute();
    }


    /*This method just demonstrates the one click sign up with a random Sign Up Params
    * Do not use it in real application
    * Merchant needs to provide real parameters while signing up.
    * Default Gmail id is recommended for sign up.
    */

    public String[] getSignupparams(Activity activity) {

        String email = getDefaultGmail(activity);
        String mobile = "1234567890";
        String firstName = "Tester";
        String lastName = "Citrus";
        String password = "";
        String[] params = new String[] {email, mobile, password, firstName, lastName};

        return params;
    }

    /*This code snippet takes default gmail Id of the user
    * It returns empty string if no default gmail is found
    * */

    public static final String getDefaultGmail(Activity activity) {
        String myEmailid = "";
        Account[] accounts= AccountManager.get(activity).getAccountsByType("com.google");
        if (accounts.length != 0) {
            myEmailid=accounts[0].name;
        }
        return myEmailid;
    }

}
