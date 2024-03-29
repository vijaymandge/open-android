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
import android.os.AsyncTask;

import com.citrus.sdk.Constants;
import com.citrus.sdk.interfaces.Messenger;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.OptionDetails;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

/**
 * Created by shardul on 3/6/14.
 */
public class SetDefault {
    private Activity activity;
    private MobileClient client;
    private SubscriptionService subscriptionService;
    private String error;
    private OptionDetails option;
    private Messenger messanger;

    public SetDefault(Activity activity) {
        this.activity = activity;
    }

    public void setdefaultOption(OptionDetails option, Messenger messanger) {
        client = new MobileClient(this.activity, Constants.CITRUS_OAUTH_URL);
        subscriptionService = client.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
        this.option = option;
        this.messanger = messanger;
        error = "";
        new updateDefault().execute();
    }

    private class updateDefault extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                subscriptionService.setDefaultOption(option);
            } catch (ProtocolException e) {
                error = "proto";
            } catch (OAuth2Exception e) {
                error = "oauth";
            } catch (SubscriptionException e) {
                error = "subsc";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           messanger.onTaskExecuted(error);
        }
    }



}
