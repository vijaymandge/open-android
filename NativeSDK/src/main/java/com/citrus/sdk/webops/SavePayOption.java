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

package com.citrus.sdk.webops;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.payment.PaymentConfiguration;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

/**
 * Created by shardul on 26/5/14.
 */
public class SavePayOption extends AsyncTask<Void, Void, Void>{
    private Activity activity;
    private JSONObject cardDetails, paymentOption;
    private MobileClient client;
    private SubscriptionService subscriptionService;
    private String result;
    public SavePayOption(Activity activity, JSONObject paymentOption) {
        this.activity = activity;
        this.paymentOption = paymentOption;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            cardDetails = new JSONObject();
            cardDetails.put("type", "payment");
        } catch (Exception e) {

        }


    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            cardDetails.put("paymentOptions", new JSONArray(Collections.singleton(this.paymentOption)));
        } catch (JSONException e) {

        }
        client = new MobileClient(this.activity, Constants.CITRUS_OAUTH_URL);
        subscriptionService = client.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
        PaymentConfiguration payment = new PaymentConfiguration();
        payment.parse(cardDetails);
        try {
            subscriptionService.updateProfile(payment);
            result = "success";
        } catch (ProtocolException e) {
            result = "proto";
        } catch (OAuth2Exception e) {
            result = "oauth";
        } catch (SubscriptionException e) {
            result = "subsc";
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (TextUtils.equals(result, "success")) {
            Toast.makeText(activity.getApplicationContext(), "Payment Option Saved.", Toast.LENGTH_LONG).show();
        }
    }
}
