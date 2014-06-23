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
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.citrus.sdk.Constants;
import com.citrus.sdk.interfaces.Messenger;
import com.citrus.sdk.webops.Web3DSecure;
import com.citruspay.mobile.payment.internals.PaymentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by shardul on 27/5/14.
 */
public class GuestCheckout {
    private Activity activity;
    private Messenger messenger;

    public GuestCheckout(Activity activity) {
        this.activity = activity;

    }

    public void cardPay(String type, String amount) {
        messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Intent intent = new Intent(activity, Web3DSecure.class);
                    intent.putExtra("redirectUrl", result);
                    activity.startActivity(intent);
                }
                else {
                    Toast.makeText(activity.getApplicationContext(), "Could not process your request", Toast.LENGTH_SHORT).show();
                }
            }
        };
        String txnId = String.valueOf(System.currentTimeMillis());
        JSONObject paymentObject = new JSONObject();
        JSONObject personalDetails, paymentDetails;

        try {
            personalDetails = JSONUtils.fillinGuestPersonalDetails();
            paymentDetails = JSONUtils.fillinGuestPayCardDetails(type);

            Iterator<String> personal_iterator = personalDetails.keys();
            Iterator<String> payment_iterator = paymentDetails.keys();

            while (personal_iterator.hasNext()) {
                String key = (String) personal_iterator.next();
                paymentObject.put(key, personalDetails.getString(key));
            }

            while (payment_iterator.hasNext()) {
                String key = (String) payment_iterator.next();
                paymentObject.put(key, paymentDetails.getString(key));
            }

            paymentObject.put("merchantTxnId", txnId);

            paymentObject.put("amount", amount);

            paymentObject.put("returnUrl", Constants.REDIRECT_URL);

            new com.citrus.sdk.webops.GuestPay(activity, paymentObject, messenger, txnId).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void netbankPay(String bankCode, String amount) {
        messenger = new Messenger() {
            @Override
            public void onTaskExecuted(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Intent intent = new Intent(activity, Web3DSecure.class);
                    intent.putExtra("redirectUrl", result);
                    activity.startActivity(intent);
                }
                else {
                    Toast.makeText(activity.getApplicationContext(), "Could not process your request", Toast.LENGTH_SHORT).show();
                }
            }
        };
        String txnId = String.valueOf(System.currentTimeMillis());
        JSONObject paymentObject = new JSONObject();
        JSONObject paymentDetails, personalDetails;
        try {
            paymentDetails = JSONUtils.fillinGuestPayNetDetails(bankCode);
            personalDetails = JSONUtils.fillinGuestPersonalDetails();

            Iterator<String> personal_iterator = personalDetails.keys();
            Iterator<String> payment_iterator = paymentDetails.keys();

            while (personal_iterator.hasNext()) {
                String key = (String) personal_iterator.next();
                paymentObject.put(key, personalDetails.getString(key));
            }

            while (payment_iterator.hasNext()) {
                String key = (String) payment_iterator.next();
                paymentObject.put(key, paymentDetails.getString(key));
            }

            paymentObject.put("merchantTxnId", txnId);

            paymentObject.put("amount", amount);

            paymentObject.put("returnUrl", Constants.REDIRECT_URL);

        } catch (JSONException e) {

        }
        new com.citrus.sdk.webops.GuestPay(activity, paymentObject, messenger, txnId).execute();
    }
}
