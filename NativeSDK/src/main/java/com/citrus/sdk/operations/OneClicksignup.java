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

import android.app.Activity;
import android.text.TextUtils;

import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.webops.ActivateAc;
import com.citrus.sdk.webops.SavePayOption;
import com.citrus.sdk.webops.SignUpAsynch;
import com.citruspay.mobile.payment.JSONTaskComplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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
                    activateAccount(messenger);
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

    public void activateAccount(Messenger messenger) {
        new ActivateAc(activity, messenger).execute();
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
    /*This method just demonstrates the one click sign up with a random email Id - Do not use it in real application*/
    public String randomEmail() {
        String email = String.valueOf(Math.random()) + "@gmail.com";
        return email;
    }

    /*This method just demonstrates the one click sign up with a random Sign Up Params
    * Do not use it in real application
    * Merchant needs to provide real parameters while signing up.
    */

    public String[] getSignupparams() {

        String email = randomEmail();
        String mobile = "1234567890";
        String firstName = "Tester";
        String lastName = "Citrus";
        String password = "";
        String[] params = new String[] {email, mobile, password, firstName, lastName};

        return params;
    }
}
