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

import com.citrus.mobile.client.load.Amount;
import com.citrus.mobile.client.load.NetCardService;
import com.citrus.mobile.client.load.PrepaidException;
import com.citrus.sdk.Constants;
import com.citrus.sdk.interfaces.Messenger;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

/**
 * Created by shardul on 26/5/14.
 */
public class ActivateAc extends AsyncTask <Void, Void, Void>{
    private NetCardService cardService;
    private MobileClient client;
    private Activity context;
    private Messenger listener;
    private String result;
    public ActivateAc(Activity context, Messenger listener) {

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        client = new MobileClient(context);
        cardService = client.getNetCardService(Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
        try {
           Amount amount = cardService.activateAccount();
        } catch (OAuth2Exception e) {
           result = "oauthException";
        } catch (ProtocolException e) {
           result = "protoException";
        } catch (PrepaidException e) {
           result = "prepaidException";
        } catch (IllegalArgumentException e) {
           result = "success";
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onTaskExecuted(result);
    }
}
