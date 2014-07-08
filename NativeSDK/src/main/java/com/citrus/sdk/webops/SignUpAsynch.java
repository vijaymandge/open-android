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

import com.citrus.sdk.Constants;
import com.citruspay.mobile.client.MobileClient;
import com.citruspay.mobile.client.subscription.SubscriptionException;
import com.citruspay.mobile.client.subscription.SubscriptionService;
import com.citruspay.mobile.payment.OnTaskCompleted;
import com.citruspay.mobile.payment.client.rest.ProtocolException;
import com.citruspay.mobile.payment.oauth2.OAuth2Exception;

import org.json.JSONException;

public class SignUpAsynch extends AsyncTask<String, Void, String>{
	private MobileClient mobileClient;
	private SubscriptionService subscriptionService;
	private Activity activity;
	private OnTaskCompleted listener;
	
	public SignUpAsynch(Activity activity, OnTaskCompleted listener) {
		this.activity = activity;
		this.listener = listener;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		mobileClient = new MobileClient(this.activity, Constants.CITRUS_OAUTH_URL);
		subscriptionService = mobileClient.getSubscriptionService(Constants.SUBSCRIPTION_ID, Constants.SUBSCRIPTION_SECRET, Constants.SIGNIN_ID, Constants.SIGNIN_SECRET);
		
		try {
			subscriptionService.signup(params[0], params[1], params[2], params[3], params[4]);
			result = "success";
		} catch (ProtocolException e) {
			e.printStackTrace();
			result = "failure";
		} catch (OAuth2Exception e) {
			e.printStackTrace();
			result = "failure";
		} catch (SubscriptionException e) {
            try {
                if (TextUtils.equals(Constants.USER_EXISTS, e.getContent().getString("type"))) {
                    result = e.getContent().getString("type");
                }
            } catch (JSONException ex) {

            }
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			result = "failure";
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		listener.onTaskExecuted(null, result);
	}

}
