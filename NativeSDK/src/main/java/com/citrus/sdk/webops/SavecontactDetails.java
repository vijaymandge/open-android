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
import android.widget.Toast;

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.client.subscription.contact.ContactDetails;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONObject;

/**
 * Created by shardul on 7/7/14.
 */
public class SavecontactDetails extends AsyncTask<Void, Void, Void> {

    private Activity activity;

    private JSONObject profile;

    private MobileClient client;

    private SubscriptionService subscriptionService;

    private String result;

    public SavecontactDetails(Activity activity, JSONObject profile) {
        this.activity = activity;
        this.profile = profile;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        client = new MobileClient(activity, Constants.CITRUS_OAUTH_URL);

        ContactDetails contactDetails = new ContactDetails();

        contactDetails.parse(profile);

        subscriptionService = client.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
        try {
            subscriptionService.updateProfile(contactDetails);

            result = "Contact Details Updated";

        } catch (ProtocolException e) {
        result = "proto Exception";
        } catch (OAuth2Exception e) {
        result = "oauth Exception";
        } catch (SubscriptionException e) {
        result = "subsc Exception";
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(activity.getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
}
